package com.xiajue.browser.localwebbrowser.view.activity.frametag;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.ClipboardManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.xiajue.browser.localwebbrowser.R;
import com.xiajue.browser.localwebbrowser.model.Config;

/**
 * xiaJue 2017/9/19创建
 */
public class AboutFragment extends BaseFramtag implements View.OnClickListener {
    private TextView mAddressTv;
    private TextView mPBProAddressTv;
    private TextView mPBApkDownloadTv;
    private TextView mDaShangTv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_fragment_about, container, false);
        bindView(view);
        set();
        return view;
    }

    private void bindView(View view) {
        mAddressTv = (TextView) view.findViewById(R.id.home_about_address);
        mPBProAddressTv = (TextView) view.findViewById(R.id.home_about_pd_pro_address);
        mPBApkDownloadTv = (TextView) view.findViewById(R.id.home_about_pd_apk_download);
        mDaShangTv = (TextView) view.findViewById(R.id.home_about_daShang);
    }

    private void set() {
        mAddressTv.setOnClickListener(this);
        mPBProAddressTv.setOnClickListener(this);
        mPBApkDownloadTv.setOnClickListener(this);
        mDaShangTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_about_address:
                getHomeActivity().getWebView().loadUrl(Config.OBJECT_GIT_URL);
                getHomeActivity().getViewPager().setCurrentItem(1);
                break;
            case R.id.home_about_pd_pro_address:
                //open web form picture&bing git address
                getHomeActivity().getWebView().loadUrl(Config.PICTURE_BING_PROJECT_GIT_ADDRESS);
                getHomeActivity().getViewPager().setCurrentItem(1);
                break;
            case R.id.home_about_pd_apk_download:
                //open web form picture&bing apk download address
                getHomeActivity().getWebView().loadUrl(Config.PICTURE_BING_APK_DOWNLOAD_ADDRESS);
                getHomeActivity().getViewPager().setCurrentItem(1);
                break;
            case R.id.home_about_daShang:
                ClipboardManager cm = (ClipboardManager) getContext().getSystemService(Context
                        .CLIPBOARD_SERVICE);
                cm.setText(Config.ZHIFUBAO_NAME);
                Toast.makeText(getContext(), getString(R.string.copy_success), Toast
                        .LENGTH_SHORT).show();
                break;
        }
    }

}
