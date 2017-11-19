package com.xiajue.browser.localwebbrowser.view.custom;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Toast;

import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.xiajue.browser.localwebbrowser.R;
import com.xiajue.browser.localwebbrowser.model.Config;
import com.xiajue.browser.localwebbrowser.model.manager.DialogManager;
import com.xiajue.browser.localwebbrowser.model.manager.HomeEventManager;
import com.xiajue.browser.localwebbrowser.model.manager.Settings;
import com.xiajue.browser.localwebbrowser.model.manager.MDownloadManager;
import com.xiajue.browser.localwebbrowser.model.utils.L;
import com.xiajue.browser.localwebbrowser.model.utils.StringUtils;

import java.io.File;

/**
 * xiaJue 2017/9/20创建
 */
public class ExtendedWebView extends WebView {
    public ExtendedWebViewClient mClient;
    public boolean isReload = false;//是否是刷新
    public boolean isGoBack = false;//是否是回退

    public ExtendedWebView(Context context) {
        this(context, null);
    }

    public ExtendedWebView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ExtendedWebView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override
    public void setWebViewClient(WebViewClient webViewClient) {
        this.mClient = (ExtendedWebViewClient) webViewClient;
        super.setWebViewClient(webViewClient);
    }

    private boolean isClose;

    @Override
    public void loadUrl(String s) {
        if (s.equals(Config.WEB_ABOUT_BLANK) || s.equals("")) {
            //加载空白页时将背景色设置为windowBackColor
            isClose = true;
            loadData("<body bgColor=\"" + Config.getResColorWinBack(getContext()) +
                    "\"></body>", "text/html", "UTF-8");
            return;
        }
        if (!StringUtils.isUrl(s)) {
            L.e("false url =" + s);
            return;
        }
        super.loadUrl(s);
    }

    @Override
    public void reload() {
        isReload = true;
        super.reload();
    }

    /**
     * 当网页加载完成后调用该方法
     */
    public void onLoadFinish() {
        isReload = false;
        isGoBack = false;
        if (mFinishListener != null) {
            if (isClose) {
                mFinishListener.onCloseFinish();
                isClose = false;
            }
            mFinishListener.onLoadFinish();
        }
    }

    public boolean isError() {
        return mClient.isError;
    }

    private OnLoadFinishListener mFinishListener;

    public void setOnLoadFinishListener(OnLoadFinishListener loadFinishListener) {
        this.mFinishListener = loadFinishListener;
    }

    public void closeLoad() {
        stopLoading();
        loadUrl("");
        clearHistory();
    }

    @Override
    public boolean canGoBack() {
        if (getUrl().equals(Config.getSetWebViewBgColorString(getContext()))) {
            return false;
        }
        return super.canGoBack();
    }

    @Override
    public String getTitle() {
        if (super.getTitle() == null) {
            return "";
        }
        return super.getTitle();
    }

    private OnTouchListener mTouchListener;

    /**
     * 防止网页缩放屏蔽触摸时事件
     */
    @Override
    public void setOnTouchListener(OnTouchListener onTouchListener) {
        this.mTouchListener = onTouchListener;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mTouchListener.onTouch(this, ev);
        if (getUrl() != null) {
            if (getUrl().equals(Config.WEB_ABOUT_BLANK) || getUrl().equals(Config
                    .getSetWebViewBgColorString(getContext()))) {
                return false;//回溯
            }
        }
//        if (isStopScroll) {
//            return false;
//        }
        return super.dispatchTouchEvent(ev);
    }

//    private boolean isStopScroll = false;

//    public void stopScroll(boolean b) {
//        isStopScroll = b;
//    }

    public interface OnLoadFinishListener {
        void onLoadFinish();

        void onCloseFinish();
    }

    public static class ExtendedDownloadListener implements DownloadListener {
        private Context mContext;

        public ExtendedDownloadListener(Context context) {
            mContext = context;
        }

        public Context getContext() {
            return mContext;
        }

        @Override
        public void onDownloadStart(final String url, String userAgent,
                                    String contentDisposition, String mimeType,
                                    long contentLength) {
            L.e("下载...");
            MDownloadManager.getReallyFileName(url, new MDownloadManager
                    .OnGetUrlFileName() {
                @Override
                public void onGetUrlFileName(String name) {
                    //此为UI线程
                    //获得下载path
                    final String downloadPath = Settings.getFileSavePath(getContext(), name,
                            "");
                    L.e("downloadPath=" + downloadPath);
                    showDialog(downloadPath);
                    Toast.makeText(getContext(), R.string.start_download,
                            Toast.LENGTH_SHORT).show();
                    HomeEventManager.getInstance().download(url, downloadPath);
                }
            });
        }

        private boolean isContinueDownload;

        /**
         * 显示一个询问对话框
         */
        private void showDialog(String downloadFile) {
            if (new File(downloadFile).exists()) {
                //显示一个询问对话框
                DialogManager.showInquiry(getContext(), StringUtils.ellipsis(StringUtils.getName
                        (downloadFile), 10) +
                                "已存在," + "是否继续下载？",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                isContinueDownload = true;
                                //停止阻塞
                                throw new RuntimeException();
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                isContinueDownload = false;
                                //停止阻塞
                                throw new RuntimeException();
                            }
                        });
                try {
                    //阻塞线程
                    Looper.loop();
                } catch (Exception e) {
                }
                if (!isContinueDownload) {
                    return;
                } else {
                    new File(downloadFile).delete();
                }
            }
        }

    }
}
