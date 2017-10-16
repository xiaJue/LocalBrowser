package com.xiajue.browser.localwebbrowser.model.utils;

import java.io.File;
import java.text.SimpleDateFormat;

/**
 * Created by xiaJue on 2017/10/10.
 */

public class StringUtils {
    //获得当前时间
    public static String formatDate(long missll) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        return formatter.format(missll);
    }

    /**
     * 获得文件格式字符串的文件名
     */
    public static String getName(String url) {
        return new File(url).getName();
    }

    public static String getEnd(String url) {
        int last = url.lastIndexOf('.');
        return url.substring(last, url.length());
    }
}
