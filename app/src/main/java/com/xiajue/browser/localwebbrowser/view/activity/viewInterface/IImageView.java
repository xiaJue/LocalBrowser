package com.xiajue.browser.localwebbrowser.view.activity.viewInterface;


import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

/**
 * xiaJue 2017/9/21创建
 */
public interface IImageView {
    Toolbar getToolbar();

    SubsamplingScaleImageView getImageView();

    TextView getTitleTextView();
}
