package com.xiajue.browser.localwebbrowser.view.activity.frametag;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.xiajue.browser.localwebbrowser.R;
import com.xiajue.browser.localwebbrowser.model.manager.SettingsManager;
import com.xiajue.browser.localwebbrowser.model.utils.L;
import com.xiajue.browser.localwebbrowser.presenter.HomePresenter;
import com.xiajue.browser.localwebbrowser.view.activity.HomeActivity;

/**
 * xiaJue 2017/9/19创建
 */
public class WebFragment extends Fragment {
    private HomePresenter mPresenter;
    private WebView mWebView;
//    private SwipeRefreshLayout mRefreshLayout;
    private ProgressBar mProgressBar;

    public WebFragment(HomePresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_fragment_web, container, false);

        mWebView = (WebView) view.findViewById(R.id.home_webView);
//        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.home_refreshLayout);
        mProgressBar = (ProgressBar) view.findViewById(R.id.home_progressBar);
//        setRefreshLayout();
        setWebView();
        return view;
    }

//    private void setRefreshLayout() {
//        mRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
//        mRefreshLayout.setColorSchemeResources(
//                R.color.refresh_color,
//                android.R.color.holo_green_light,
//                android.R.color.holo_orange_light,
//                android.R.color.holo_red_light);
//        mRefreshLayout.setOnRefreshListener(
//                new SwipeRefreshLayout.OnRefreshListener() {
//                    @Override
//                    public void onRefresh() {
//                        // 刷新动画开始后回调到此方法
////                        mWebView.reload();
//                        mWebView.reload();
//                        if (mWebView.getUrl() == null) {
//                            mRefreshLayout.setRefreshing(false);
//                        }
//                    }
//                }
//        );
//        mRefreshLayout.setOnChildScrollUpCallback(new SwipeRefreshLayout.OnChildScrollUpCallback() {
//            @Override
//            public boolean canChildScrollUp(SwipeRefreshLayout parent, @Nullable View child) {
//                //顶部才刷新webView
//                return mWebView.getWebScrollY() != 0;
//            }
//        });
//    }

    private void setWebView() {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);//支持javaScript
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);//支持手势缩放
        settings.setUseWideViewPort(true);
        settings.supportMultipleWindows();
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setSupportMultipleWindows(true);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient());
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView webView, int i) {
                super.onProgressChanged(webView, i);
                mProgressBar.setVisibility(View.VISIBLE);
                mProgressBar.setProgress(i);
                if (i == 100) {
                    mProgressBar.setVisibility(View.GONE);
//                    mRefreshLayout.setRefreshing(false);
                    ((HomeActivity) getActivity()).homeFlag = false;
                    //web状态下才更新toolbar标题
                    if (((HomeActivity) getActivity()).getViewPager().getCurrentItem() == 1)
                        ((HomeActivity) getActivity()).getActivity().mToolbarText.setText(mWebView
                                .getTitle());
                }
                //保存最后访问的网页
                String lastUrl = mWebView.getUrl();
                SettingsManager.putSettingsString(getContext(), SettingsManager.LAST_URL, lastUrl);
            }
        });
        mWebView.setOnLongClickListener(mPresenter.onLongClickWebView());
        mWebView.setOnTouchListener(mPresenter.onTouchEvent());

        int type = SettingsManager.getStartSettingsType(getContext());
        switch (type) {
            case 0:
                break;
            case 1:
                String lastUrl = SettingsManager.getSettingsString(getContext(),
                        SettingsManager.LAST_URL);
                L.e("lastUrl=" + lastUrl);
                mWebView.loadUrl(lastUrl);
                break;
            case 2:
                String inUrl = SettingsManager.getSettingsString(getContext(),
                        SettingsManager.IN_URL);
                mWebView.loadUrl(inUrl);
                break;
        }
    }

    public WebView getWebView() {
        return mWebView;
    }
}
