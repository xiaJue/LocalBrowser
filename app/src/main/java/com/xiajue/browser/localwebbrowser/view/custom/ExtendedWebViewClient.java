package com.xiajue.browser.localwebbrowser.view.custom;

import android.graphics.Bitmap;
import android.os.Build;

import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.xiajue.browser.localwebbrowser.model.utils.L;
import com.xiajue.browser.localwebbrowser.model.utils.StringUtils;

/**
 * Created by Moing_Admin on 2017/10/9.
 */

public abstract class ExtendedWebViewClient extends WebViewClient {

    public boolean isError = false;

    @Override
    public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
        super.onPageStarted(webView, s, bitmap);
        isError = false;
    }

    //旧版本API
    @Override
    public void onReceivedError(WebView webView, int errorCode,
                                String description, String failingUrl) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return;
        }
        if (testIsError(webView)) {
            L.e("error received code=" + errorCode);
            isError = true;
            onLoadError();
        }
    }

    //新版本API23以上
    @Override
    public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest,
                                WebResourceError webResourceError) {
        super.onReceivedError(webView, webResourceRequest, webResourceError);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        L.e("error received new api code=");
        if (testIsError(webView)) {
            isError = true;
            onLoadError();
        } else {
            L.e("receive lost...");
        }
    }

    private boolean testIsError(WebView webView) {
        return true;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, String s) {
        //为什么不反过来试试?
        if (!StringUtils.isUrl(s)) {
            return true;
        }
        return false;
    }

    @Override
    public void onPageFinished(WebView webView, String s) {
        super.onPageFinished(webView, s);
        if (!isError && webView.getUrl() != null) {
            onLoadSuccess();
        }
        ((ExtendedWebView) webView).onLoadFinish();
    }

    public abstract void onLoadError();

    public abstract void onLoadSuccess();
}
