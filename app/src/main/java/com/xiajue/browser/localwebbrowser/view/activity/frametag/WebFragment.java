package com.xiajue.browser.localwebbrowser.view.activity.frametag;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.xiajue.browser.localwebbrowser.R;
import com.xiajue.browser.localwebbrowser.model.Config;
import com.xiajue.browser.localwebbrowser.model.manager.DownloadManager;
import com.xiajue.browser.localwebbrowser.model.manager.HomeEventManager;
import com.xiajue.browser.localwebbrowser.model.manager.NotificationManager;
import com.xiajue.browser.localwebbrowser.model.manager.Settings;
import com.xiajue.browser.localwebbrowser.model.manager.SettingsUtils;
import com.xiajue.browser.localwebbrowser.model.utils.L;
import com.xiajue.browser.localwebbrowser.model.utils.OpenFileUtils;
import com.xiajue.browser.localwebbrowser.model.utils.StringUtils;
import com.xiajue.browser.localwebbrowser.presenter.HomePresenter;
import com.xiajue.browser.localwebbrowser.view.activity.HomeActivity;
import com.xiajue.browser.localwebbrowser.view.custom.ExtendedWebView;
import com.xiajue.browser.localwebbrowser.view.custom.ExtendedWebViewClient;

import java.io.File;

import static com.xiajue.browser.localwebbrowser.model.manager.Settings.LAST_URL;

/**
 * xiaJue 2017/9/19创建
 */
public class WebFragment extends BaseFramtag {
    private HomePresenter mPresenter;
    private ExtendedWebView mWebView;
    private ProgressBar mProgressBar;
    private View mErrorLayout;
    private boolean isSetTitle = true;//是否需要设置title为urlTitle
    private FloatingActionButton mFAB;//底部按钮
    private NotificationManager mNotificationManager;//底部按钮

    @SuppressLint("ValidFragment")
    public WebFragment(HomePresenter presenter, Context context) {
        mPresenter = presenter;
        mNotificationManager = new NotificationManager(context);
    }

    public WebFragment() {
    }

    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_fragment_web, container, false);

        bindView(view);
        setWebView();
        setBottomButton();
        return view;
    }

    public void goBack() {
        mWebView.goBack();
    }


    private void setBottomButton() {
        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWebView.canGoBack()) {
                    goBack();
                } else {
                    closeLoad();//关闭网页
                }
            }
        });
    }

    private void bindView(View view) {
        mWebView = (ExtendedWebView) view.findViewById(R.id.home_webView);
        mErrorLayout = view.findViewById(R.id.home_web_error);
        mProgressBar = (ProgressBar) view.findViewById(R.id.home_progressBar);
        mFAB = (FloatingActionButton) view.findViewById(R.id.home_web_fab);
    }


    public ExtendedWebView getWebView() {
        return mWebView;
    }

    private void setWebView() {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);//支持javaScript
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);//支持手势缩放
        settings.setUseWideViewPort(true);
        settings.supportMultipleWindows();
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setSupportMultipleWindows(true);
        mWebView.clearHistory();
        mWebView.setWebViewClient(new ExtendedWebViewClient() {
            @Override
            public void onLoadError() {
                //保存到历史数据库
                L.e("error");
                mErrorLayout.setVisibility(View.VISIBLE);
                putAndSettings(mWebView.getTitle().isEmpty
                        () ? getString(R.string.load_error) : mWebView.getTitle());
            }

            @Override
            public void onLoadSuccess() {
                L.e("success");
                //保存到历史数据库
                mErrorLayout.setVisibility(View.GONE);
                putAndSettings(mWebView.getTitle().isEmpty
                        () ? getString(R.string.load_success_not_title) : mWebView.getTitle());
            }
        });
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView webView, int i) {
                super.onProgressChanged(webView, i);
                mProgressBar.setVisibility(View.VISIBLE);
                mProgressBar.setProgress(i);
                if (i == 100) {
                    mProgressBar.setVisibility(View.GONE);
                    if (mWebView.canGoBack()) {
                        mFAB.setVisibility(View.VISIBLE);
                        mFAB.setImageResource(R.mipmap.back);
                    } else {
                        mFAB.setVisibility(View.GONE);
                        mErrorLayout.setVisibility(View.GONE);
                    }
                }
            }
        });
        mWebView.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(final String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                DownloadManager.download(Settings.getFileSavePath(getContext(), StringUtils
                        .getName(url), ""), url, new DownloadManager
                        .DownloadCallback() {

                    @Override
                    public void success(final File file) {
                        getActivity().runOnUiThread(new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), getString(R.string
                                        .download_success)
                                        + "--" + file.getAbsolutePath(), Toast
                                        .LENGTH_SHORT).show();
                                Intent fileIntent = OpenFileUtils.getFileIntent(getActivity(),
                                        file);
                                if (fileIntent != null) {
                                    PendingIntent intentPend = PendingIntent.getActivity
                                            (getContext(), 0, fileIntent, PendingIntent
                                                    .FLAG_CANCEL_CURRENT);
                                    //显示通知栏通知
                                    mNotificationManager.pushDownloadNotification(getString(R
                                            .string.download_success), StringUtils
                                            .getName(url), intentPend);
                                }
                            }
                        }));
                    }

                    @Override
                    public void failure() {
                        getActivity().runOnUiThread(new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), R.string.download_failure, Toast
                                        .LENGTH_SHORT).show();
                                //显示通知栏通知
                                mNotificationManager.pushDownloadNotification(getString(R.string
                                        .download_failure), StringUtils.getName(url), null);
                            }
                        }));
                    }
                });
                Toast.makeText(getContext(), R.string.start_download, Toast.LENGTH_SHORT)
                        .show();
            }
        });
        mWebView.setOnLongClickListener(mPresenter.onLongClickWebView());
        mWebView.setOnTouchListener(mPresenter.onTouchEvent());

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

    //存储数据到数据库
    private void putAndSettings(String title) {
        HomeActivity activity = getHomeActivity();

        if (!mWebView.getUrl().contains(Config.WEB_ABOUT_BLANK) && !mWebView.getUrl().equals("") &&
                !mWebView.isReload && !mWebView.isGoBack) {
            activity.getHomeFragment().mPresenter.putHistoryData(title, mWebView.getUrl());
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
        mErrorLayout.setVisibility(View.GONE);
    }
}
