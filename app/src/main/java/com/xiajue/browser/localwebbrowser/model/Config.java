package com.xiajue.browser.localwebbrowser.model;

import android.os.Environment;

import java.io.File;

/**
 * xiaJue 2017/9/15创建
 */
public class Config {
//    public static String[] EXTENSION_STRING = {".html", ".htm", ".mht"};
    public static String WEB_SAVE_PATH = Environment.getExternalStorageDirectory() + File
            .separator + "webFile" + File.separator;
    public static final String IMAGE_DOWNLOAD_ROOT_URL = "http://iqnpud.iqdcdn.com/";
    public static final String BING_BASE_URL = "http://www.bing.com/";
    public static final String BING_IMAGE_JSON_URL = "HPImageArchive.aspx?format=js";
    public static final String MEIZI_IMAGE_JSON_URL = "data/%E7%A6%8F%E5%88%A9/{size}/{pages}";
    public static String MEIZI_IMAGE_BASE_URL = "http://gank.io/api/";
    public static String DOWNLOAD_IMAGE_DIR = Environment.getExternalStorageDirectory() + File
            .separator + "localWeb" + File.separator;
    public static String OBJECT_GIT_URL = "http://www.github.com/xiaJue/";
    public static long CLICK_TO_BACK_SLEEP_LENGTH = 1500;//再按一次退出应用的间隔时间
}
