package com.xiajue.browser.localwebbrowser.view.custom;

import android.content.Context;
import android.util.AttributeSet;

import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

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

    @Override
    public void loadUrl(String s) {
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
    }

    public boolean isError() {
        return mClient.isError;
    }

    public void closeLoad() {
        loadUrl("");
        clearHistory();
    }
}
