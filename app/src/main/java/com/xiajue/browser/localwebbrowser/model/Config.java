package com.xiajue.browser.localwebbrowser.model;

import android.content.Context;
import android.os.Environment;

import com.xiajue.browser.localwebbrowser.R;
import com.xiajue.browser.localwebbrowser.model.utils.ColorUtils;

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
    public static final String UNKNOWN = ".unknown";
    //妹子数据的BaseUrl
    public static String MEIZI_IMAGE_BASE_URL = "http://gank.io/api/";
    //保存离线网页的默认地址
    public static String WEB_SAVE_PATH = Environment.getExternalStorageDirectory() + File
            .separator + "localWeb" + File.separator;
    //项目地址
    public static String OBJECT_GIT_URL = "https://github.com/xiaJue/LocalBrowser";
    //再按一次退出应用的间隔时间
    public static long CLICK_TO_BACK_SLEEP_LENGTH = 2000;
    //picture&bing下载地址
    public static String PICTURE_BING_APK_DOWNLOAD_ADDRESS = "https://github" +
            ".com/xiaJue/PictureBing/raw/master/1.2.apk";
    //picture&bing项目地址
    public static String PICTURE_BING_PROJECT_GIT_ADDRESS = "https://github.com/xiaJue/PictureBing";
    //支付宝账号
    public static String ZHIFUBAO_NAME = "xiajueya@outlook.com";
    //历史记录的最大记录数/-1为不限
    public static int HISTORY_LIST_MAX_SIZE = 200;
    //历史记录listView最大高度=windowHeight*10/HISTORY_MAX_HEIGHT
    public static int HISTORY_MAX_HEIGHT = 7;
    //模拟历史记录listView条目的高度=maxHeight/HISTORY_ITEM_HEIGHT
    public static int HISTORY_ITEM_HEIGHT = 5;
    public static String BING_URL = "https://bing.ioliu.cn/";
    public static String GANKE_URL = "http://gank.io/";
    public static String WEB_ABOUT_BLANK = "about:blank";
    //是否设置侧滑菜单的背景为每日妹子图片
    public static boolean IS_SET_DRAWER_MENU_BGIMG = false;
    //全屏模式下手动显示状态栏后(不会自动隐藏)隐藏它的时间的间隔
    public static long HIDE_STATUS_TIME = 2000;

    private static String mSetWebViewBgColorString = "";

    /**
     * webView设置背景色为res中的colorWinBack的字符串
     */
    public static String getSetWebViewBgColorString(Context context) {
        if (mSetWebViewBgColorString.isEmpty()) {
            mSetWebViewBgColorString = "data:text/html,<body bgColor=\"" + getResColorWinBack
                    (context) +
                    "\"></body>";
        }
        return mSetWebViewBgColorString;
    }

    /**
     * 获得res中colorWinBack的16进制字符串
     */
    public static String getResColorWinBack(Context context) {
        int color = context.getResources().getColor(R.color.colorWinBack);
        return ColorUtils.toHexEncoding(color);
    }

    public static String[] COMMON_ADDRESS_TITLE = new String[]{"百度", "谷歌", "微博", "爱奇艺"};
    public static String[] COMMON_ADDRESS = new String[]{"http://www.baidu.com",
            "https://www.google.cn", "https://weibo.com/", "http://www.iqiyi.com/"};

    public static String VERSION_CHECK_ADDRESS = "https://raw.githubusercontent" +
            ".com/xiaJue/LocalBrowser/master/app/version.xml";
}
