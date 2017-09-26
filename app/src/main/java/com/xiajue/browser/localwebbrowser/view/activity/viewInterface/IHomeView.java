package com.xiajue.browser.localwebbrowser.view.activity.viewInterface;

import android.support.v4.widget.DrawerLayout;

import com.tencent.smtt.sdk.WebView;
import com.xiajue.browser.localwebbrowser.adapter.HomeListAdapter;
import com.xiajue.browser.localwebbrowser.model.bean.HomeListBean;
import com.xiajue.browser.localwebbrowser.view.activity.HomeActivity;
import com.xiajue.browser.localwebbrowser.view.custom.ExtendedViewPager;

import java.util.List;

/**
 * xiaJue 2017/9/15创建
 */
public interface IHomeView {
    DrawerLayout getDrawerLayout();

    WebView getWebView();

    HomeListAdapter getAdapter();

    ExtendedViewPager getViewPager();

    List<HomeListBean> getList();

    HomeActivity getActivity();
}
