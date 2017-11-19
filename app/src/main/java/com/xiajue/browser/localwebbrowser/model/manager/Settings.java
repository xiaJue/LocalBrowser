package com.xiajue.browser.localwebbrowser.model.manager;

import android.content.Context;

import com.xiajue.browser.localwebbrowser.model.Config;
import com.xiajue.browser.localwebbrowser.model.utils.L;
import com.xiajue.browser.localwebbrowser.model.utils.SPUtils;

import java.io.File;

/**
 * xiaJue 2017/9/22创建
 */
public class Settings {
    public static String START_KEY = "web_start_type";
    public static String LAST_URL = "last_url";
    public static String IN_URL = "in_url";
    public static String SHOW_HOME_IMAGE = "show_home_image";
    public static String DON_LOAD_PATH = "don_load_path";
    public static String SLIDE_TAG = "web_dark";
    public static String FILE_SAVE_PATH = "file_save_path";
    public static String IMAGE_SAVE_PATH = "image_save_path";
    public static final int SETTINGS_RESULT = 251;

    /**
     * 设置--启动--项的选择
     */
    public static void putStartSettingsType(Context context, int type) {
        SPUtils.getInstance(context).put(START_KEY, type);
    }

    /**
     * 查询--启动--项的选择情况
     */
    public static int getStartSettingsType(Context context) {
        return SPUtils.getInstance(context).getInt(START_KEY, 0);
    }

    /**
     * 查询一个设置的数据
     */
    public static boolean getSettingsBoolean(Context context, String key, boolean... bs) {
        if (bs != null && bs.length > 0) {
            return SPUtils.getInstance(context).getBoolean(key, bs[0]);
        }
        return SPUtils.getInstance(context).getBoolean(key);
    }

    /**
     * 查询一个设置的数据
     */
    public static String getSettingsString(Context context, String key) {
        return SPUtils.getInstance(context).getString(key);
    }

    /**
     * 存储一条设置的数据
     */
    public static void putSettingsBoolean(Context context, String key, Boolean value) {
        SPUtils.getInstance(context).put(key, value);
    }

    /**
     * 存储一条设置的数据
     */
    public static void putSettingsString(Context context, String key, String value) {
        SPUtils.getInstance(context).put(key, value);
    }

    /**
     * 获得文件保存路径
     */
    public static String getFileSavePath(Context context, String name, String end) {
        String path = SPUtils.getInstance(context).getString(FILE_SAVE_PATH, Config.WEB_SAVE_PATH);
        L.e("path=" + path);
        File file = new File(path);
        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();//创建目录
            L.e(" mkdirs..." + mkdirs);
        }
        if (name.isEmpty() && end.isEmpty()) {
            return path;
        }
        return path + File.separator + name + end;
    }

    /**
     * 设置文件保存路径
     */
    public static void setFileSavePath(Context context, String path) {
        SPUtils.getInstance(context).put(FILE_SAVE_PATH, path);
    }

    public static String getImageSavePath(Context context, String name, String end) {
        String path = SPUtils.getInstance(context).getString(IMAGE_SAVE_PATH, Config
                .WEB_SAVE_PATH);
        L.e("path=" + path);
        File file = new File(path);
        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();//创建目录
            L.e(" mkdirs..." + mkdirs);
        }
        if (name.isEmpty() && end.isEmpty()) {
            return path;
        }
        return path + File.separator + name + end;
    }

    public static void createDir(Context context) {
        L.e("createDir");
        getFileSavePath(context, "", "");
        getImageSavePath(context, "", "");
    }

    public static void setImagePathEditText(Context context, String path) {
        SPUtils.getInstance(context).put(IMAGE_SAVE_PATH, path);
    }
}
