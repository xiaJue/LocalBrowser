package com.xiajue.browser.localwebbrowser.view.activity.frametag;

import android.support.v4.app.Fragment;

import com.xiajue.browser.localwebbrowser.view.activity.HomeActivity;

/**
 * Created by Moing_Admin on 2017/10/13.
 */

public class BaseFramtag extends Fragment {

    public  HomeActivity getHomeActivity(){
        return (HomeActivity) getActivity();
    }
}
