package com.xiajue.browser.localwebbrowser.model.manager;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import com.xiajue.browser.localwebbrowser.R;

/**
 * Created by Moing_Admin on 2017/10/18.
 */

public class MJNotificationManager {
    private Notification.Builder mDownBuilder;
    private NotificationManager mManager;
    private Context mContext;
    private int DOWNLOAD_NOTIFY_ID = 57;

    public MJNotificationManager(Context context) {
        mContext = context;
        mManager = (NotificationManager) context.getApplicationContext().getSystemService(Context
                .NOTIFICATION_SERVICE);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void initDownNotification(String name) {
        mDownBuilder = new Notification.Builder(mContext);
        mDownBuilder.setContentTitle(name);
        mDownBuilder.setContentText(mContext.getString(R.string.start_download));
        mDownBuilder.setSmallIcon(android.R.drawable.stat_sys_download);
        mDownBuilder.setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap
                .icon));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            mDownBuilder.setProgress(100, 0, false);
        }
        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        Intent intent = new Intent();
        PendingIntent pi = PendingIntent.getActivity(mContext, 0, intent, flags);
        mDownBuilder.setContentIntent(pi);
        Notification notify = mDownBuilder.build();
        notify.flags = Notification.FLAG_ONGOING_EVENT;
        mManager.notify(DOWNLOAD_NOTIFY_ID, notify);
    }

    public void updateDownNotification(int id, int max, int progress, PendingIntent pendIntent) {
        String flagContent = mContext.getString(id);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            mDownBuilder.setProgress(max, progress, false);
        }
        mDownBuilder.setContentText(flagContent);
        Notification notify = mDownBuilder.build();
        notify.flags = Notification.FLAG_ONGOING_EVENT;
        if (pendIntent != null) {
            mDownBuilder.setContentTitle(mContext.getString(R.string.download_success));
            mDownBuilder.setContentIntent(pendIntent);
            notify = mDownBuilder.build();
            notify.flags = Notification.FLAG_AUTO_CANCEL;
        }
        mManager.notify(DOWNLOAD_NOTIFY_ID, notify);
    }
}
