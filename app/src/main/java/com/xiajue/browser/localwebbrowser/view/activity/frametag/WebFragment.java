package com.xiajue.browser.localwebbrowser.view.activity.frametag;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.xiajue.browser.localwebbrowser.R;
import com.xiajue.browser.localwebbrowser.model.Config;
import com.xiajue.browser.localwebbrowser.model.manager.HomeEventManager;
import com.xiajue.browser.localwebbrowser.model.manager.Settings;
import com.xiajue.browser.localwebbrowser.model.manager.SettingsUtils;
import com.xiajue.browser.localwebbrowser.model.utils.L;
import com.xiajue.browser.localwebbrowser.presenter.HomePresenter;
import com.xiajue.browser.localwebbrowser.view.activity.HomeActivity;
import com.xiajue.browser.localwebbrowser.view.custom.ExtendedWebView;
import com.xiajue.browser.localwebbrowser.view.custom.ExtendedWebViewClient;

import static com.xiajue.browser.localwebbrowser.model.manager.Settings.LAST_URL;

/**
 * xiaJue 2017/9/19创建
 */
public class WebFragment extends BaseFramtag implements ExtendedWebView.OnLoadFinishListener,
        View.OnClickListener {
    private HomePresenter mPresenter;
    private ExtendedWebView mWebView;
    private ProgressBar mProgressBar;
    private ImageView mCloseLoad;
    private View mErrorLayout;
    private boolean isSetTitle = true;//是否需要设置title为urlTitle
    private FloatingActionButton mFAB;//底部按钮
    private View mBottomRoot;//
    private ImageView mUnFullButton;//unFull
    private ImageView mMenuButton;//menu
    private ImageView mShowUnFull;

    @SuppressLint({"NewApi", "ValidFragment"})
    public WebFragment(HomePresenter presenter, Context context) {
        mPresenter = presenter;
    }

    public WebFragment() {
    }

    private void bindView(View view) {
        mWebView = (ExtendedWebView) view.findViewById(R.id.home_webView);
        mErrorLayout = view.findViewById(R.id.home_web_error);
        mProgressBar = (ProgressBar) view.findViewById(R.id.home_progressBar);
        mFAB = (FloatingActionButton) view.findViewById(R.id.home_web_fab);
        mBottomRoot = view.findViewById(R.id.home_web_bottom_root);
        mUnFullButton = (ImageView) view.findViewById(R.id.home_web_unFull_but);
        mMenuButton = (ImageView) view.findViewById(R.id.home_web_menu_but);
        mCloseLoad = (ImageView) view.findViewById(R.id.home_web_close_load);
        mShowUnFull = (ImageView) view.findViewById(R.id.home_web_show_unFull);
    }

    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_fragment_web, container, false);
        bindView(view);
        setWebView();
        setCloseLoad();
        setBottomButton();
        setUnFullButton();
        setMenuButton();
        return view;
    }

    private void setCloseLoad() {
        mCloseLoad.setOnClickListener(this);
    }

    private void setMenuButton() {
        mMenuButton.setOnClickListener(this);
    }

    private void setUnFullButton() {
        mShowUnFull.setOnClickListener(this);
        mUnFullButton.setOnClickListener(this);
    }

    private void setBottomButton() {
        mFAB.setOnClickListener(this);
    }

    private void setWebView() {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);//支持javaScript
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);//支持手势缩放
        settings.setUseWideViewPort(true);
        settings.setDisplayZoomControls(false);
        settings.supportMultipleWindows();
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setSupportMultipleWindows(true);
        //设置 缓存模式
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setAppCacheEnabled(true);
        // 开启 DOM storage API 功能
        settings.setDomStorageEnabled(true);
        //提高渲染的优先级
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //处理下载
        mWebView.setDownloadListener(new ExtendedWebView.ExtendedDownloadListener(getContext()));
        //处理长按事件
        mWebView.setOnLongClickListener(mPresenter.onLongClickWebView());
        //处理触摸事件
        mWebView.setOnTouchListener(mPresenter.onTouchEvent());
        mWebView.setOnLoadFinishListener(this);
        mWebView.setWebViewClient(new ExtendedWebViewClient() {
            @Override
            public void onLoadError() {
                //保存到历史数据库
                L.e("error url=" + mWebView.getUrl());
                mErrorLayout.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
                mCloseLoad.setVisibility(View.GONE);
                putAndSettings(mWebView.getTitle().isEmpty
                        () ? getString(R.string.load_error) : mWebView.getTitle());
            }

            @Override
            public void onLoadSuccess() {
                L.e("success url=" + mWebView.getUrl());
                //保存到历史数据库
                mErrorLayout.setVisibility(View.GONE);
                putAndSettings(mWebView.getTitle().isEmpty
                        () ? getString(R.string.load_success_not_title) : mWebView.getTitle());
            }
        });
        //更新进度条
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView webView, int i) {
                super.onProgressChanged(webView, i);
                mProgressBar.setVisibility(View.VISIBLE);
                mCloseLoad.setVisibility(View.VISIBLE);
                mProgressBar.setProgress(i);
                if (i == 100) {
                    mProgressBar.setVisibility(View.GONE);
                    mCloseLoad.setVisibility(View.GONE);
                    //更新回退按钮状态
                    if (mWebView.canGoBack()) {
                        mFAB.setVisibility(View.VISIBLE);
                        mFAB.setImageResource(R.mipmap.back);
                    } else {
                        mFAB.setVisibility(View.GONE);
                        mErrorLayout.setVisibility(View.GONE);
                        //取消全屏
                        HomeEventManager.getInstance().unFullScreen();
                    }
                }
            }
        });
        //初始加载
        int type = Settings.getStartSettingsType(getContext());
        switch (type) {
            case 0:
                mWebView.loadUrl(Config.WEB_ABOUT_BLANK);
                break;
            case 1:
                String lastUrl = SettingsUtils.getLastUrl(getContext(), LAST_URL);
                mWebView.loadUrl(lastUrl);
                break;
            case 2:
                String inUrl = SettingsUtils.getInUrl(getContext());
                mWebView.loadUrl(inUrl);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_web_close_load:
                mWebView.stopLoading();
                mProgressBar.setVisibility(View.GONE);
                v.setVisibility(View.GONE);
                break;
            case R.id.home_web_fab:
                if (mWebView.canGoBack()) {
                    goBack();
                }
                break;
            case R.id.home_web_unFull_but:
                HomeEventManager.getInstance().unFullScreen();
                mBottomRoot.setVisibility(View.GONE);
                break;
            case R.id.home_web_menu_but:
                //显示overflow菜单
                getHomeActivity().getActivity().getPopupMenu().show();
                break;
            case R.id.home_web_show_unFull:
                HomeEventManager.getInstance().showUnFullButton();
                mShowUnFull.setVisibility(View.GONE);
                break;
        }
    }

    public ExtendedWebView getWebView() {
        return mWebView;
    }

    //存储数据到数据库
    private void putAndSettings(String title) {
        HomeActivity activity = getHomeActivity();
        String url = mWebView.getUrl();
        if (!url.contains(Config.WEB_ABOUT_BLANK) && !url.equals("") &&
                !mWebView.isReload && !mWebView.isGoBack && !url.equals(Config
                .getSetWebViewBgColorString(getContext()))) {
            activity.getHomeFragment().mPresenter.putHistoryData(title, url);
        }
        //保存最后访问的网页
        String lastUrl = mWebView.getUrl();
        SettingsUtils.setLastUrl(getContext(), lastUrl);
        activity.homeFlag = false;
        //web状态下才更新toolbar标题
        if ((activity.getViewPager().getCurrentItem() == 1) && isSetTitle) {
            HomeEventManager.getInstance().setToolbarTitle(mWebView.getTitle());
        }
    }

    /**
     * 关闭网页
     */
    public void closeLoad() {
        HomeEventManager.getInstance().setToolbarTitle(getString(R.string.web));
        mWebView.closeLoad();
        mFAB.setVisibility(View.GONE);
        isSetTitle = false;
        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                isSetTitle = true;
            }
        }.start();
    }

    @Override
    public void onCloseFinish() {
        //尽量避免出现系统自带的错误页面
        mErrorLayout.setVisibility(View.GONE);
    }

    @Override
    public void onLoadFinish() {
    }

    public View getBottomRoot() {
        return mBottomRoot;
    }

    public View getShowUnFull() {
        return mShowUnFull;
    }

    public void goBack() {
        mWebView.goBack();
    }

    @Override
    public void onResume() {
        super.onResume();
//        HomeEventManager.getInstance().hideStatus(mWebView);
    }

    public void closeError() {
        mErrorLayout.setVisibility(View.GONE);
    }
}
