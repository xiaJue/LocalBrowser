package com.xiajue.browser.localwebbrowser.model.manager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.xiajue.browser.localwebbrowser.R;

/**
 * xiaJue 2017/9/16创建
 */
public class DialogManager {

    public void showInquiry(Context context, String title, DialogInterface.OnClickListener
            okListener) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title);
        dialog.setPositiveButton(context.getString(R.string.ok), okListener);
        dialog.setNegativeButton(context.getString(R.string.cancel), null);
        dialog.show();
    }
}
