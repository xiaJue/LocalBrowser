package com.xiajue.popupmenu.popupWindowMenu;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Created by xiaJue on 2017/9/30.
 */

public class PopWinMenu {
    private static final int OVERFLOW_ID = 507;
    private PopWinHandle mPopWinHandle;
    private View view;
    private List<View> mItemViews;
    //context
    private Context mContext;
    //location
    private int mWidth;//屏幕宽度
    private boolean isUserSetMargin;
    private int mMarginHorizontal;
    private int mMarginTop = 0;
    private int mGravity = Gravity.TOP | Gravity.RIGHT;

    public PopWinMenu(Context context, View view) {
        WindowManager window = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mWidth = window.getDefaultDisplay().getWidth();
        mMenuWidth = mWidth / 7 * 4;
        initialize(context, view);
        mItemViews = new ArrayList<>();
    }

    public PopWinMenu(Context context, View view, int menuWidth) {
        mMenuWidth = menuWidth;
        initialize(context, view);
    }

    private void initialize(Context context, View view) {
        this.mContext = context;
        this.view = view;
        mPopWinHandle = new PopWinHandle(context, mMenuWidth);
    }

    /**
     * 显示菜单
     */
    public void show() {
        int tempMargin = 0;
        if (mGravity == (Gravity.TOP | Gravity.RIGHT) || mGravity == (Gravity.TOP | Gravity
                .CENTER) || mGravity == (Gravity.TOP | Gravity.LEFT)) {
            //右上  左上  中上
            //设置背景
            mPopWinHandle.getBgLayout().setBackgroundResource(R.drawable.popup_menu_top_bg);
            //是否需要默认margin
            if (!isUserSetMargin && mGravity != (Gravity.TOP | Gravity
                    .CENTER)) tempMargin = dp2px(mContext, 20);
            //显示
            mPopWinHandle.show(view, mGravity, tempMargin + mMarginHorizontal,
                    mMarginTop + ((int) view.getY()) + view.getHeight() + getStatusHeight());
        }
        if (mGravity == (Gravity.BOTTOM | Gravity.RIGHT) || mGravity == (Gravity.BOTTOM | Gravity
                .CENTER) || mGravity == (Gravity.BOTTOM | Gravity.LEFT)) {
            //右下 左下 中下
            //设置背景
            mPopWinHandle.getBgLayout().setBackgroundResource(R.drawable.popup_menu_bottom_bg);
            //是否需要默认margin
            if (!isUserSetMargin && mGravity != (Gravity.BOTTOM | Gravity
                    .CENTER)) tempMargin = dp2px(mContext, 20);
            //显示
            mPopWinHandle.show(view, mGravity, tempMargin + mMarginHorizontal,
                    view.getHeight() + mMarginTop);
        }
    }

    /**
     * 显示菜单
     * 相对于整个屏幕
     */
    public void showAtLocation(int gravity, int offsetX, int offsetY) {
        mPopWinHandle.getPopupWindow().showAtLocation(view, gravity, offsetX, offsetY);
    }

    /**
     * 取消显示
     */
    public void dismiss() {
        mPopWinHandle.dismiss();
    }

    /**
     * 如果设置为overflow，在activity的onOptionsItemSelected中调用此方法
     */
    public void onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == OVERFLOW_ID) {
            show();
        }
    }

    /**
     * 添加一个最简单的菜单条目
     */
    public void add(String title) {
        add(new MenuItemInfo(title));
    }

    /**
     * 添加一个最简单的菜单条目
     */
    public void add(int stringRes) {
        add(new MenuItemInfo(mContext.getString(stringRes)));
    }

    /**
     * 添加一个最DIY的菜单条目
     */
    public void add(View view) {
        add(new MenuItemInfo(view));
    }

    private int mItemCount;//条目的个数顺序

    /**
     * 添加一组菜单
     */
    public void add(String[] menus) {
        for (int i = 0; i < menus.length; i++) {
            add(menus[i]);
        }
    }

    /**
     * 添加一组菜单
     *
     * @param titles       title int[stringResId] 条目的标题。必须设置
     * @param icons        icon int[]{res} 条目的图标resId,如果不需要则设置传递null
     * @param textColorRes textColor 如果不需要设置则传递0
     * @param textSize     textSize 如果不需要则设置传递0
     * @param bgColorRes   条目的bgColor 如果不需要则设置传递0
     * @param margins      条目的margin int[]{left,top,right,bottom} 如果不需要则设置传递null
     */
    public void add(int[] titles, int[] icons, int textColorRes, int textSize, int bgColorRes, int[]
            margins) {
        String[] titleArr = new String[titles.length];
        for (int i = 0; i < titles.length; i++) {
            titleArr[i] = mContext.getString(titles[i]);
        }
        add(titleArr, icons, textColorRes != 0 ? getC(textColorRes) : 0, textSize, bgColorRes !=
                0 ? getC(bgColorRes) : 0, margins);
    }

    /**
     * 添加一组菜单
     *
     * @param titles    title string[string] 条目的标题。必须设置
     * @param icons     icon int[]{res} 条目的图标resId,如果不需要则设置传递null
     * @param textColor textColor 如果不需要设置则传递0
     * @param textSize  textSize 如果不需要则设置传递0
     * @param bgColor   条目的bgColor 如果不需要则设置传递0
     * @param margins   条目的margin int[]{left,top,right,bottom} 如果不需要则设置传递null
     */
    public void add(String[] titles, int[] icons, int textColor, int textSize, int bgColor, int[]
            margins) {
        for (int i = 0; i < titles.length; i++) {
            MenuItemInfo info = new MenuItemInfo();
            info.setTitle(titles[i]);
            if (textColor != 0) {
                info.setTextColor(textColor);
            }
            if (textSize != 0) {
                info.setTextSize(textSize);
            }
            if (bgColor != 0) {
                info.setBgColor(bgColor);
            }
            if (icons != null && i < icons.length) {
                info.setIcon(mContext, icons[i]);
            }
            if (margins != null && margins.length == 4) {
                info.marginLeft = margins[0];
                info.marginTop = margins[1];
                info.marginRight = margins[2];
                info.marginBottom = margins[3];
            }
            add(info);
        }
    }

    /**
     * 添加一个菜单条目
     *
     * @param info 菜单条目的属性
     */
    public void add(final MenuItemInfo info) {
        final View itemView;
        if (info.getView() != null) {
            itemView = info.getView();
        } else {
            //get view
            itemView = mPopWinHandle.getInflater().inflate(R.layout.popup_menu_item_layout, null)
                    .findViewById(R.id.popup_menu_item_ll);
            TextView titleView = (TextView) itemView.findViewById(R.id.popup_menu_title);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.popup_menu_icon);
            //set title
            titleView.setText(info.getTitle());
            titleView.setTextSize(info.getTextSize());
            titleView.setTextColor(info.getTextColor());
            //set image
            if (info.getIcon() != null) {
                imageView.setImageBitmap(info.getIcon());
                imageView.setVisibility(View.VISIBLE);
            }
            //set background
            if (mBgColor == 0) {
                itemView.setBackgroundColor(info.getBgColor());
            } else {
                itemView.setBackgroundColor(mBgColor);
            }
            //set select background
            itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        switch (mTouchBgStyle) {
                            case T_BG_COLOR:
                                itemView.setBackgroundColor(mTouchBgColor);
                                break;
                            case T_BG_RES:
                                itemView.setBackgroundResource(mTouchBgRes);
                                break;
                            case T_BG_DRAWABLE:
                                itemView.setBackgroundDrawable(mTouchBgDrawable);
                                break;
                        }
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (mBgColor == 0) {
                            itemView.setBackgroundColor(info.getBgColor());
                        } else {
                            itemView.setBackgroundColor(mBgColor);
                        }
                    }
                    return false;
                }
            });
            //set padding
            itemView.setPadding(dp2px(mContext, info.marginLeft),
                    dp2px(mContext, info.marginTop),
                    dp2px(mContext, info.marginRight),
                    dp2px(mContext, info.marginBottom));
        }
        if (mItemCount > 0 && isShowDivider) {
            LinearLayout linearLayout = new LinearLayout(mContext);
            linearLayout.setBackgroundColor(mDividerColor);
            mPopWinHandle.getLayout().addView(linearLayout, MATCH_PARENT, dp2px(mContext,
                    mDividerHeight));
        }
        itemView.setTag(mItemCount++);
        //set view
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFastDismiss) {
                    dismiss();
                }
                if (mItemSelectListener != null) {
                    mItemSelectListener.onItemSelect((Integer) itemView.getTag());
                }
            }
        });
        mPopWinHandle.getLayout().addView(itemView);
        mItemViews.add(itemView);
    }

    /**
     * 条目点击事件
     */
    private OnItemSelectListener mItemSelectListener;

    /**
     * 设置菜单条目的点击事件
     */
    public void setOnItemSelectListener(OnItemSelectListener listener) {
        this.mItemSelectListener = listener;
    }

    /**
     * 获得res中的color
     *
     * @param id resId
     * @return color
     */
    private int getC(int id) {
        return mContext.getResources().getColor(id);
    }

    public interface OnItemSelectListener {

        void onItemSelect(int selectIndex);

    }

    /**
     * 是否外部触摸不消失
     */
    public void setDontOutsideTouchClose() {
        mPopWinHandle.setOutsideTouchable(false);
    }

    public boolean isFastDismiss = true;

    /**
     * 选择条目后是否立即取消显示
     */
    public void setSelectingAutoDismiss(boolean dismiss) {
        isFastDismiss = dismiss;
    }

    /**
     * 屏幕左边或右边的偏移距离
     *
     * @param marginHorizontal margin size,可以为负数
     */
    public void setMarginHorizontal(int marginHorizontal) {
        isUserSetMargin = true;
        mMarginHorizontal = marginHorizontal;
    }

    /**
     * 屏幕顶端或底端的偏移距离
     */
    public void setMarginTop(int marginTop) {
        mMarginTop = marginTop;
    }

    /**
     * 设置菜单的弹出位置。传入Gravity的两个值。<p>
     * 如Gravity.TOP|Gravity.RIGHT：<p>
     * 第一个值表示菜单显示在屏幕的上方还是下方，第二个值表示菜单显示在屏幕的左边、中间还是右边
     * <p>
     * **根据构造中传入的View自动偏移位置：<p>
     * 如果为TOP，则自动偏移至view的下方<p>
     * 如果为BOTTOM，则偏移至view的上方<p>
     * marginHorizontal和marginTop的偏移：<p>
     * LEFT  相对于屏幕左边向右偏移marginHorizontal个距离<p>
     * RIGHT  相对于屏幕右边向左偏移marginHorizontal个距离<p>
     * CENTER  相对于屏幕左边向右偏移marginHorizontal个距离<p>
     * OP 相对于屏幕顶端向下偏移marginTop个距离<p>
     * BOTTOM 相对于屏幕底端向上偏移marginTop个距离<p>
     * <p>
     * 可设置的值:<p>
     * Gravity.TOP|Gravity.LEFT <p>
     * Gravity.TOP|Gravity.CENTER<p>
     * Gravity.TOP|Gravity.RIGHT<p>
     * Gravity.BOTTOM|Gravity.LEFT <p>
     * Gravity.BOTTOM|Gravity.CENTER<p>
     * Gravity.BOTTOM|Gravity.RIGHT<p>
     */
    public void setGravity(int gravity) {
        mGravity = gravity;
    }

    /**
     * 设置内容根布局的背景,不会影响默认背景中的阴影效果
     */
    public void setBackground(Drawable drawable) {
        mPopWinHandle.getLayout().setBackgroundDrawable(drawable);
    }

    /**
     * 设置内容根布局的背景,不会影响默认背景中的阴影效果
     */
    public void setBackground(int res) {
        mPopWinHandle.getLayout().setBackgroundResource(res);
    }

    private int mBgColor = 0;

    /**
     * 设置内容根布局的背景,不会影响默认背景中的阴影效果
     */
    public void setBackgroundColorRes(int id) {
        int bg = mContext.getResources().getColor(id);
        mBgColor = bg;
    }

    /**
     * 设置内容根布局的背景,不会影响默认背景中的阴影效果
     */
    public void setBackgroundColor(int color) {
        mBgColor = color;
    }

    /**
     * 设置最外层的根布局背景。会改变阴影效果
     */
    public void setRootBackground(int res) {
        mPopWinHandle.getBgLayout().setBackgroundResource(res);
    }

    private int mMenuWidth;

    /**
     * 设置菜单的宽度
     */
    public void setMenuWidth(int width) {
        if (width > mWidth) {
            width = mWidth;
        }
        this.mMenuWidth = width;
    }

    private boolean isShowDivider;
    private int mDividerHeight;
    private int mDividerColor;

    /**
     * 设置分割线,默认不显示
     * 必须在调用add前调用才能生效
     *
     * @param dividerHeight 单位dp
     */
    public void setDivider(int dividerHeight, int color) {
        this.mDividerHeight = dividerHeight;
        mDividerColor = color;
        isShowDivider = true;
    }

    /**
     * 调用此方法后添加的菜单条目不会有分割线
     */
    public void cancelShowDivider() {
        isShowDivider = false;
    }

    /**
     * 从指定View点击后打开菜单
     */
    public void setOpenFromView(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show();
            }
        });
    }

    private static final int T_BG_COLOR = 721;
    private static final int T_BG_DRAWABLE = 722;
    private static final int T_BG_RES = 723;
    private int mTouchBgStyle = T_BG_COLOR;
    private int mTouchBgColor = Color.parseColor("#DFDFDF");

    /**
     * 设置当手指触摸到条目时条目的背景颜色
     */
    public void setOnTouchItemBgColor(int color) {
        this.mTouchBgColor = color;
        mTouchBgStyle = T_BG_COLOR;
    }

    private int mTouchBgRes;

    /**
     * 设置当手指触摸到条目时条目的背景资源
     */
    public void setOnTouchItemBgRes(int res) {
        this.mTouchBgRes = res;
        mTouchBgStyle = T_BG_RES;
    }

    private Drawable mTouchBgDrawable;

    /**
     * 设置当手指触摸到条目时条目的背景Drawable
     */
    public void setOnTouchItemBgDrawable(Drawable drawable) {
        this.mTouchBgDrawable = drawable;
        mTouchBgStyle = T_BG_DRAWABLE;
    }

    /**
     * 通过toolbar左上角的overflow打开
     * 重写Activity中的
     *
     * @Override public boolean onCreateOptionsMenu(Menu menu){
     * createOverflowStyle(menu);
     * }
     */
    public static final int TYPE_OVERFLOW_ICON_LIGHT = 57;
    public static final int TYPE_OVERFLOW_ICON_DARK = 59;

    public void createOverflowStyle(Menu menu, int... type) {
        MenuItem item = menu.add(0, OVERFLOW_ID, 0, "overflow");
        int iconRes = R.drawable.overflow_icon;
        if (type != null && type.length > 0 && type[0] == TYPE_OVERFLOW_ICON_DARK) {
            iconRes = R.drawable.overflow_dark_icon;
        }
        item.setIcon(iconRes);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                show();
                return false;
            }
        });
    }

    /**
     * 通过系统菜单键打开菜单
     */
    public void onKeyUp(int keyCode) {
        if (keyCode == KeyEvent.KEYCODE_MENU && !mPopWinHandle.getPopupWindow().isShowing()) {
            show();
        }
        //dismiss
        mPopWinHandle.getPopupView().setFocusable(true);
        mPopWinHandle.getPopupView().setFocusableInTouchMode(true);
        mPopWinHandle.getPopupView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && (keyCode == KeyEvent.KEYCODE_MENU)
                        && (mPopWinHandle.getPopupWindow().isShowing())) {
                    dismiss();
                }
                return false;
            }
        });
    }

    /**
     * 通过toolbar左上角的overflow打开
     *
     * @param menu    菜单
     * @param iconRes overflow图标
     */
    public void createOverflowStyle(Menu menu, int iconRes) {
        MenuItem item = menu.add(0, OVERFLOW_ID, 0, "overflow");
        item.setIcon(iconRes);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                show();
                return false;
            }
        });
    }

    /**
     * 设置菜单的动画效果
     */
    public void setAnimationStyle(int animationStyle) {
        mPopWinHandle.getPopupWindow().setAnimationStyle(animationStyle);
    }

    /**
     * 获得指定条目
     *
     * @param index 条目下标
     * @return view
     */
    public View getItemView(int index) {
        if (index > mItemViews.size() - 1 || index < 0) {
            return null;
        }
        return mItemViews.get(index);
    }

    /**
     * 获得指定条目
     *
     * @return List<View>
     */
    public List<View> getItemViews() {
        return mItemViews;
    }

    /**
     * dp转px
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    /**
     * 获得状态栏的高度
     */
    private int getStatusHeight() {

        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = mContext.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }
}
