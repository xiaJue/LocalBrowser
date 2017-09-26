package com.xiajue.browser.localwebbrowser.presenter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.xiajue.browser.localwebbrowser.R;
import com.xiajue.browser.localwebbrowser.model.bean.BingBean;
import com.xiajue.browser.localwebbrowser.model.bean.BingBeanList;
import com.xiajue.browser.localwebbrowser.model.bean.MeiziBean;
import com.xiajue.browser.localwebbrowser.model.bean.MeiziBeanList;
import com.xiajue.browser.localwebbrowser.model.internet.RetrofitUtils;
import com.xiajue.browser.localwebbrowser.model.manager.ImageLoaderManager;
import com.xiajue.browser.localwebbrowser.model.manager.SettingsManager;
import com.xiajue.browser.localwebbrowser.model.utils.KeyBoardUtils;
import com.xiajue.browser.localwebbrowser.model.utils.L;
import com.xiajue.browser.localwebbrowser.model.utils.SPUtils;
import com.xiajue.browser.localwebbrowser.model.utils.ScreenUtils;
import com.xiajue.browser.localwebbrowser.view.activity.HomeActivity;
import com.xiajue.browser.localwebbrowser.view.activity.ImageActivity;
import com.xiajue.browser.localwebbrowser.view.activity.SettingsActivity;
import com.xiajue.browser.localwebbrowser.view.activity.frametag.HomeFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * xiaJue 2017/9/21创建
 */
public class HomeFragmentPresenter {
    private HomeFragment mHomeFragment;
    private RetrofitUtils mRetrofitUtils;
    private String mMeiziUrl;
    private String mBingUrl;
    private boolean mMeiziLoadFlag;
    private boolean mBingLoadFlag;
    private String mBingTitle;

    public HomeFragmentPresenter(HomeFragment homeFragment) {
        mHomeFragment = homeFragment;
        mRetrofitUtils = new RetrofitUtils();
    }

    public void onButtonClick(View view, String text) {
        if (!text.substring(0, 7).equals("http://") && !text.substring(0, 8).equals
                ("https://")) {
            text = "http://" + text;
        }
        HomeActivity activity = (HomeActivity) mHomeFragment.getActivity();
        activity.mWebFragment.getWebView().loadUrl(text);
        L.e("---t" + text);
        L.e("---wf" + activity.mWebFragment);
        L.e("---wv" + activity.mWebFragment.getWebView());
        activity.getViewPager().setCurrentItem(1);
        KeyBoardUtils.closeKeybord(mHomeFragment.mSearchEditText.getEditText(), mHomeFragment
                .getContext());
    }

    public void setImageUrl() {
        /**
         * loading meizi ...
         */
        mRetrofitUtils.getMeiziBean(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                List<MeiziBean> results = ((MeiziBeanList) response.body()).getResults();
                MeiziBean meiziBean = results.get(0);
                mMeiziUrl = meiziBean.getUrl();
                onLoadResponse(meiziBean.getUrl(), "meiziUrl", mHomeFragment.mMeiziIv, new
                        SimpleImageLoadingListener() {
                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap
                                    loadedImage) {
                                super.onLoadingComplete(imageUri, view, loadedImage);
                                //set imageView and size
                                int height = loadedImage.getHeight();
                                int imgHeight = mHomeFragment.mMeiziRl.getLayoutParams()
                                        .height;
                                int imgWidth = (int) (loadedImage.getWidth() * ((float)
                                        imgHeight) / height);
                                //防止图片宽度过大
                                int maxWidth = ScreenUtils.getScreenWidth(mHomeFragment
                                        .getContext()) / 7 * 4;//七分之四
                                if (imgWidth > maxWidth) {
                                    imgWidth = maxWidth;
                                }
                                mHomeFragment.mMeiziIv.getLayoutParams().width = imgWidth;
                                mHomeFragment.mMeiziIv.getLayoutParams().height = imgHeight;
                                mHomeFragment.mMeiziRl.getLayoutParams().width = imgWidth;
                                mHomeFragment.mMeiziIv.setImageBitmap(loadedImage);
                                FadeInBitmapDisplayer.animate(mHomeFragment.mMeiziIv,
                                        800);
                                mMeiziLoadFlag = true;
                                if (mMeiziLoadFlag && mBingLoadFlag) {
                                    mHomeFragment.mRefreshLayout.setRefreshing(false);
                                }
                            }
                        });
                mHomeFragment.mMeiziTv.setText(mHomeFragment.getString(R.string.meizi_text));

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                L.e("failure--meizi");
                mHomeFragment.mRefreshLayout.setRefreshing(false);
            }
        });
        /**
         * loading bing...
         */
        mRetrofitUtils.getBingBean(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                L.e("success");
                List<BingBean> images = ((BingBeanList) response.body()).getImages();
                BingBean bingBean = images.get(0);
                mBingUrl = bingBean.getAbsUrl();
                mBingTitle = bingBean.getCopyright();
                onLoadResponse(bingBean.getAbsUrl(), "bingUrl", mHomeFragment.mBingIv, new
                        SimpleImageLoadingListener() {
                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap
                                    loadedImage) {
                                super.onLoadingComplete(imageUri, view, loadedImage);
                                mHomeFragment.mBingIv.setImageBitmap(loadedImage);
                                FadeInBitmapDisplayer.animate(mHomeFragment.mBingIv, 800);
                                mBingLoadFlag = true;
                                if (mMeiziLoadFlag && mBingLoadFlag) {
                                    mHomeFragment.mRefreshLayout.setRefreshing(false);
                                }
                            }
                        });
                mHomeFragment.mBingTv.setText(bingBean.getCopyright() + bingBean.getEnddate());
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                L.e("failure--bing");
                mHomeFragment.mRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void onLoadResponse(String url, String key, ImageView imageView,
                                ImageLoadingListener loadListener) {
        String keyUrl = SPUtils.getInstance(mHomeFragment.getContext()).getString
                (key);
        if (!url.equals(keyUrl) && keyUrl.length() > 0) {
            //删除之前的缓存
            ImageLoader.getInstance().getDiskCache().get(key).delete();
        }
        SPUtils.getInstance(mHomeFragment.getContext()).put(key, mMeiziUrl);
        ImageLoaderManager.getInstance(mHomeFragment.getContext()).displayImage
                (url, imageView, loadListener);
    }

    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.home_home_bing_rl:
                //打开图片
                intent = new Intent(mHomeFragment.getContext(), ImageActivity.class);
                intent.putExtra("image_url", mBingUrl);
                intent.putExtra("type", mBingTitle);
                mHomeFragment.startActivity(intent);
                break;
            case R.id.home_home_meizi_rl:
                //打开图片
                intent = new Intent(mHomeFragment.getContext(), ImageActivity.class);
                intent.putExtra("image_url", mMeiziUrl);
                intent.putExtra("type", mHomeFragment.getString(R.string.meizi_text));
                mHomeFragment.startActivity(intent);
                break;
            case R.id.home_home_settings:
                //打开settings界面
                mHomeFragment.startActivityForResult(new Intent(mHomeFragment.getContext(),
                        SettingsActivity.class), SettingsManager.SETTINGS_RESULT);
                break;
        }
    }

    public void onButtonClose() {
        //强制关闭软键盘
        KeyBoardUtils.closeKeybord(mHomeFragment.mSearchEditText.getEditText(), mHomeFragment
                .getContext());
    }
}
