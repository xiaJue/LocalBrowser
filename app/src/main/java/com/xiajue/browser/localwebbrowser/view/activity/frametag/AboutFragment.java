package com.xiajue.browser.localwebbrowser.view.activity.frametag;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.ClipboardManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xiajue.browser.localwebbrowser.R;
import com.xiajue.browser.localwebbrowser.model.Config;
import com.xiajue.browser.localwebbrowser.view.activity.ImageActivity;

import static com.xiajue.browser.localwebbrowser.model.manager.ApplicationManager.getVersionName;

/**
 * xiaJue 2017/9/19创建
 */
public class AboutFragment extends BaseFramtag implements View.OnClickListener {
    private TextView mAddressTv;
    private TextView mPBProAddressTv;
    private TextView mPBApkDownloadTv;
    private TextView mDaShangTv;
    private TextView mGankeTv;
    private TextView mBingTv;
    private TextView mVersion;
    private ImageView mWeiXinImg;
    private ImageView mZhiFuBaoImg;

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
        mGankeTv = (TextView) view.findViewById(R.id.home_about_ganke);
        mBingTv = (TextView) view.findViewById(R.id.home_about_bing);
        mVersion = (TextView) view.findViewById(R.id.home_about_version);
        mWeiXinImg = (ImageView) view.findViewById(R.id.weixin_img);
        mZhiFuBaoImg = (ImageView) view.findViewById(R.id.zhifubao_img);
    }

    private void set() {
        mAddressTv.setOnClickListener(this);
        mPBProAddressTv.setOnClickListener(this);
        mPBApkDownloadTv.setOnClickListener(this);
        mDaShangTv.setOnClickListener(this);
        mGankeTv.setOnClickListener(this);
        mBingTv.setOnClickListener(this);
        //获得应用版本号
        mVersion.setText(getString(R.string.version) + getVersionName(getContext()));

        mWeiXinImg.setOnClickListener(this);
        mZhiFuBaoImg.setOnClickListener(this);
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
            case R.id.home_about_ganke:
                getHomeActivity().getWebView().loadUrl(Config.GANKE_URL);
                getHomeActivity().getViewPager().setCurrentItem(1);
                break;
            case R.id.home_about_bing:
                getHomeActivity().getWebView().loadUrl(Config.BING_URL);
                getHomeActivity().getViewPager().setCurrentItem(1);
                break;
            case R.id.weixin_img:
                Intent intent = new Intent(getContext(), ImageActivity.class);
                intent.putExtra("image_res", R.mipmap.weixin);
                intent.putExtra("type", getString(R.string.weixin));
                intent.putExtra("name", getString(R.string.weixin_ds));
                startActivity(intent);
                break;
            case R.id.zhifubao_img:
                Intent intent2 = new Intent(getContext(), ImageActivity.class);
                intent2.putExtra("image_res", R.mipmap.zhifubao);
                intent2.putExtra("type", getString(R.string.zhifubao));
                intent2.putExtra("name", getString(R.string.zfb_ds));
                startActivity(intent2);
                break;
        }
    }
}
