package com.xiajue.browser.localwebbrowser.view.activity.frametag;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiajue.browser.localwebbrowser.R;
import com.xiajue.browser.localwebbrowser.model.manager.SettingsManager;
import com.xiajue.browser.localwebbrowser.presenter.HomeFragmentPresenter;
import com.xiajue.browser.localwebbrowser.view.activity.HomeActivity;
import com.xiajue.browser.localwebbrowser.view.custom.SearchEditText;

/**
 * xiaJue 2017/9/19创建
 */
public class HomeFragment extends Fragment implements View.OnClickListener, SearchEditText
        .OnButtonClickListener {
    private CardView mTopLl;
    public SwipeRefreshLayout mRefreshLayout;
    public SearchEditText mSearchEditText;
    public ImageView mMeiziIv;
    public TextView mMeiziTv;
    public ImageView mBingIv;
    public TextView mBingTv;
    public RelativeLayout mMeiziRl;
    public RelativeLayout mBingRl;
    public RelativeLayout mSettings;

    private HomeFragmentPresenter mPresenter;

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

    private void set() {
        if (SettingsManager.getSettingsBoolean(getContext(), SettingsManager.SHOW_HOME_IMAGE)) {
            setImageUrl();//加载图片
        } else {
            mTopLl.setVisibility(View.GONE);
        }
        setEvent();//设置事件
        setRefresh();//设置刷新控件
    }

    private void bindView(View view) {
        mTopLl = (CardView) view.findViewById(R.id.home_home_top_cv);
        mSearchEditText = (SearchEditText) view.findViewById(R.id.home_home_url_edit);
        mMeiziIv = (ImageView) view.findViewById(R.id.home_home_meizi_iv);
        mMeiziTv = (TextView) view.findViewById(R.id.home_home_meizi_tv);
        mBingIv = (ImageView) view.findViewById(R.id.home_home_bing_iv);
        mBingTv = (TextView) view.findViewById(R.id.home_home_bing_tv);
        mMeiziRl = (RelativeLayout) view.findViewById(R.id.home_home_meizi_rl);
        mBingRl = (RelativeLayout) view.findViewById(R.id.home_home_bing_rl);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.home_home_refreshLayout);
        mSettings = (RelativeLayout) view.findViewById(R.id.home_home_settings);
    }

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
                        setImageUrl();
                    }
                }
        );
    }

    private void setImageUrl() {
        mPresenter.setImageUrl();
    }

    private void setEvent() {
        mSearchEditText.setOnButtonClickListener(this);
        mBingRl.setOnClickListener(this);
        mMeiziRl.setOnClickListener(this);
        mSettings.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mPresenter.onClick(v);
    }

    @Override
    public void onButtonClick(View view, String text) {
        mPresenter.onButtonClick(view, text);
    }

    @Override
    public void onButtonClose() {
        mPresenter.onButtonClose();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SettingsManager.SETTINGS_RESULT) {
            //从设置页面返回
            if (SettingsManager.getSettingsBoolean(getContext(), SettingsManager.SHOW_HOME_IMAGE)) {
                mTopLl.setVisibility(View.VISIBLE);
                setImageUrl();//加载图片
            } else {
                mTopLl.setVisibility(View.GONE);
            }
            //设置viewPager滑动
            boolean settingsBoolean = SettingsManager.getSettingsBoolean(getContext(),
                    SettingsManager
                    .SLIDE_TAG);
            ((HomeActivity) getActivity()).getViewPager().setScroll(settingsBoolean);
        }
    }
}