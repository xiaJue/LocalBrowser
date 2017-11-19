package com.xiajue.browser.localwebbrowser.model.manager;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import com.xiajue.browser.localwebbrowser.R;
import com.xiajue.browser.localwebbrowser.model.Config;
import com.xiajue.browser.localwebbrowser.model.internet.RetrofitUtils;
import com.xiajue.browser.localwebbrowser.model.utils.L;
import com.xiajue.browser.localwebbrowser.model.utils.NetUtils;
import com.xiajue.browser.localwebbrowser.model.utils.SPUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Moing_Admin on 2017/11/14.
 */

public class ApplicationManager {
    /**
     * 版本检查
     *
     * @param context
     */
    public static void checkVersionUpdate(final Context context) {
        if (NetUtils.isNetworkConnected(context)) {
            //检查是否有新版本
            RetrofitUtils.getContentFromInternet(Config.VERSION_CHECK_ADDRESS, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    L.e("不告诉你检查失败了...");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response == null || response.body() == null || response.code() != 200) {
                        return;
                    }
                    String versionString = response.body().string().trim();
                    if (versionString.isEmpty()) {
                        return;
                    }
                    final float version = Float.valueOf(versionString);
                    if (SPUtils.getInstance(context).getFloat("this_version_checkable", 0) ==
                            version) {
                        //该版本不检测
                        return;
                    }
                    float nowVersion = Float.valueOf(getVersionName(context));
                    if (version > nowVersion) {
                        L.e("有新版本了...");
                        update(version, (Activity) context);
                    }
                }
            });
        }
    }

    private static void update(final float version, final Activity context) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                /**
                 * UI线程-显示一个提醒更新的dialog
                 */
                DialogManager.showInquiry(context, context.getString(R.string
                                .version_update),
                        new String[]{context.getString(R.string.o_jb_k_la), context.getString(R
                                .string.not_bb_la)},
                        null, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SPUtils.getInstance(context).put("this_version_checkable",
                                        version);
                            }
                        }, true);
            }
        });
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = packInfo.versionName;
        return version;
    }

    /**
     * 发起写文件权限请求
     */
    public static void sendPermissioRequest(Activity activity) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(activity, new String[]{android
                    .Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }
}
