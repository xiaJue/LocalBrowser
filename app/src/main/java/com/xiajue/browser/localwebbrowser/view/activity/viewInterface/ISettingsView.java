package com.xiajue.browser.localwebbrowser.view.activity.viewInterface;

import android.widget.Button;
import android.widget.EditText;

import com.xiajue.browser.localwebbrowser.view.activity.SettingsActivity;

/**
 * xiaJue 2017/9/22创建
 */
public interface ISettingsView {
    EditText getInEditText();
    Button getInSaveButton();
    SettingsActivity getActivity();
}
