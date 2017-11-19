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
        String name = new File(url).getName();
        // 如果出现'='则认为是这种格式并只截取=后面的字符:xxx="fileName.xx",除非是这种情况:fileName.xx=
        int last = name.lastIndexOf('=');
        if (last != -1 && last < url.length() - 1) {
            name = name.substring(last + 1, name.length());
        }
        name = lostIllChar(name);
        name = ellipsis(name, 20, getEnd(name));
        return name;
    }

    //如果出现在文件名中则跳过这些字符
    public static char[] FILE_ILLEGAL_CHARACTER = new char[]{'\\', '/', ':',
            '*', '?', '*', '<', '>', '|', '"', '\'', '='};

    private static String lostIllChar(String name) {
        for (int i = 0; i < FILE_ILLEGAL_CHARACTER.length; i++) {
            StringBuilder sb = new StringBuilder();
            int last = name.indexOf(FILE_ILLEGAL_CHARACTER[i]);
            while (last != -1) {
                if (last != 0) {
                    sb.append(name.substring(0, last));
                }
                sb.append(name.substring(last + 1, name.length()));
                name = sb.toString();
                sb = new StringBuilder();
                last = name.lastIndexOf(FILE_ILLEGAL_CHARACTER[i]);
            }
        }
        return name;
    }

    public static String getEnd(String url) {
        int last = url.lastIndexOf('.');
        if (last == -1) {
            return "";
        }
        return url.substring(last, url.length());
    }

    /**
     * 省略一段字符串
     *
     * @param text     string
     * @param length   length
     * @param ellipsis ellipsis string
     * @return
     */
    public static String ellipsis(String text, int length, String... ellipsis) {
        if (text.length() - 1 < length) {
            return text;
        }
        return text.substring(0, length) + (ellipsis != null && ellipsis.length > 0 ?
                ellipsis[0]
                : "...");
    }

    public static boolean isUrl(String text) {
        if (text == null || text.isEmpty() || (!text.substring(0, 7).equals("http://") &&
                !text.substring(0, 8).equals("https://") && (!text.substring(0, 7).equals
                ("file://")))) {
            return false;
        }
        return true;
    }


}
