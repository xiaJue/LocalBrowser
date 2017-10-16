package com.xiajue.popupmenu.popupWindowMenu;

/**
 * Created by Moing_Admin on 2017/10/1.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.View;

/**
 * popupWindow item 的属性
 * 如果需要自定义布局，调用setView方法即可
 */
public class MenuItemInfo {

    public MenuItemInfo() {
    }

    public MenuItemInfo(String title) {
        mTitle = title;
    }

    public MenuItemInfo(String title, Bitmap icon) {
        mTitle = title;
        mIcon = icon;
    }

    public MenuItemInfo(String title, Bitmap icon, int textSize, int textColor, int bgColor) {
        mTitle = title;
        mIcon = icon;
        mTextSize = textSize;
        mTextColor = textColor;
        mBgColor = bgColor;
    }

    public MenuItemInfo(Context context, int stringRes, int iconRes, int textSize, int textColor,
                        int bgColor) {
        mTitle = context.getString(stringRes);
        mIcon = BitmapFactory.decodeResource(context.getResources(), iconRes);
        mTextSize = textSize;
        mTextColor = textColor;
        mBgColor = bgColor;
    }

    public MenuItemInfo(View view) {
        mView = view;
    }

    private String mTitle = "";
    private Bitmap mIcon;
    private int mTextSize = 15;
    private int mTextColor = Color.BLACK;
    private int mBgColor = Color.TRANSPARENT;
    public int marginTop = 5;
    public int marginBottom = 5;
    public int marginRight = 5;
    public int marginLeft = 5;

    private View mView;

    public String getTitle() {
        return mTitle;
    }

    public Bitmap getIcon() {
        return mIcon;
    }

    public int getTextSize() {
        return mTextSize;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public int getBgColor() {
        return mBgColor;
    }

    public View getView() {
        return mView;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setIcon(Bitmap icon) {
        mIcon = icon;
    }

    public void setIcon(Context context, int res) {
        mIcon = BitmapFactory.decodeResource(context.getResources(), res);
    }


    public void setTextSize(int textSize) {
        mTextSize = textSize;
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
    }

    public void setBgColor(int bgColor) {
        mBgColor = bgColor;
    }

    public void setView(View view) {
        mView = view;
    }
}