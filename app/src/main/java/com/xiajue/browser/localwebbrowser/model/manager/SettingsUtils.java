package com.xiajue.browser.localwebbrowser.model.manager;

import android.content.Context;

import static com.xiajue.browser.localwebbrowser.model.manager.Settings.LAST_URL;

/**
 * Created by Moing_Admin on 2017/10/13.
 */

public class SettingsUtils {
    /**
     * 设置 禁止加载多个目录
     */
    public static boolean isDonLoad(Context context, boolean b) {
        return Settings.getSettingsBoolean(context, Settings
                .DON_LOAD_PATH, true);
    }

    /**
     * 设置"IN_URL"
     */
    public static void setInUrl(Context context, String url) {
        Settings.putSettingsString(context, Settings.IN_URL, url);
    }

    public static String getInUrl(Context context) {
        return Settings.getSettingsString(context, Settings.IN_URL);
    }

    /**
     * 设置是否显示首页图片
     */
    public static void setShowHomeImage(Context context, boolean b) {
        Settings.putSettingsBoolean(context, Settings.SHOW_HOME_IMAGE, b);
    }

    public static boolean isShowHomeImage(Context context, boolean b) {
        return Settings.getSettingsBoolean(context, Settings
                .SHOW_HOME_IMAGE, b);
    }

    /**
     * 设置是否滑动tag
     */
    public static void setSlideTag(Context context, boolean b) {
        Settings.putSettingsBoolean(context, Settings.SLIDE_TAG, b);
    }

    public static boolean isSlideTag(Context context, boolean b) {
        return Settings.getSettingsBoolean(context, Settings
                .SLIDE_TAG, true);
    }

    /**
     * 设置 "LAST_URL"
     */
    public static void setLastUrl(Context context, String lastUrl) {
        Settings.putSettingsString(context, Settings.LAST_URL,
                lastUrl);
    }

    public static String getLastUrl(Context context, String lastUrl) {
        return Settings.getSettingsString(context, LAST_URL);
    }
}
