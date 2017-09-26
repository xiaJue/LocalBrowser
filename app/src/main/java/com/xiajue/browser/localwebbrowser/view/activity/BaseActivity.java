package com.xiajue.browser.localwebbrowser.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * xiaJue 2017/9/19创建
 */
public class BaseActivity extends AppCompatActivity {
    public <T extends View> T getView(int id) {
        return (T) findViewById(id);
    }
}
