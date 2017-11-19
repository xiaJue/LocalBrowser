package com.xiajue.browser.localwebbrowser.view.activity.frametag;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fynn.fluidlayout.FluidLayout;
import com.xiajue.browser.localwebbrowser.R;
import com.xiajue.browser.localwebbrowser.adapter.HomeHistoryListAdapter;
import com.xiajue.browser.localwebbrowser.model.Config;
import com.xiajue.browser.localwebbrowser.model.bean.CommonWebsiteBean;
import com.xiajue.browser.localwebbrowser.model.manager.Settings;
import com.xiajue.browser.localwebbrowser.model.manager.SettingsUtils;
import com.xiajue.browser.localwebbrowser.model.utils.DensityUtils;
import com.xiajue.browser.localwebbrowser.presenter.HomeFragmentPresenter;
import com.xiajue.browser.localwebbrowser.view.custom.AdaptListView;
import com.xiajue.browser.localwebbrowser.view.custom.SearchEditText;
import com.xiajue.browser.localwebbrowser.view.custom.ExtendedNestedScrollView;

import java.util.ArrayList;
import java.util.List;


/**
 * xiaJue 2017/9/19创建
 */
public class HomeFragment extends BaseFramtag implements View.OnClickListener, SearchEditText
        .OnButtonClickListener, SearchEditText.OnStateChangeListener {
    private View mTopView;
    public SwipeRefreshLayout mRefreshLayout;
    public SearchEditText mSearchEditText;
    public ImageView mMeiziIv;
    public TextView mMeiziTv;
    public ImageView mBingIv;
    public TextView mBingTv;
    public RelativeLayout mMeiziRl;
    public RelativeLayout mBingRl;
    public RelativeLayout mSettings;
    public AdaptListView mListView;
    public TextView mClearHistoryTv;
    public HomeHistoryListAdapter mHistoryAdapter;
    public HomeFragmentPresenter mPresenter;
    public View mHomeHistoryRootView;
    public ExtendedNestedScrollView mNestedScrollView;
    public FluidLayout mFluidLayout;
    public TextView mCommonTv;
    public List mList;

    public HomeFragment() {
        mPresenter = new HomeFragmentPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_fragment_home, container, false);
        bindView(view);//绑定view
        set();
        return view;
    }

    /**
     * 绑定view
     */
    private void bindView(View view) {
        mTopView = view.findViewById(R.id.home_home_top_v);
        mSearchEditText = (SearchEditText) view.findViewById(R.id.home_home_url_edit);
        mMeiziIv = (ImageView) view.findViewById(R.id.home_home_meizi_iv);
        mMeiziTv = (TextView) view.findViewById(R.id.home_home_meizi_tv);
        mBingIv = (ImageView) view.findViewById(R.id.home_home_bing_iv);
        mBingTv = (TextView) view.findViewById(R.id.home_home_bing_tv);
        mMeiziRl = (RelativeLayout) view.findViewById(R.id.home_home_meizi_rl);
        mBingRl = (RelativeLayout) view.findViewById(R.id.home_home_bing_rl);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.home_home_refreshLayout);
        mSettings = (RelativeLayout) view.findViewById(R.id.home_home_settings);
        mListView = (AdaptListView) view.findViewById(R.id.home_home_listView);
        mClearHistoryTv = (TextView) view.findViewById(R.id.home_home_clear_History);
        mHomeHistoryRootView = view.findViewById(R.id.home_home_history_root_v);
        mNestedScrollView = (ExtendedNestedScrollView) view.findViewById(R.id.home_home_nested);
        mFluidLayout = (FluidLayout) view.findViewById(R.id.home_home_fluidLayout);
        mCommonTv = (TextView) view.findViewById(R.id.home_home_common_tv);
    }

    /**
     * 是否显示首页图片
     */
    private boolean isShowImage = true;

    private void set() {
        if (isShowImage = SettingsUtils.isShowHomeImage(getContext(), true)) {
            mRefreshLayout.setEnabled(true);
            setImageUrl();//加载图片
        } else {
            mTopView.setVisibility(View.GONE);
            mRefreshLayout.setEnabled(false);
            getHomeActivity().setDrawerBackground(null);
        }
        setEvent();//设置事件
        setRefresh();//设置刷新控件
        setHistoryListView();//设置历史ListView
        setFluidLayout();
    }

    private List mCommonWebsiteList;

    private void setFluidLayout() {
        mCommonWebsiteList = new ArrayList();
        for (int i = 0; i < Config.COMMON_ADDRESS.length; i++) {
            CommonWebsiteBean bean = new CommonWebsiteBean(Config.COMMON_ADDRESS_TITLE[i], Config
                    .COMMON_ADDRESS[i]);
            mCommonWebsiteList.add(bean);
            TextView textView = new TextView(getContext());
            textView.setText(bean.getTitle());
            //设置padding
            int leftPadding = DensityUtils.dp2px(getContext(), 3);
            int topPadding = DensityUtils.dp2px(getContext(), 2);
            textView.setPadding(leftPadding, topPadding, leftPadding, topPadding);
            //set background
            textView.setBackgroundResource(R.drawable.button_back);
            //set onClickListener
            textView.setOnClickListener(mPresenter.onCommonWebsiteClickListener(i));
            //设置margins
            FluidLayout.LayoutParams lp = new FluidLayout.LayoutParams(ViewGroup.LayoutParams
                    .WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(leftPadding, topPadding, leftPadding, topPadding);
            textView.setLayoutParams(lp);
            //add
            mFluidLayout.addView(textView);
        }
    }

    /**
     * 设置listView
     */
    private void setHistoryListView() {
        mList = new ArrayList();
        mPresenter.getHistoryData();//读取历史访问数据
        mHistoryAdapter = new HomeHistoryListAdapter(getContext(), mList, mHomeHistoryRootView,
                mListView);
        mListView.setAdapter(mHistoryAdapter);
        mListView.setList(mList);
        mHistoryAdapter.notifyDataSetChanged();//预先排序一次
        mListView.setOnItemClickListener(mPresenter.onItemClickListener());
        mListView.setFocusable(false);
        //长按删除--上下文菜单
        registerForContextMenu(mListView);
    }

    /**
     * 创建上下文菜单
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo
            menuInfo) {
        menu.setHeaderTitle(R.string.headerTitle);
        menu.add(0, 0, Menu.NONE, R.string.delete);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    /**
     * 当选择上下文菜单时
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        mPresenter.onContextItemSelected(item);
        return super.onContextItemSelected(item);
    }

    /**
     * 设置刷新组件
     */
    private void setRefresh() {
        //init refresh
        mRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
        mRefreshLayout.setColorSchemeResources(
                R.color.refresh_color,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        // 刷新动画开始后回调到此方法
                        if (isShowImage) {
                            setImageUrl();
                        }
                    }
                }
        );
    }

    /**
     * 加载图片
     */
    private void setImageUrl() {
        mPresenter.setImageUrl();
    }

    /**
     * 设置事件
     */
    private void setEvent() {
        mSearchEditText.setOnButtonClickListener(this);
        mSearchEditText.setOnStateChangeListener(this);
        mBingRl.setOnClickListener(this);
        mMeiziRl.setOnClickListener(this);
        mSettings.setOnClickListener(this);
        mClearHistoryTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mPresenter.onClick(v);
    }

    @Override
    public void onStateChange(boolean state) {
        if (state) {
            mFluidLayout.setVisibility(View.VISIBLE);
            mCommonTv.setVisibility(View.VISIBLE);
        } else {
            mFluidLayout.setVisibility(View.GONE);
            mCommonTv.setVisibility(View.GONE);
            mPresenter.onButtonClose();
        }
    }

    @Override
    public void onButtonClick(View view, String text) {
        mPresenter.onButtonClick(view, text);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Settings.SETTINGS_RESULT) {
            //从设置页面返回
            if (SettingsUtils.isShowHomeImage(getContext(), true)) {
                mTopView.setVisibility(View.VISIBLE);
                setImageUrl();//加载图片
                mRefreshLayout.setEnabled(true);
            } else {
                mTopView.setVisibility(View.GONE);
                mRefreshLayout.setEnabled(false);
            }
            //设置viewPager滑动
            boolean settingsBoolean = SettingsUtils.isSlideTag(getContext(), true);
            getHomeActivity().getViewPager().setScroll(settingsBoolean);
        }
    }

    public List<CommonWebsiteBean> getCommonWebsiteList() {
        return mCommonWebsiteList;
    }

    /**
     * 取消加载图片
     */
    public void cancelLoadImage() {
        mPresenter.cancelLoadImage();
        mPresenter = null;
    }

    public SearchEditText getSearchEditText() {
        return mSearchEditText;
    }
}