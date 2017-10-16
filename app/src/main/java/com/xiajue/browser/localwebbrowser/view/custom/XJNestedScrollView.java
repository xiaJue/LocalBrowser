package com.xiajue.browser.localwebbrowser.view.custom;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Moing_Admin on 2017/10/14.
 */

public class XJNestedScrollView extends NestedScrollView {
    public XJNestedScrollView(Context context) {
        super(context);
    }

    public XJNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XJNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private boolean isCancelScroll = false;

    public void setCancelScroll(boolean cancelScroll) {
        isCancelScroll = cancelScroll;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isCancelScroll) {
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }
}
