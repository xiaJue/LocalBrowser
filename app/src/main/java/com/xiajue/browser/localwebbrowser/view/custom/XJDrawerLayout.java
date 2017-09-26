package com.xiajue.browser.localwebbrowser.view.custom;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * xiaJue 2017/9/15创建
 */
public class XJDrawerLayout extends DrawerLayout {

    public XJDrawerLayout(Context context) {
        super(context);
    }

    public XJDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XJDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private int downX;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                if (downX > (int) ev.getX()) {
                    return false;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

}
