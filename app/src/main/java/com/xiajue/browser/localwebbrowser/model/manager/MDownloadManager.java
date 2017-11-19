package com.xiajue.browser.localwebbrowser.model.manager;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.xiajue.browser.localwebbrowser.R;
import com.xiajue.browser.localwebbrowser.model.utils.L;
import com.xiajue.browser.localwebbrowser.model.utils.OpenFileUtils;
import com.xiajue.browser.localwebbrowser.model.utils.StringUtils;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.woblog.android.downloader.DownloadService;
import cn.woblog.android.downloader.callback.DownloadListener;
import cn.woblog.android.downloader.callback.DownloadManager;
import cn.woblog.android.downloader.config.Config;
import cn.woblog.android.downloader.domain.DownloadInfo;
import cn.woblog.android.downloader.exception.DownloadException;

/**
 * xiaJue 2017/9/16创建
 */
public class MDownloadManager {

    public static MDownloadManager manager;

    public static MDownloadManager getInstance(Context context) {
        if (manager == null) {
            synchronized (MDownloadManager.class) {
                if (manager == null) {
                    manager = new MDownloadManager(context);
                }
            }
        }
        return manager;
    }

    private Context mContext;
    private DownloadManager mDownloadManager;
    private MJNotificationManager mNotificationManager;//底部按钮

    public MDownloadManager(Context context) {
        this.mContext = context;
        Config config = new Config();
        //set database path.
        config.setDatabaseName(Settings.getFileSavePath(context, "d", ".db"));
        //set download quantity at the same time.
        config.setDownloadThread(3);
        //set each download info thread number
        config.setEachDownloadThread(2);
        // set connect timeout,unit millisecond
        config.setConnectTimeout(10000);
        // set read data timeout,unit millisecond
        config.setReadTimeout(10000);
        mDownloadManager = DownloadService
                .getDownloadManager(context, config);
        mNotificationManager = new MJNotificationManager(context);
    }

    private boolean isNotify = true;

    public MDownloadManager setNotify(boolean notify) {
        isNotify = notify;
        return manager;
    }

    public void downloadFile(String url, String path) {
        //create download info set download uri and save path.
        final DownloadInfo downloadInfo = new DownloadInfo.Builder().setUrl(url).setPath(path)
                .build();
        downloadInfo.setDownloadListener(new MDownloadListener(mContext, path));
        mDownloadManager.download(downloadInfo);
    }

    public void downloadFile(String url, String path, int[] toastRes) {
        //create download info set download uri and save path.
        final DownloadInfo downloadInfo = new DownloadInfo.Builder().setUrl(url).setPath(path)
                .build();
        downloadInfo.setDownloadListener(new MDownloadListener(mContext, path, toastRes));
        mDownloadManager.download(downloadInfo);
    }

    class MDownloadListener implements DownloadListener {
        private Context mContext;
        private String mPath;
        private int[] mToastRes;

        public MDownloadListener(Context context, String path) {
            mContext = context;
            mPath = path;
        }

        public MDownloadListener(Context context, String path, int[] toastRes) {
            mContext = context;
            mPath = path;
            mToastRes = toastRes;
        }

        @Override
        public void onStart() {
            L.e("download start...");
            if (mToastRes == null) {
                Toast.makeText(mContext, R.string.start_download,
                        Toast.LENGTH_SHORT).show();
            }
            if (isNotify) {
                mNotificationManager.initDownNotification(StringUtils.getName(mPath));
            }
        }

        @Override
        public void onDownloading(long progress, long size) {
            L.e("download ing ...progress=" + progress + "% /" + size);
            if (isNotify) {
                mNotificationManager.updateDownNotification(R.string.download_ing, (int) size, (int)
                                progress,
                        null);
            }
        }

        @Override
        public void onDownloadSuccess() {
            Toast.makeText(mContext, mToastRes == null ? R.string.download_success :
                    mToastRes[1], Toast.LENGTH_SHORT).show();
            //设置状态栏点击
            //TODO
            //在这里处理任务完成的状态
            if (isNotify) {
                File file = new File(mPath);
                Intent fileIntent = OpenFileUtils.getFileIntent(mContext, file);
                if (fileIntent != null) {
                    PendingIntent intentPend = PendingIntent.getActivity(mContext, 0,
                            fileIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                    mNotificationManager.updateDownNotification(R.string.download_success, 100, 100,
                            intentPend);
                }
            }
        }

        @Override
        public void onDownloadFailed(DownloadException e) {
            L.e("download Failed");
            Toast.makeText(mContext, mToastRes == null ? R.string.download_failure : mToastRes[2],
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onWaited() {

        }

        @Override
        public void onPaused() {

        }

        @Override
        public void onRemoved() {

        }
    }

    public static void getReallyFileName(final String url, final OnGetUrlFileName
            onGetUrlFileName) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String filename = "";
                URL myURL;
                HttpURLConnection conn = null;
                if (url == null || url.length() < 1) {
                    return null;
                }
                try {
                    myURL = new URL(url);
                    conn = (HttpURLConnection) myURL.openConnection();
                    conn.connect();
                    conn.getResponseCode();
                    URL absUrl = conn.getURL();// 获得真实Url
                    filename = conn.getHeaderField("Content-Disposition");//
                    // 通过Content-Disposition获取文件名，这点跟服务器有关，需要灵活变通
                    if (filename == null || filename.length() < 1) {
                        filename = absUrl.getFile();
                    }
                } catch (Exception e) {
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
                return filename;
            }

            @Override
            protected void onPostExecute(String s) {
                String name = s;
                name = StringUtils.getName(s);
                onGetUrlFileName.onGetUrlFileName(name);
            }
        }.execute();
    }

    public static void getUrlResponseCode(final String url, final OnGetUrlCode
            onGetUrlCode) {
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... params) {
                int code = 400;
                URL myURL;
                HttpURLConnection conn = null;
                if (url == null || url.length() < 1) {
                    return null;
                }
                try {
                    myURL = new URL(url);
                    conn = (HttpURLConnection) myURL.openConnection();
                    conn.connect();
                    code = conn.getResponseCode();
                } catch (Exception e) {
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
                return code;
            }

            @Override
            protected void onPostExecute(Integer s) {
                onGetUrlCode.onGetUrlCode(s);
            }
        }.execute();
    }

    public interface OnGetUrlFileName {
        void onGetUrlFileName(String name);
    }

    public interface OnGetUrlCode {
        void onGetUrlCode(int code);
    }
}
