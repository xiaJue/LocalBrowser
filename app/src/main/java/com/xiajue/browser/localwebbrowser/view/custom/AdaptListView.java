package com.xiajue.browser.localwebbrowser.view.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import java.util.List;

/**
 * Created by Moing_Admin on 2017/11/13.
 */

public class AdaptListView extends ListView {
    public AdaptListView(Context context) {
        super(context);
    }

    public AdaptListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AdaptListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private List mList;

    public void setList(List list) {
        mList = list;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.onInterceptTouchEvent(ev);
    }

    private int downY;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            downY = (int) ev.getY();
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            if (getChildCount() > 0) {
                int moveY = (int) ev.getY();
                boolean firstItemVisible = getFirstVisiblePosition() == 0;
                boolean topOfFirstItemVisible = getChildAt(0).getTop() == 0;
                //顶端
                if (downY < moveY && firstItemVisible && topOfFirstItemVisible) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                boolean lastItemVisible = getLastVisiblePosition() == mList.size() - 1;
                View lastItem = getChildAt(getChildCount() - 1);
                if (lastItem != null) {
                    boolean bottomOfFirstItemVisible = lastItem.getBottom() <= getHeight();
                    //底端
                    if (downY > moveY && lastItemVisible && bottomOfFirstItemVisible) {
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                }
            }
        }
        return super.onTouchEvent(ev);
    }
}
