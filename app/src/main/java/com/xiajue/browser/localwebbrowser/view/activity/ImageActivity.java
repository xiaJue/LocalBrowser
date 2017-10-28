package com.xiajue.browser.localwebbrowser.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.xiajue.browser.localwebbrowser.R;
import com.xiajue.browser.localwebbrowser.presenter.ImagePresenter;
import com.xiajue.browser.localwebbrowser.view.activity.viewInterface.IImageView;

/**
 * xiaJue 2017/9/20创建
 */
public class ImageActivity extends BaseActivity implements IImageView, View.OnClickListener {
    private SubsamplingScaleImageView mImageView;
    private Toolbar mToolbar;
    private TextView mTitleTextView;
    private ImagePresenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        mPresenter = new ImagePresenter(this);
        bindView();
        set();
    }

    private void set() {
        String url = getIntent().getStringExtra("image_url");
        String type = getIntent().getStringExtra("type");
        mPresenter.loadImage(url);

        mTitleTextView.setText(type);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mImageView.setOnClickListener(this);
    }

    private void bindView() {
        mToolbar = getView(R.id.image_toolbar);
        mImageView = getView(R.id.image_iv);
        mTitleTextView = getView(R.id.image_title_tv);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.image, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mPresenter.onMenuSelect(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter.shareTempFile != null && mPresenter.shareTempFile.exists()) {
            mPresenter.shareTempFile.delete();
        }
    }

    public void animationFinish() {
        finish();
        overridePendingTransition(R.anim.activity_enter_anim, R.anim.activity_exit_anim);
    }

    @Override
    public void onBackPressed() {
        animationFinish();
    }

    @Override
    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    public SubsamplingScaleImageView getImageView() {
        return mImageView;
    }

    @Override
    public TextView getTitleTextView() {
        return mTitleTextView;
    }

    @Override
    public void onClick(View v) {
        mPresenter.onClick(v);
    }
}
