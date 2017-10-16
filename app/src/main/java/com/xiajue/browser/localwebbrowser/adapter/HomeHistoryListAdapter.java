package com.xiajue.browser.localwebbrowser.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.xiajue.browser.localwebbrowser.R;
import com.xiajue.browser.localwebbrowser.model.Config;
import com.xiajue.browser.localwebbrowser.model.bean.HistoryBean;
import com.xiajue.browser.localwebbrowser.model.utils.DensityUtils;
import com.xiajue.browser.localwebbrowser.model.utils.ScreenUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * xiaJue 2017/10/08创建
 */
public class HomeHistoryListAdapter extends BaseAdapter {
    private List<HistoryBean> mList;
    private View mHomeHistoryRootView;
    private Context mContext;
    private ListView mListView;
    private LayoutInflater mInflater;

    public HomeHistoryListAdapter(Context context, List list, View homeHistoryRootView, ListView
            listView) {
        this.mList = list;
        mInflater = LayoutInflater.from(context);
        mHomeHistoryRootView = homeHistoryRootView;
        mListView = listView;
        mContext = context;
    }

    public List<HistoryBean> getList() {
        return mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_home_home_history_list, null);
            holder = new ViewHolder();
            holder.mTitleTv = (TextView) convertView.findViewById(R.id
                    .item_home_History_list_title);
            holder.mUrlTv = (TextView) convertView.findViewById(R.id.item_home_History_list_url);
            holder.mTimeTv = (TextView) convertView.findViewById(R.id.item_home_History_list_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mTitleTv.setText(mList.get(position).getTitle());
        holder.mUrlTv.setText(mList.get(position).getUrl());
        holder.mTimeTv.setText(mList.get(position).getLastLoadFormatString());
        return convertView;
    }

    public class ViewHolder {

        public TextView mTitleTv;
        public TextView mUrlTv;
        public TextView mTimeTv;
    }

    @Override
    public void notifyDataSetChanged() {
        //根据时间排序
        Collections.sort(mList, new Comparator<HistoryBean>() {
            @Override
            public int compare(HistoryBean o1, HistoryBean o2) {
                return Long.valueOf(o2.getLastLoad()).compareTo(Long.valueOf(o1.getLastLoad()));
            }
        });
        //刷新布局--计算listView高度--设置viewGroup高度
//        if (mListView.getChildCount() > 0) {
        int height = getItemHeight();
        int listHeight;
        if (getCount() <= Config.HISTORY_ITEM_SHOW_MAX_ITEM) {
            listHeight = height * getCount();
        } else {
            listHeight = height * Config.HISTORY_ITEM_SHOW_MAX_ITEM;
        }
        mListView.getLayoutParams().height = listHeight - DensityUtils.dp2px(mContext, 10);
        mHomeHistoryRootView.invalidate();
        mHomeHistoryRootView.requestLayout();
//        }

        super.notifyDataSetChanged();
    }

    private int getItemHeight() {
        return (ScreenUtils.getScreenHeight(mContext) / 7 * 3) / Config
                .HISTORY_ITEM_SHOW_MAX_ITEM;//屏幕的2/3
    }
}
