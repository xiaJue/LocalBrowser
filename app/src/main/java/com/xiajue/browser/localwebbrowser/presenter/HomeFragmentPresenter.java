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
import com.xiajue.browser.localwebbrowser.model.bean.BingBean;
import com.xiajue.browser.localwebbrowser.model.bean.BingBeanList;
import com.xiajue.browser.localwebbrowser.model.bean.HistoryBean;
import com.xiajue.browser.localwebbrowser.model.bean.MeiziBean;
import com.xiajue.browser.localwebbrowser.model.bean.MeiziBeanList;
import com.xiajue.browser.localwebbrowser.model.database.DatabaseDao;
import com.xiajue.browser.localwebbrowser.model.internet.RetrofitUtils;
import com.xiajue.browser.localwebbrowser.model.manager.ImageLoaderManager;
import com.xiajue.browser.localwebbrowser.model.manager.Settings;
import com.xiajue.browser.localwebbrowser.model.utils.KeyBoardUtils;
import com.xiajue.browser.localwebbrowser.model.utils.SPUtils;
import com.xiajue.browser.localwebbrowser.model.utils.ScreenUtils;
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
            if (!text.substring(0, 7).equals("http://") && !text.substring(0, 8).equals
                    ("https://")) {
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

    private boolean isNotInternet;

    public void setImageUrl() {
        /**
         * loading meizi ...
         */
        mRetrofitUtils.getMeiziBean(new Callback() {
            private void getUrlToLoad() {
                onLoadResponse(mMeiziUrl, "meiziUrl", mHomeFragment.mMeiziIv, new
                        SimpleImageLoadingListener() {
                            @Override
                            public void onLoadingFailed(String imageUri, View view, FailReason
                                    failReason) {
                                super.onLoadingFailed(imageUri, view, failReason);
                                mHomeFragment.mRefreshLayout.setRefreshing(false);
                                Toast.makeText(mHomeFragment.getContext(), "加载\"妹子\"图片失败", Toast
                                        .LENGTH_SHORT).show();
                            }

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
                                //设置侧滑菜单背景
                                mHomeFragment.getHomeActivity().getActivity().setDrawerBackground
                                        (loadedImage);
                                //显示图片渐变动画
                                FadeInBitmapDisplayer.animate(mHomeFragment.mMeiziIv, 800);
                                mMeiziLoadFlag = true;
                                if (mMeiziLoadFlag && mBingLoadFlag) {
                                    mHomeFragment.mRefreshLayout.setRefreshing(false);
                                }
                            }
                        });
            }

            @Override
            public void onResponse(Call call, Response response) {
                List<MeiziBean> results = ((MeiziBeanList) response.body()).getResults();
                MeiziBean meiziBean = results.get(0);
                mMeiziUrl = meiziBean.getUrl();
                isNotInternet = false;
                getUrlToLoad();
                mHomeFragment.mMeiziTv.setText(mHomeFragment.getString(R.string.meizi_text));
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                isNotInternet = true;
                getUrlToLoad();
                mMeiziLoadFailureFlag = true;
                if (mMeiziLoadFailureFlag && mBingLoadFailureFlag) {
                    Toast.makeText(mHomeFragment.getContext(), "连接网络失败", Toast.LENGTH_SHORT).show();
                }
                mHomeFragment.mRefreshLayout.setRefreshing(false);
            }
        });
        /**
         * loading bing...
         */
        mRetrofitUtils.getBingBean(new Callback() {
            private void getUrlToLoad() {
                onLoadResponse(mBingUrl, "bingUrl", mHomeFragment.mBingIv, new
                        SimpleImageLoadingListener() {
                            @Override
                            public void onLoadingFailed(String imageUri, View view, FailReason
                                    failReason) {
                                super.onLoadingFailed(imageUri, view, failReason);
                                mHomeFragment.mRefreshLayout.setRefreshing(false);
                                Toast.makeText(mHomeFragment.getContext(), "加载\"必应\"图片失败", Toast
                                        .LENGTH_SHORT).show();
                            }

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
            }

            @Override
            public void onResponse(Call call, Response response) {
                List<BingBean> images = ((BingBeanList) response.body()).getImages();
                BingBean bingBean = images.get(0);
                mBingUrl = bingBean.getAbsUrl();
                mBingTitle = bingBean.getCopyright();
                isNotInternet = false;
                getUrlToLoad();
                mHomeFragment.mBingTv.setText(bingBean.getCopyright() + bingBean.getEnddate());
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                isNotInternet = true;
                getUrlToLoad();
                mBingLoadFailureFlag = true;
                if (mMeiziLoadFailureFlag && mBingLoadFailureFlag) {
                    Toast.makeText(mHomeFragment.getContext(), "连接网络失败", Toast.LENGTH_SHORT).show();
                }
                mHomeFragment.mRefreshLayout.setRefreshing(false);
            }
        });
    }

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
        if (dao.selectHistorySize() == HISTORY_LIST_MAX_SIZE) {
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
}
