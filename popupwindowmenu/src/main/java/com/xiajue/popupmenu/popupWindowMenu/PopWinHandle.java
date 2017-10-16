package com.xiajue.popupmenu.popupWindowMenu;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by xiaJue on 2017/10/6.
 */

public class PopWinHandle {
    private PopupWindow mPopupWindow;
    private View mPopupView;
    private LinearLayout mBgLinearLayout;
    private LinearLayout mLinearLayout;
    private LayoutInflater mInflater;
    private Context mContext;

    public PopWinHandle(Context context, int menuWidth) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mPopupView = mInflater.inflate(R.layout.popup_menu_layout, null);
        mBgLinearLayout = (LinearLayout) mPopupView
                .findViewById(R.id.popup_menu_bg_ll);
        mLinearLayout = (LinearLayout) mPopupView
                .findViewById(R.id.popup_menu_ll);
        mPopupWindow = new PopupWindow(mPopupView, menuWidth, WRAP_CONTENT);
        mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
    }

    /**
     * 添加一个菜单条目
     */
    public void add(View itemView) {
        mLinearLayout.addView(itemView);
    }

    private boolean initSetTouch = true;

    /**
     * 显示菜单
     */
    public void show(View view, int gravity, int x, int y) {
        if (isOutTouch && initSetTouch) {
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            initSetTouch = false;
        }
        mPopupWindow.showAtLocation(view, gravity, x, y);
    }

    /**
     * 取消显示
     */
    public void dismiss() {
        mPopupWindow.dismiss();
    }

    public View getPopupView() {
        return mPopupView;
    }

    public PopupWindow getPopupWindow() {
        return mPopupWindow;
    }

    public LinearLayout getBgLayout() {
        return mBgLinearLayout;
    }

    public LinearLayout getLayout() {
        return mLinearLayout;
    }

    public LayoutInflater getInflater() {
        return mInflater;
    }

    private boolean isOutTouch = true;

    public void setOutsideTouchable(boolean isOut) {
        isOutTouch = isOut;
    }
}
