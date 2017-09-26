package com.xiajue.browser.localwebbrowser.view.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ImageView;

import com.xiajue.browser.localwebbrowser.R;
import com.xiajue.browser.localwebbrowser.model.utils.L;

/**
 * xiaJue 2017/9/16创建
 */
public class CollectionImageButton extends ImageView {
    public CollectionImageButton(Context context) {
        this(context, null);
    }

    public CollectionImageButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CollectionImageButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        setImageResource(res1);
        resId = res2;
    }

    private boolean isDown;
    private int touchSlop, downX, downY;
    private int res2 = R.mipmap.nocollection;
    private int res1 = R.mipmap.collection;
    private boolean flag = true;
    public int resId;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDown = true;
                L.e("down");
                downX = (int) getX();
                downY = (int) getY();
                break;
            case MotionEvent.ACTION_MOVE:
                L.e("move");
                if (Math.sqrt(Math.pow((downX - getX()), 2) * Math.pow((downY - getY()), 2)) > touchSlop) {
                    isDown = false;
                    L.e("move out");
                }
                break;
            case MotionEvent.ACTION_UP:
                L.e("up");
                if (isDown) {
                    //click
                    L.e("click");
                    setImageResource(resId != res1 ? res1 : res2);
                    resId = resId != res1 ? res1 : res2;
                    flag = !flag;
                }
                isDown = false;
                break;
        }
        return super.onTouchEvent(event);
    }

    public void setCollection(boolean collection) {
        setImageResource(collection ? res1 : res2);
        resId =collection ? res1 : res2;
    }
}
