package com.xiajue.browser.localwebbrowser.view.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.xiajue.browser.localwebbrowser.model.utils.ScreenUtils;

/**
 * xiaJue 2017/9/19创建
 */
public abstract class BaseActivity extends AppCompatActivity {
    public <T extends View> T getView(int id) {
        return (T) findViewById(id);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //如果版本高于4.4[API19]则将toolbar向下移动一个状态栏的高度
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int statusHeight = ScreenUtils.getStatusHeight(this);//状态栏高度
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) getToolbarToBaseActivity()
                    .getLayoutParams();
            lp.topMargin = statusHeight;
        }
    }

    public abstract Toolbar getToolbarToBaseActivity();
}
