package com.xiajue.browser.localwebbrowser.view.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Moing_Admin on 2017/11/9.
 */

public class ExtendedToolbar extends Toolbar {
    public ExtendedToolbar(Context context) {
        this(context, null);
    }

    public ExtendedToolbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExtendedToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    private TextView mTitleTextView;

    public void setTitleTextView(TextView titleTextView) {
        mTitleTextView = titleTextView;
        titleTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), mTitleTextView.getText().toString(), Toast
                        .LENGTH_SHORT).show();
            }
        });
    }

}
