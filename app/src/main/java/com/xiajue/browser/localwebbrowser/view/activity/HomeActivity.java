package com.xiajue.browser.localwebbrowser.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.smtt.sdk.WebView;
import com.xiajue.browser.localwebbrowser.R;
import com.xiajue.browser.localwebbrowser.adapter.HomeListAdapter;
import com.xiajue.browser.localwebbrowser.model.Config;
import com.xiajue.browser.localwebbrowser.model.bean.HomeListBean;
import com.xiajue.browser.localwebbrowser.model.manager.SettingsManager;
import com.xiajue.browser.localwebbrowser.model.utils.KeyBoardUtils;
import com.xiajue.browser.localwebbrowser.model.utils.SPUtils;
import com.xiajue.browser.localwebbrowser.presenter.HomePresenter;
import com.xiajue.browser.localwebbrowser.view.activity.frametag.AboutFragment;
import com.xiajue.browser.localwebbrowser.view.activity.frametag.HomeFragment;
import com.xiajue.browser.localwebbrowser.view.activity.frametag.WebFragment;
import com.xiajue.browser.localwebbrowser.view.activity.viewInterface.IHomeView;
import com.xiajue.browser.localwebbrowser.view.custom.ExtendedViewPager;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity implements IHomeView, View.OnClickListener {

    private Toolbar mToolbar;
    private DrawerLayout mDrawLayout;
    private ExtendedViewPager mViewPager;
    private ActionBarDrawerToggle mToggle;
    //    private WebView mWebFragment.getWebView();
    private List mList;
    //public view
    public TextView mToolbarText;
    public ProgressBar mListProgressBar;
    //list view
    public ListView mListView;
    public TextView mListNullTv;
    private HomeListAdapter mAdapter;
    public ImageView mRemoveSee;
    public ImageView mCollectionSee;
    public ImageView mAllSee;
    public ImageView mSearch;
    public EditText mSearchEdit;
    public boolean isSearchState = false;//是否在搜索状态
    public TextView mPathTextView;
    public TextView mSizeText;
    //    public ProgressBar mWebFragment.getProgressBar();
//    public SwipeRefreshLayout mWebFragment.getRefreshLayout();
    private ImageView mHome;
    private ImageView mWeb;
    private ImageView mAbout;

    private RelativeLayout mStartUi;
    //presenter
    public HomePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mPresenter = new HomePresenter(this);
        bindViews();
        set();
    }

    private void bindViews() {
        //initialize all view
        mListView = getView(R.id.home_listView);
        mListProgressBar = getView(R.id.home_list_progressBar);
        mListNullTv = getView(R.id.home_list_null_tv);
        mPathTextView = getView(R.id.home_listPath_tv);
        mSizeText = getView(R.id.home_list_size_tv);
        mToolbar = getView(R.id.home_toolbar);
        mToolbarText = getView(R.id.home_toolbar_text);
        mDrawLayout = getView(R.id.home_drawLayout);
        mRemoveSee = getView(R.id.home_remove_see);
        mCollectionSee = getView(R.id.home_collection_see);
        mAllSee = getView(R.id.home_all_see);
        mSearch = getView(R.id.home_search);
        mSearchEdit = getView(R.id.home_search_edit);
        mViewPager = getView(R.id.home_viewPager);
        mWebFragment = new WebFragment(mPresenter);
        mHomeFragment = new HomeFragment();
        mAboutFragment = new AboutFragment();
        mHome = getView(R.id.home_home_iv);
        mWeb = getView(R.id.home_web_iv);
        mAbout = getView(R.id.home_about_iv);
        mStartUi = getView(R.id.home_start_ui);
    }

    private void set() {
        //set toolbar
        setToolbar();
        //set drawerLayout
        setDrawerLayout();
        //set viewPager
        setViewPager();
        //set home image bottom
        mHome.setOnClickListener(this);
        mWeb.setOnClickListener(this);
        mAbout.setOnClickListener(this);
        //set listView top button
        mRemoveSee.setOnClickListener(this);
        mCollectionSee.setOnClickListener(this);
        mAllSee.setOnClickListener(this);
        mSearch.setOnClickListener(this);
        //set list data editText
        mPathTextView.setOnClickListener(this);
        String path = SPUtils.getInstance(this).getString("list_path", getString(R.string
                .select_path));
        mPathTextView.setText(path);
    }

    public WebFragment mWebFragment;
    private HomeFragment mHomeFragment;
    private AboutFragment mAboutFragment;
    private List<Fragment> mFragmentList;

    private void setViewPager() {
        mFragmentList = new ArrayList();
        mFragmentList.add(mHomeFragment);
        mFragmentList.add(mWebFragment);
        mFragmentList.add(mAboutFragment);
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int
                    positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setImageButtonSelect(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        boolean settingsBoolean = SettingsManager.getSettingsBoolean(this, SettingsManager
                .SLIDE_TAG);
        mViewPager.setScroll(settingsBoolean);
//        mViewPager.setCurrentItem(0);
    }

    public void setImageButtonSelect(int position) {
        switch (position) {
            case 0:
                mHome.setImageResource(R.mipmap.home);
                mAbout.setImageResource(R.mipmap.about_out);
                mWeb.setImageResource(R.mipmap.web_out);
                mToolbarText.setText(R.string.home);
                break;
            case 1:
                mWeb.setImageResource(R.mipmap.web);
                mHome.setImageResource(R.mipmap.home_out);
                mAbout.setImageResource(R.mipmap.about_out);
                if (getWebView().getTitle() == null || getWebView().getTitle().isEmpty()) {
                    mToolbarText.setText(R.string.web);
                } else {
                    mToolbarText.setText(getWebView().getTitle());
                }
                break;
            case 2:
                mAbout.setImageResource(R.mipmap.about);
                mHome.setImageResource(R.mipmap.home_out);
                mWeb.setImageResource(R.mipmap.web_out);
                mToolbarText.setText(R.string.about);
                break;
        }
    }

    private void setToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbarText.setText(R.string.home);
    }

    private void setDrawerLayout() {
        mDrawLayout.setScrimColor(Color.TRANSPARENT);
        mToggle = new ActionBarDrawerToggle(this, mDrawLayout, R.string.open, R.string.close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                KeyBoardUtils.closeKeybord(mSearchEdit, HomeActivity.this);
            }
        };
        mDrawLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        //set listView
        mList = new ArrayList();
        mPresenter.put2listFromDatabase(mList);
        mAdapter = new HomeListAdapter(this, mList);
        mAdapter.setOnRightButtonClickListener(mPresenter.onRightButtonClick());
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(mPresenter.onItemClickListener());
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                if (mList.size() == 0) {
                    mSizeText.setText("0/0");
                } else {
                    mSizeText.setText((firstVisibleItem + 1) + "/" + mList.size());
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        mPresenter.onMenuItemSelected(item);
        return super.onOptionsItemSelected(item);
    }

    public boolean homeFlag = true;//是否处在无网页加载的状态
    public boolean backFlag = false;//再按一次退出

    @Override
    public void onBackPressed() {
        if (isSearchState) {
            isNeedCloseSearch(isSearchState);
            return;
        }
        boolean isOpen = mDrawLayout.isDrawerOpen(Gravity.START);
        //关闭侧滑菜单
        if (isOpen) {
            mDrawLayout.closeDrawers();
            return;
        }
        if (mViewPager.getCurrentItem() == 1) {
            //网页返回
            if (mWebFragment.getWebView().canGoBack()) {
                mWebFragment.getWebView().goBack();// 返回前一个页面
                return;
            } else {
                if (!homeFlag) {
                    mPresenter.showHome();
                    homeFlag = true;
                    return;
                }
            }
        }
        //输入框返回
        if (mHomeFragment.mSearchEditText.isEditState()) {
            mHomeFragment.mSearchEditText.setEditState(false);
            return;
        }
        //再按一次退出
        if (!backFlag) {
            backFlag = true;
            Toast.makeText(this, R.string.click_again, Toast.LENGTH_SHORT).show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(Config.CLICK_TO_BACK_SLEEP_LENGTH);
                        backFlag = false;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            return;
        }
        super.onBackPressed();
    }

    /**
     * 是否关闭搜索框
     */
    public void isNeedCloseSearch(boolean b) {
        if (b) {
            //关闭搜索框
            mSearchEdit.setText("");
            mAllSee.setVisibility(View.GONE);
            mSearchEdit.setVisibility(View.GONE);
            mSearch.setVisibility(View.VISIBLE);
            mCollectionSee.setVisibility(View.VISIBLE);
            mRemoveSee.setVisibility(View.VISIBLE);
            //再次显示所有条目
            mPresenter.put2listFromDatabase(mList);
            mAdapter.notifyDataSetChanged();
            isSearchState = false;
            //关闭软键盘
            KeyBoardUtils.closeKeybord(mSearchEdit, HomeActivity.this);
        } else {
            //显示搜索框
            mAllSee.setVisibility(View.VISIBLE);
            mSearchEdit.setVisibility(View.VISIBLE);
            mSearch.setVisibility(View.GONE);
            mCollectionSee.setVisibility(View.GONE);
            mRemoveSee.setVisibility(View.GONE);
            isSearchState = true;
            //开启软键盘
            KeyBoardUtils.openKeybord(mSearchEdit, HomeActivity.this);
            mSearchEdit.requestFocus();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyWebView();
    }

    /**
     * 解决WebView持有mContext导致的内存泄漏问题
     */
    private void destroyWebView() {
        if (mWebFragment.getWebView() != null) {
            ViewParent parent = mWebFragment.getWebView().getParent();
            if (parent != null) ((ViewGroup) parent).removeView(mWebFragment.getWebView());
            mWebFragment.getWebView().stopLoading();
            mWebFragment.getWebView().getSettings().setJavaScriptEnabled(false);
            mWebFragment.getWebView().clearHistory();
            mWebFragment.getWebView().clearView();
            mWebFragment.getWebView().removeAllViews();
            try {
                mWebFragment.getWebView().destroy();
            } catch (Throwable e) {
            }
        }
    }

    /**
     * 始终在右上角显示菜单-有些手机点击菜单键后在手机下方显示菜单
     */
    @Override
    public boolean onKeyUp(int keycode, KeyEvent e) {
        switch (keycode) {
            case KeyEvent.KEYCODE_MENU:
                mToolbar.showOverflowMenu();
                return true;
        }
        return super.onKeyUp(keycode, e);
    }

    @Override
    public DrawerLayout getDrawerLayout() {
        return mDrawLayout;
    }

    @Override
    public WebView getWebView() {
        return mWebFragment.getWebView();
    }

    @Override
    public HomeListAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public ExtendedViewPager getViewPager() {
        return mViewPager;
    }

    @Override
    public List<HomeListBean> getList() {
        return mList;
    }

    @Override
    public HomeActivity getActivity() {
        return this;
    }

    @Override
    public void onClick(View v) {
        mPresenter.onClickListener(v);
    }
}
