package com.xiajue.browser.localwebbrowser.model.manager;

import android.app.PendingIntent;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.xiajue.browser.localwebbrowser.R;

/**
 * Created by Moing_Admin on 2017/10/18.
 */

public class NotificationManager {
    private Context mContext;
    private android.app.NotificationManager manager;

    public NotificationManager(Context context) {
        this.mContext = context;
        manager = (android.app.NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
    }

    private NotificationCompat.Builder mBuilder;
    private int DOWNLOAD_NOTIFICATION_ID = 557;

    public void pushDownloadNotification(String title, String msg, PendingIntent intentPend) {
        mBuilder = new NotificationCompat.Builder(mContext);
        mBuilder.setTicker(title);
        mBuilder.setContentText(msg);
        mBuilder.setSmallIcon(R.mipmap.download);
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setContentTitle(title);
        mBuilder.setAutoCancel(true);
        if (intentPend != null) {
            mBuilder.setContentIntent(intentPend);
        }
        manager.notify(DOWNLOAD_NOTIFICATION_ID, mBuilder.build());
    }

}
