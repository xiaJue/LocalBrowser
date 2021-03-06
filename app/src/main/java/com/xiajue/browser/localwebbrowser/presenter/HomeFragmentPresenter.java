package com.xiajue.browser.localwebbrowser.presenter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.xiajue.browser.localwebbrowser.R;
import com.xiajue.browser.localwebbrowser.model.Config;
import com.xiajue.browser.localwebbrowser.model.bean.BingBean;
import com.xiajue.browser.localwebbrowser.model.bean.BingBeanList;
import com.xiajue.browser.localwebbrowser.model.bean.CommonWebsiteBean;
import com.xiajue.browser.localwebbrowser.model.bean.HistoryBean;
import com.xiajue.browser.localwebbrowser.model.bean.MeiziBeanList;
import com.xiajue.browser.localwebbrowser.model.database.DatabaseDao;
import com.xiajue.browser.localwebbrowser.model.internet.RetrofitUtils;
import com.xiajue.browser.localwebbrowser.model.manager.ImageLoaderManager;
import com.xiajue.browser.localwebbrowser.model.manager.Settings;
import com.xiajue.browser.localwebbrowser.model.utils.KeyBoardUtils;
import com.xiajue.browser.localwebbrowser.model.utils.SPUtils;
import com.xiajue.browser.localwebbrowser.model.utils.ScreenUtils;
import com.xiajue.browser.localwebbrowser.model.utils.StringUtils;
import com.xiajue.browser.localwebbrowser.view.activity.ImageActivity;
import com.xiajue.browser.localwebbrowser.view.activity.SettingsActivity;
import com.xiajue.browser.localwebbrowser.view.activity.frametag.HomeFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.xiajue.browser.localwebbrowser.model.Config.HISTORY_LIST_MAX_SIZE;

/**
 * xiaJue 2017/9/21创建
 */
public class HomeFragmentPresenter {
    private HomeFragment mHomeFragment;
    private RetrofitUtils mRetrofitUtils;
    private String mMeiziUrl;
    private String mBingUrl;
    private boolean mMeiziLoadFlag;
    private boolean mMeiziLoadFailureFlag;
    private boolean mBingLoadFlag;
    private boolean mBingLoadFailureFlag;
    private String mBingTitle;
    public DatabaseDao mHistoryDao;

    public HomeFragmentPresenter(HomeFragment homeFragment) {
        mHomeFragment = homeFragment;
        mRetrofitUtils = new RetrofitUtils();
        mHistoryDao = DatabaseDao.getInstance(mHomeFragment.getContext());
    }

    public void onButtonClick(View view, String text) {
        try {
            if (!StringUtils.isUrl(text)) {
                text = "http://" + text;
            }
        } catch (Exception e) {
            Toast.makeText(mHomeFragment.getContext(), R.string.address_error, Toast
                    .LENGTH_SHORT).show();
            return;
        }
        mHomeFragment.getHomeActivity().getWebView().loadUrl(text);
        mHomeFragment.getHomeActivity().getViewPager().setCurrentItem(1);
        KeyBoardUtils.closeKeybord(mHomeFragment.mSearchEditText.getEditText(), mHomeFragment
                .getContext());
    }

    private boolean isNotInternet;//是否没有网络

    /**
     * 加载图片
     */
    public void setImageUrl() {
        //loading meizi ...
        mRetrofitUtils.getMeiziBean(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                mMeiziUrl = ((MeiziBeanList) response.body()).getResults().get(0).getUrl();
                isNotInternet = false;
                getUrlAndLoad(mMeiziUrl, "meiziUrl", mHomeFragment.mMeiziIv, mHomeFragment.getString
                        (R.string.load_meizi_image_error));
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                isNotInternet = true;
                getUrlAndLoad(mMeiziUrl, "meiziUrl", mHomeFragment.mMeiziIv, mHomeFragment
                        .getString(R.string.load_meizi_image_error));
                mMeiziLoadFailureFlag = true;
                showNetError();
                mHomeFragment.mRefreshLayout.setRefreshing(false);
            }
        });
        //loading bing...
        mRetrofitUtils.getBingBean(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                List<BingBean> images = ((BingBeanList) response.body()).getImages();
                BingBean bingBean = images.get(0);
                mBingUrl = bingBean.getAbsUrl();
                mBingTitle = bingBean.getCopyright();
                isNotInternet = false;
                getUrlAndLoad(mBingUrl, "bingUrl", mHomeFragment.mBingIv, mHomeFragment
                        .getString(R.string.load_bing_image_error));
                mHomeFragment.mBingTv.setText(bingBean.getCopyright() + bingBean.getEnddate());
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                isNotInternet = true;
                mBingLoadFailureFlag = true;
                getUrlAndLoad(mBingUrl, "bingUrl", mHomeFragment.mBingIv, mHomeFragment.getString
                        (R.string.load_bing_image_error));
                showNetError();
                mHomeFragment.mRefreshLayout.setRefreshing(false);
            }
        });
    }

    /**
     * 显示网络出错
     */
    private void showNetError() {
        if (mMeiziLoadFailureFlag && mBingLoadFailureFlag) {
            Toast.makeText(mHomeFragment.getContext(), mHomeFragment.getString(R.string
                    .internet_error_dialog_title), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 抽取相似代码以复用
     *
     * @param key       url存储于SharedPreferences 中的key
     * @param imageView 对应的imageView
     */
    private void getUrlAndLoad(String url, String key, final ImageView imageView, final String
            errorToast) {
        final boolean isLoadMeizi = key.contains("meizi");
        onLoadResponse(url, key, imageView, new
                SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason
                            failReason) {
                        super.onLoadingFailed(imageUri, view, failReason);
                        mHomeFragment.mRefreshLayout.setRefreshing(false);
                        Toast.makeText(mHomeFragment.getContext(), errorToast, Toast
                                .LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap
                            loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);
                        if (isLoadMeizi) {
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
                            imageView.getLayoutParams().width = imgWidth;
                            imageView.getLayoutParams().height = imgHeight;
                            mHomeFragment.mMeiziRl.getLayoutParams().width = imgWidth;
                            //设置侧滑菜单背景
                            if (Config.IS_SET_DRAWER_MENU_BGIMG) {
                                mHomeFragment.getHomeActivity().getActivity().setDrawerBackground
                                        (loadedImage);
                            }
                            //设置文字
                            mHomeFragment.mMeiziTv.setText(mHomeFragment.getString(R.string
                                    .meizi_text));
                            mMeiziLoadFlag = true;
                        } else {
                            mBingLoadFlag = true;
                        }
                        imageView.setImageBitmap(loadedImage);
                        //显示图片渐变动画
                        FadeInBitmapDisplayer.animate(imageView, 800);
                        if (mMeiziLoadFlag && mBingLoadFlag) {
                            mHomeFragment.mRefreshLayout.setRefreshing(false);
                        }
                    }
                });
    }

    /**
     * 抽取相似代码以复用
     *
     * @param url          图片url
     * @param key          url存储于SharedPreferences 中的key
     * @param imageView    对应的imageView
     * @param loadListener 加载图片的回调
     */
    private void onLoadResponse(String url, String key, ImageView imageView,
                                ImageLoadingListener loadListener) {
        String keyUrl = SPUtils.getInstance(mHomeFragment.getContext()).getString
                (key);
        if (!isNotInternet) {
            if (!url.equals(keyUrl) && keyUrl.length() > 0) {
                //删除之前的缓存
                ImageLoader.getInstance().getDiskCache().get(key).delete();
            }
        } else {
            if (keyUrl.isEmpty()) {
                return;
            }
            url = keyUrl;
            if (key.contains("meizi")) {
                mMeiziUrl = keyUrl;
            } else {
                mBingUrl = keyUrl;
            }
        }
        SPUtils.getInstance(mHomeFragment.getContext()).put(key, url);
        ImageLoaderManager.getInstance(mHomeFragment.getContext()).displayImage
                (url, imageView, loadListener);
    }

    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.home_home_bing_rl:
                //打开图片
                if (mBingUrl == null || mBingUrl.isEmpty()) {
                    return;
                }
                intent = new Intent(mHomeFragment.getContext(), ImageActivity.class);
                intent.putExtra("image_url", mBingUrl);
                intent.putExtra("type", mBingTitle);
                mHomeFragment.startActivity(intent);
                mHomeFragment.getActivity().overridePendingTransition(R.anim.activity_enter_anim,
                        R.anim.activity_exit_anim);
                break;
            case R.id.home_home_meizi_rl:
                //打开图片
                if (mMeiziUrl == null || mMeiziUrl.isEmpty()) {
                    return;
                }
                intent = new Intent(mHomeFragment.getContext(), ImageActivity.class);
                intent.putExtra("image_url", mMeiziUrl);
                intent.putExtra("type", mHomeFragment.getString(R.string.meizi_text));
                mHomeFragment.startActivity(intent);
                mHomeFragment.getActivity().overridePendingTransition(R.anim.activity_enter_anim,
                        R.anim.activity_exit_anim);

                break;
            case R.id.home_home_settings:
                //打开settings界面
                mHomeFragment.startActivityForResult(new Intent(mHomeFragment.getContext(),
                        SettingsActivity.class), Settings.SETTINGS_RESULT);
                break;
            case R.id.home_home_clear_History:
                mHistoryDao.deleteAllHistory();
                mHomeFragment.mList.clear();
                mHomeFragment.mHistoryAdapter.notifyDataSetChanged();
                break;
        }
    }

    public void onButtonClose() {
        //强制关闭软键盘
        KeyBoardUtils.closeKeybord(mHomeFragment.mSearchEditText.getEditText(), mHomeFragment
                .getContext());
    }

    public void getHistoryData() {
        List list = mHistoryDao.selectHistory();
        if (list != null && list.size() > 0) {
            mHomeFragment.mList.addAll(list);
        }
    }

    //存储"历史记录"数据到数据库
    public void putHistoryData(String title, String url) {
        DatabaseDao dao = DatabaseDao.getInstance(mHomeFragment.getContext());
        if (HISTORY_LIST_MAX_SIZE != -1 && dao.selectHistorySize() == HISTORY_LIST_MAX_SIZE) {
            //删除最后一条数据
            dao.delete(new HistoryBean(dao.selectHistory().get(0).getLastLoad(), "", ""));
            mHomeFragment.mList.remove(0);
        }
        HistoryBean bean = new HistoryBean(System.currentTimeMillis() + "", title, url);
        if (!dao.isExistHistory(bean)) {
            dao.add(bean);
            mHomeFragment.mList.add(bean);
        }
        mHomeFragment.mHistoryAdapter.notifyDataSetChanged();
    }

    public AdapterView.OnItemClickListener onItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HistoryBean bean = (HistoryBean) mHomeFragment.mList.get(position);
                mHomeFragment.getHomeActivity().getWebView().loadUrl(bean.getUrl());
                mHomeFragment.getHomeActivity().getViewPager().setCurrentItem(1);
            }
        };
    }

    /**
     * 取消加载图片
     */
    public void cancelLoadImage() {
        mRetrofitUtils.cancel();
        ImageLoaderManager.getInstance(mHomeFragment.getContext()).
                cancel(mHomeFragment.mBingIv);
        ImageLoaderManager.getInstance(mHomeFragment.getContext()).
                cancel(mHomeFragment.mMeiziIv);
    }

    public void onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        mHistoryDao.delete((HistoryBean) mHomeFragment.mList.get(menuInfo.position));
        mHomeFragment.mList.remove(menuInfo.position);
        mHomeFragment.mHistoryAdapter.notifyDataSetChanged();
    }

    public View.OnClickListener onCommonWebsiteClickListener(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mHomeFragment.getHomeActivity().getWebView().loadUrl(mHomeFragment
//                        .getCommonWebsiteList().get(position).getAddress());
//                mHomeFragment.getHomeActivity().getViewPager().setCurrentItem(1);
                CommonWebsiteBean bean = mHomeFragment.getCommonWebsiteList().get(position);
                mHomeFragment.getSearchEditText().setText(bean.getAddress());
            }
        };
    }
}