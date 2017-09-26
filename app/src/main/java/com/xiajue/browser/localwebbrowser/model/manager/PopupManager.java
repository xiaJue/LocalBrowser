package com.xiajue.browser.localwebbrowser.model.manager;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.xiajue.browser.localwebbrowser.R;
import com.xiajue.browser.localwebbrowser.model.utils.DensityUtils;

/**
 * xiaJue 2017/9/15创建
 */
public class PopupManager {

    private Context mContext;
    private LayoutInflater mInflater;

    public PopupManager(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public void showLongClickImagePopup(View v, int x, int y, final OnImagePopupMenuClickListener clickListener) {
        View view = mInflater.inflate(R.layout.popup_long_click_image, null);
        View saveImage = view.findViewById(R.id.home_popup_save_image);
        //set tempOnClickListener

//        openImage.setOnClickListener(listener);

        final PopupWindow popupWindow = new PopupWindow(view, DensityUtils.dp2px(mContext, 100),
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAtLocation(v, Gravity.TOP | Gravity.LEFT, x - view.getWidth(), y + DensityUtils.dp2px(mContext, 30));
//        View openImage = view.findViewById(R.id.home_popup_open_image);
        //click listener
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                switch (v.getId()) {
                    case R.id.home_popup_save_image:
                        clickListener.onSaveImage();
                        break;
//                    case R.id.home_popup_open_image:
//                        clickListener.onOpenImage();
//                        break;
                }
            }
        };
        saveImage.setOnClickListener(listener);
    }

    public void showLongClickLinkPopup(View v, int x, int y, final OnLinkPopupMenuClickListener clickListener) {
        View view = mInflater.inflate(R.layout.popup_long_click_link, null);
        View openLink = view.findViewById(R.id.home_popup_open_link);

        final PopupWindow popupWindow = new PopupWindow(view, DensityUtils.dp2px(mContext, 150),
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAtLocation(v, Gravity.TOP | Gravity.LEFT, x - view.getWidth(), y + DensityUtils.dp2px(mContext, 30));
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                switch (v.getId()) {
                    case R.id.home_popup_open_link:
                        clickListener.onOpenLink();
                        break;
                }
            }
        };
        openLink.setOnClickListener(listener);
    }

    public interface OnImagePopupMenuClickListener {
        void onSaveImage();
    }

    public interface OnLinkPopupMenuClickListener {
        void onOpenLink();
    }
}
