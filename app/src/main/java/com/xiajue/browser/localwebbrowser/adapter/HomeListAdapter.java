package com.xiajue.browser.localwebbrowser.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.xiajue.browser.localwebbrowser.R;
import com.xiajue.browser.localwebbrowser.model.bean.HomeListBean;
import com.xiajue.browser.localwebbrowser.view.custom.CollectionImageButton;

import java.util.List;

/**
 * xiaJue 2017/9/15创建
 */
public class HomeListAdapter extends BaseSwipeAdapter {
    private List<HomeListBean> mList;
    private LayoutInflater mInflater;

    public List<HomeListBean> getList() {
        return mList;
    }


    public HomeListAdapter(Context context, List list) {
        this.mList = list;
        mInflater = LayoutInflater.from(context);
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
    public int getSwipeLayoutResourceId(int position) {
        return R.id.item_home_swipe_layout;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        View v = mInflater.inflate(R.layout.item_home_drawer_list, null);
        return v;
    }

    @Override
    public void fillValues(final int position, View convertView) {
        TextView name = (TextView) convertView.findViewById(R.id.item_home_name);
        HomeListBean bean = mList.get(position);
        name.setText(bean.getName());
        CollectionImageButton imageButton = (CollectionImageButton) convertView.findViewById(R.id
                .item_home_collection);
        imageButton.setCollection(bean.isCollection);
        //set right button
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener == null) {
                    return;
                }
                switch (v.getId()) {
                    case R.id.item_home_remove:
                        listener.onRemove(position);
                        break;
                    case R.id.item_home_delete:
                        listener.onDelete(position);
                        break;
                    case R.id.item_home_collection:
                        listener.onCollection(position);
                        break;
                }
            }
        };
        ImageView removeIv = (ImageView) convertView.findViewById(R.id.item_home_remove);
        removeIv.setOnClickListener(clickListener);
        removeIv.setImageResource(bean.isRemove ? R.mipmap.remove_add : R.mipmap.remove);
        convertView.findViewById(R.id.item_home_delete).setOnClickListener(clickListener);
        convertView.findViewById(R.id.item_home_collection).setOnClickListener(clickListener);
        closeItem(position);
    }

    public interface OnRightButtonClickListener {
        void onRemove(int position);

        void onDelete(int position);

        void onCollection(int position);
    }

    private OnRightButtonClickListener listener;

    /**
     * 设置滑动出现的按钮的点击事件
     */
    public void setOnRightButtonClickListener(OnRightButtonClickListener listener) {
        this.listener = listener;
    }
}
