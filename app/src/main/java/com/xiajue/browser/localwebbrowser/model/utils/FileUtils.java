package com.xiajue.browser.localwebbrowser.model.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * Created by XiaJue on 2017/8/7.
 */

public class FileUtils {
    private static final int SUCCESS = 0;
    private static final int FAILURE = 1;
    private static final int EXIST = 2;

    public static int copyFile(File file, String dir, File[] outFile) {
        L.e("------------------" + file.getAbsolutePath());
        if (file.exists()) {
            outFile[0] = new File(dir, changeSuff(file.getName(), ".jpg"));
            return toCopy(file, outFile[0]);
        }
        return FAILURE;
    }

    public static int copyFile(File file, String dir, String name,File[] outFile) {
        if (file.exists()) {
            outFile[0] = new File(dir, changeSuff(name, ".jpg"));
            return toCopy(file, outFile[0]);
        }
        return FAILURE;
    }

    public static int toCopy(File srcFile, File destFile) {

        // 判断源文件是否存在
        if (!srcFile.exists()) {
            return FAILURE;
        } else if (!srcFile.isFile()) {
            return FAILURE;
        }

        // 判断目标文件是否存在
        if (destFile.exists()) {
            // 如果目标文件存在并允许覆盖
            return EXIST;
        } else {
            // 如果目标文件所在目录不存在，则创建目录
            if (!destFile.getParentFile().exists()) {
                // 目标文件所在目录不存在
                if (!destFile.getParentFile().mkdirs()) {
                    // 复制文件失败：创建目标文件所在目录失败
                    return FAILURE;
                }
            }
        }

        // 复制文件

        try {
            FileInputStream fi = new FileInputStream(srcFile);
            FileOutputStream fo = new FileOutputStream(destFile);

            FileChannel in = fi.getChannel();

            FileChannel out = fo.getChannel();

            in.transferTo(0, in.size(), out);

            fi.close();
            fo.close();
            in.close();
            out.close();

            return SUCCESS;
        } catch (Exception e) {
            return FAILURE;
        }
    }

    /**
     * 更改字符串的后缀
     *
     * @param name
     * @param suff
     * @return
     */
    public static String changeSuff(String name, String suff) {
        int index = name.indexOf('.');
        L.e("index=" + index);
        return index != -1 ? name.substring(0, index) : name + suff;
    }
}
