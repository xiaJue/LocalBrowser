package com.xiajue.browser.localwebbrowser.view.activity.viewInterface;

import android.view.View;
import android.widget.EditText;

import com.xiajue.browser.localwebbrowser.view.activity.SettingsActivity;

/**
 * xiaJue 2017/9/22创建
 */
public interface ISettingsView {
    EditText getInEditText();
    View getInSaveButton();
    SettingsActivity getActivity();
}
