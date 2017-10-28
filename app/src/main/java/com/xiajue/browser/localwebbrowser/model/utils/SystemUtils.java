package com.xiajue.browser.localwebbrowser.model.utils;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

/**
 * Created by Moing_Admin on 2017/8/18.
 */

public class SystemUtils {
    /**
     * 打开无线网络设置
     *
     * @param context
     */
    public static void openSystemInternetSettings(Context context) {
        //打开系统网络连接设置
        context.startActivity(new
                Intent(Settings.ACTION_WIRELESS_SETTINGS));//进入无线网络配置界面
    }

    /**
     * 打开wifi设置界面
     * @param context
     */
    public static void openWifiSettings(Context context) {
        context.startActivity(new
                Intent(Settings.ACTION_WIFI_SETTINGS));//进入无线网络配置界面
    }
}
