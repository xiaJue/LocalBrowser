package com.xiajue.browser.localwebbrowser.view.custom;

import android.content.Context;
import android.util.AttributeSet;

import com.tencent.smtt.sdk.WebView;

/**
 * xiaJue 2017/9/20创建
 */
public class ExtendedWebView extends WebView {

    public ExtendedWebView(Context context) {
        super(context);
    }

    public ExtendedWebView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ExtendedWebView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override
    public boolean canScrollHorizontally(int direction) {
        final int offset = computeHorizontalScrollOffset();
        final int range = computeHorizontalScrollRange() - computeHorizontalScrollExtent();
        if (range == 0) return false;
        else return (direction < 0) ? (offset > 0) : (offset < range - 1);
    }
}
