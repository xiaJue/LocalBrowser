package com.xiajue.browser.localwebbrowser.view.activity.frametag;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.xiajue.browser.localwebbrowser.model.manager.DownloadManager;
import com.xiajue.browser.localwebbrowser.model.manager.Settings;
import com.xiajue.browser.localwebbrowser.model.manager.SettingsUtils;
import com.xiajue.browser.localwebbrowser.model.utils.L;
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

    public WebFragment(HomePresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_fragment_web, container, false);

        bindView(view);
        setWebView();
        return view;
    }


    public void goBack() {
        mWebView.goBack();
    }

    private void bindView(View view) {
        mWebView = (ExtendedWebView) view.findViewById(R.id.home_webView);
        mErrorLayout = view.findViewById(R.id.home_web_error);
        mProgressBar = (ProgressBar) view.findViewById(R.id.home_progressBar);
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
                mWebView.setVisibility(View.GONE);
                putAndSettings(mWebView.getTitle().isEmpty
                        () ? getString(R.string.load_error) : mWebView.getTitle());
            }

            @Override
            public void onLoadSuccess() {
                L.e("success");
                //保存到历史数据库
                mErrorLayout.setVisibility(View.GONE);
                mWebView.setVisibility(View.VISIBLE);
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
                }
            }
        });
        mWebView.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                Toast.makeText(getContext(), R.string.start_download, Toast.LENGTH_SHORT).show();
                DownloadManager.download(Settings.getFileSavePath(getContext(), StringUtils
                        .getName(url), StringUtils.getEnd(url)), url, new DownloadManager
                        .DownloadCallback() {

                    @Override
                    public void success(final File file) {
                        getActivity().runOnUiThread(new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), getString(R.string.download_success)
                                        + "--" + file.getAbsolutePath(), Toast
                                        .LENGTH_SHORT).show();
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
                            }
                        }));
                    }
                });
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse(url));
//                startActivity(i);
            }
        });
        mWebView.setOnLongClickListener(mPresenter.onLongClickWebView());
        mWebView.setOnTouchListener(mPresenter.onTouchEvent());

        int type = Settings.getStartSettingsType(getContext());
        switch (type) {
            case 0:
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
        if (!mWebView.getUrl().contains("about:blank") && !mWebView.getUrl().equals("") &&
                !mWebView.isReload && !mWebView.isGoBack) {
            activity.mHomeFragment.mPresenter.putHistoryData(title, mWebView.getUrl());
        }
        //保存最后访问的网页
        String lastUrl = mWebView.getUrl();
        SettingsUtils.setLastUrl(getContext(), lastUrl);
        activity.homeFlag = false;
        //web状态下才更新toolbar标题
        if ((activity.getViewPager().getCurrentItem() == 1) && isSetTitle) {
            activity.mToolbarText.setText(mWebView
                    .getTitle());
        }
    }

    /**
     * 关闭网页
     */
    public void closeLoad() {
        getHomeActivity().setToolbarTitle(getString(R.string.web));
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
