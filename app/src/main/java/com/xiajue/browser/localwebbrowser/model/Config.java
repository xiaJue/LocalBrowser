package com.xiajue.browser.localwebbrowser.model;

import android.os.Environment;

import java.io.File;

/**
 * xiaJue 2017/9/15创建
 */
public class Config {
    //    public static String[] EXTENSION_STRING = {".html", ".htm", ".mht"};
//    public static final String WEBVIEW_LOAD_ERROR_HTML_PATH = "file:///android_asset/failure" +
//            ".html";//
    //必应图片的BaseUrl
    public static final String BING_BASE_URL = "http://www.bing.com/";
    //必应图片的url后缀
    public static final String BING_IMAGE_JSON_URL = "HPImageArchive.aspx?format=js";
    //妹子数据的参数
    public static final String MEIZI_IMAGE_JSON_URL = "data/%E7%A6%8F%E5%88%A9/{size}/{pages}";
    //妹子数据的BaseUrl
    public static String MEIZI_IMAGE_BASE_URL = "http://gank.io/api/";
    //保存图片的默认地址
    public static String DOWNLOAD_IMAGE_DIR = Environment.getExternalStorageDirectory() + File
            .separator + "localWeb" + File.separator;
    //保存离线网页的默认地址
    public static String WEB_SAVE_PATH = Environment.getExternalStorageDirectory() + File
            .separator + "webFile" + File.separator;
    //项目地址
    public static String OBJECT_GIT_URL = "https://github.com/xiaJue/LocalBrowser";
    //再按一次退出应用的间隔时间
    public static long CLICK_TO_BACK_SLEEP_LENGTH = 2000;
    //picture&bing下载地址
    public static String PICTURE_BING_APK_DOWNLOAD_ADDRESS = "https://github" +
            ".com/xiaJue/PictureBing/raw/master/1.2.apk";
    //picture&bing项目地址
    public static String PICTURE_BING_PROJECT_GIT_ADDRESS = "https://github" +
            ".com/xiaJue/PictureBing";
    //支付宝账号
    public static String ZHIFUBAO_NAME = "xiajueya@outlook.com";
    //历史记录的最大记录数
    public static int HISTORY_LIST_MAX_SIZE = 200;
    //模拟的历史记录listView最大显示条目
    public static int HISTORY_ITEM_SHOW_MAX_ITEM = 4;
}
