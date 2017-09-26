package com.xiajue.browser.localwebbrowser.model.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * xiaJue 2017/9/16创建
 */
public class DownloadManager {
    private static OkHttpClient client = new OkHttpClient();

    /**
     * 下载文件
     */
    public static void download(final String savePath,final String url, final DownloadCallback callback) {
        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.failure();
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    InputStream is = response.body().byteStream();
                    FileOutputStream fileOut = null;
                    File file = new File(savePath);
                    fileOut = new FileOutputStream(file);
                    byte[] buf = new byte[1024 * 8];
                    int read;
                    while ((read = is.read(buf)) != -1) {
                        fileOut.write(buf, 0, read);
                    }
                    fileOut.flush();
                    fileOut.close();
                    is.close();
                    callback.success(file);
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.failure();
                }
            }
        });

    }

    /**
     * 获得文件名
     */
    public static String getFileName(String url) {
        String name=new File(url).getName();
        return name;
    }

    public interface DownloadCallback {
        void success(File file);

        void failure();
    }
}
