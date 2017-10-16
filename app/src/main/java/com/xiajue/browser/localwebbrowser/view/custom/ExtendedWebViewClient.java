package com.xiajue.browser.localwebbrowser.view.custom;

import android.graphics.Bitmap;

import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

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

    @Override
    public void onReceivedError(WebView view, int errorCode,
                                String description, String failingUrl) {
        isError = true;
        onLoadError();
    }

    @Override
    public void onPageFinished(WebView webView, String s) {
        super.onPageFinished(webView, s);
        ((ExtendedWebView) webView).onLoadFinish();
        if (!isError) {
            onLoadSuccess();
        }
    }

    public abstract void onLoadError();

    public abstract void onLoadSuccess();
}
