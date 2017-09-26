package com.xiajue.browser.localwebbrowser.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.xiajue.browser.localwebbrowser.R;
import com.xiajue.browser.localwebbrowser.model.manager.SettingsManager;
import com.xiajue.browser.localwebbrowser.model.utils.KeyBoardUtils;
import com.xiajue.browser.localwebbrowser.view.activity.viewInterface.ISettingsView;

import ru.bartwell.exfilepicker.ExFilePicker;
import ru.bartwell.exfilepicker.data.ExFilePickerResult;

/**
 * xiaJue 2017/9/22创建
 */
public class SettingsPresenter {
    private ISettingsView mISettings;
    private Context mContext;
    private int File_EX_FILE_PICKER_RESULT = 2017;
    private int IMAGE_EX_FILE_PICKER_RESULT = 20178;

    public SettingsPresenter(ISettingsView iSettings) {
        mISettings = iSettings;
        mContext = (Context) iSettings;
    }

    public void onCheckedChanged(View view, boolean isChecked) {
        switch (view.getId()) {
            case R.id.settings_start_blank:
                if (isChecked)
                    SettingsManager.putStartSettingsType(mContext, 0);
                //blank
                break;
            case R.id.settings_start_last:
                //save last web url
                if (isChecked)
                    SettingsManager.putStartSettingsType(mContext, 1);
                break;
            case R.id.settings_start_in:
                if (isChecked) {
                    SettingsManager.putStartSettingsType(mContext, 2);
                }
                mISettings.getInEditText().setVisibility(isChecked ? View.VISIBLE : View.GONE);
                mISettings.getInSaveButton().setVisibility(isChecked ? View.VISIBLE : View
                        .GONE);
                break;
        }
    }

    public void onClickListener(View v) {
        switch (v.getId()) {
            case R.id.settings_start_button:
                String url = mISettings.getInEditText().getText
                        ().toString();
                if (!url.isEmpty()) {
                    //save in_url
                    if (!url.substring(0, 7).equals("http://") && !url.substring(0, 8).equals
                            ("https://")) {
                        url = "http://" + url;
                    }
                    SettingsManager.putSettingsString(mContext, SettingsManager.IN_URL, url);
                    //强制关闭软键盘
                    KeyBoardUtils.closeKeybord(mISettings.getInEditText(), mContext);
                } else {
                    Toast.makeText(mContext, R.string.is_null, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.settings_home_image_cb:
                SettingsManager.putSettingsBoolean(mContext, SettingsManager.SHOW_HOME_IMAGE,
                        mISettings.getActivity().mHomeImageCheckBox.isChecked() ? true : false);
                break;
            case R.id.settings_load_path_cb:
                SettingsManager.putSettingsBoolean(mContext, SettingsManager.DON_LOAD_PATH,
                        mISettings.getActivity().mLoadPathCheckBox.isChecked() ? true : false);
                break;
            case R.id.settings_slide_tag:
                SettingsManager.putSettingsBoolean(mContext, SettingsManager.SLIDE_TAG, mISettings
                        .getActivity().mSlideTagCheckBox.isChecked() ? true : false);
                break;
            case R.id.settings_file_path_button:
                //TODO open dir select
                openDirSelect(SettingsManager.getFileSavePath(mContext, "", ""),
                        File_EX_FILE_PICKER_RESULT);
                break;
            case R.id.settings_image_path_button:
                //TODO open dir select
                openDirSelect(SettingsManager.getImageSavePath(mContext, "", ""),
                        IMAGE_EX_FILE_PICKER_RESULT);
                break;
        }
    }

    /**
     * click-打开选择目录
     */
    public void openDirSelect(String startDir, int requestCode) {
        //打开一个目录选择器
        ExFilePicker exFilePicker = new ExFilePicker();
//        exFilePicker.setShowOnlyExtensions(Config.EXTENSION_STRING);
        exFilePicker.setCanChooseOnlyOneItem(true);
        exFilePicker.setChoiceType(ExFilePicker.ChoiceType.DIRECTORIES);
        exFilePicker.setQuitButtonEnabled(true);
        exFilePicker.setStartDirectory(startDir);
        exFilePicker.start((Activity) mContext, requestCode);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ExFilePickerResult result = ExFilePickerResult.getFromIntent(data);
        if (result == null) {
            return;
        }
        String name = result.getNames().get(0);
        String path = result.getPath() + name;
        if (requestCode == File_EX_FILE_PICKER_RESULT) {
            if (result != null && result.getCount() > 0) {
                mISettings.getActivity().mFilePathEditText.setText(path);
                SettingsManager.setFileSavePath(mContext, path);
            }
        } else if (requestCode == IMAGE_EX_FILE_PICKER_RESULT) {
            if (result != null && result.getCount() > 0) {
                mISettings.getActivity().mImagePathEditText.setText(path);
                SettingsManager.setImagePathEditText(mContext, path);
            }
        }
    }
}
