package com.xiajue.browser.localwebbrowser.view.activity.viewInterface;

import android.support.v4.widget.DrawerLayout;
import android.widget.ListView;

import com.xiajue.browser.localwebbrowser.adapter.HomeListAdapter;
import com.xiajue.browser.localwebbrowser.model.bean.HomeListBean;
import com.xiajue.browser.localwebbrowser.view.activity.HomeActivity;
import com.xiajue.browser.localwebbrowser.view.activity.frametag.AboutFragment;
import com.xiajue.browser.localwebbrowser.view.activity.frametag.HomeFragment;
import com.xiajue.browser.localwebbrowser.view.activity.frametag.WebFragment;
import com.xiajue.browser.localwebbrowser.view.custom.ExtendedViewPager;
import com.xiajue.browser.localwebbrowser.view.custom.ExtendedWebView;

import java.util.List;

/**
 * xiaJue 2017/9/15创建
 */
public interface IHomeView {
    DrawerLayout getDrawerLayout();

    ExtendedWebView getWebView();

    HomeListAdapter getAdapter();

    ExtendedViewPager getViewPager();

    List<HomeListBean> getList();

    ListView getListView();

    HomeActivity getActivity();

    HomeFragment getHomeFragment();

    WebFragment getWebFragment();

    AboutFragment getAboutFragment();
}
