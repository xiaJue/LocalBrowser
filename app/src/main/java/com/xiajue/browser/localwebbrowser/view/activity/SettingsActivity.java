package com.xiajue.browser.localwebbrowser.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.xiajue.browser.localwebbrowser.R;
import com.xiajue.browser.localwebbrowser.model.manager.Settings;
import com.xiajue.browser.localwebbrowser.model.manager.SettingsUtils;
import com.xiajue.browser.localwebbrowser.model.utils.KeyBoardUtils;
import com.xiajue.browser.localwebbrowser.presenter.SettingsPresenter;
import com.xiajue.browser.localwebbrowser.view.activity.viewInterface.ISettingsView;

/**
 * xiaJue 2017/9/21创建
 */
public class SettingsActivity extends BaseActivity implements CompoundButton
        .OnCheckedChangeListener, ISettingsView, View.OnClickListener {
    private Toolbar mToolbar;
    private RadioButton mBlankButton;
    private RadioButton mLastButton;
    private RadioButton mInButton;
    private EditText mInEditText;
    private TextView mInSaveButton;

    public CheckBox mHomeImageCheckBox;
    public CheckBox mLoadPathCheckBox;
    public CheckBox mSlideTagCheckBox;

    public EditText mFilePathEditText;
    public TextView mFilePathButton;
    public EditText mImagePathEditText;
    public TextView mImagePathButton;

    private SettingsPresenter mPresenter;

    private void bindView() {
        mToolbar = getView(R.id.settings_toolbar);
        mBlankButton = getView(R.id.settings_start_blank);
        mLastButton = getView(R.id.settings_start_last);
        mInButton = getView(R.id.settings_start_in);
        mInEditText = getView(R.id.settings_start_edit);
        mInSaveButton = getView(R.id.settings_start_button);
        mHomeImageCheckBox = getView(R.id.settings_home_image_cb);
        mLoadPathCheckBox = getView(R.id.settings_load_path_cb);
        mSlideTagCheckBox = getView(R.id.settings_slide_tag);
        mFilePathEditText = getView(R.id.settings_file_path_edit);
        mFilePathButton = getView(R.id.settings_file_path_button);
        mImagePathEditText = getView(R.id.settings_image_path_edit);
        mImagePathButton = getView(R.id.settings_image_path_button);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_settings);
        super.onCreate(savedInstanceState);
        mPresenter = new SettingsPresenter(this);
        bindView();
        set();
    }

    @Override
    public Toolbar getToolbarToBaseActivity() {
        return getView(R.id.settings_toolbar);
    }

    private void set() {
        KeyBoardUtils.closeKeybord(mInEditText, this);//关闭软键盘
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //set start type
        int type = Settings.getStartSettingsType(this);
        switch (type) {
            case 0:
                mBlankButton.setChecked(true);
                break;
            case 1:
                mLastButton.setChecked(true);
                break;
            case 2:
                mInButton.setChecked(true);
                mInEditText.setVisibility(View.VISIBLE);
                mInSaveButton.setVisibility(View.VISIBLE);
                break;
        }
        //if start type==2 ,set editText text
        mInEditText.setText(SettingsUtils.getInUrl(this));
        mHomeImageCheckBox.setChecked(SettingsUtils.isShowHomeImage(this, true));
        mLoadPathCheckBox.setChecked(SettingsUtils.isDonLoad(this, true));
        mSlideTagCheckBox.setChecked(SettingsUtils.isSlideTag(this, true));
        //set file path and image path editText
        mFilePathEditText.setText(Settings.getFileSavePath(this, "", ""));
        mImagePathEditText.setText(Settings.getImageSavePath(this, "", ""));
        //set all view clickListener
        mBlankButton.setOnCheckedChangeListener(this);
        mLastButton.setOnCheckedChangeListener(this);
        mInButton.setOnCheckedChangeListener(this);
        mInSaveButton.setOnClickListener(this);
        mHomeImageCheckBox.setOnClickListener(this);
        mLoadPathCheckBox.setOnClickListener(this);
        mSlideTagCheckBox.setOnClickListener(this);
        mFilePathButton.setOnClickListener(this);
        mImagePathButton.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mPresenter.onCheckedChanged(buttonView, isChecked);
    }

    @Override
    public EditText getInEditText() {
        return mInEditText;
    }

    @Override
    public View getInSaveButton() {
        return mInSaveButton;
    }

    @Override
    public SettingsActivity getActivity() {
        return this;
    }

    @Override
    public void onClick(View v) {
        mPresenter.onClickListener(v);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }

    public void setFilePathEditText(String path) {
        mFilePathEditText.setText(path);
    }

    public void setImagePathEditText(String path) {
        mImagePathEditText.setText(path);
    }

    public CheckBox getHomeImageCheckBox() {
        return mHomeImageCheckBox;
    }

    public CheckBox getLoadPathCheckBox() {
        return mLoadPathCheckBox;
    }

    public CheckBox getSlideTagCheckBox() {
        return mSlideTagCheckBox;
    }
}
