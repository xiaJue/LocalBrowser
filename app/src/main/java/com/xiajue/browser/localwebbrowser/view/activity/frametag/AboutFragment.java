package com.xiajue.browser.localwebbrowser.view.activity.frametag;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiajue.browser.localwebbrowser.R;
import com.xiajue.browser.localwebbrowser.model.Config;
import com.xiajue.browser.localwebbrowser.view.activity.HomeActivity;

/**
 * xiaJue 2017/9/19创建
 */
public class AboutFragment extends Fragment implements View.OnClickListener {
    private TextView mAddressTv;

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
    }

    private void set() {
        mAddressTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_about_address:
                ((HomeActivity) getActivity()).getWebView().loadUrl(Config.OBJECT_GIT_URL);
                ((HomeActivity) getActivity()).getViewPager().setCurrentItem(1);
                break;
        }
    }
}
