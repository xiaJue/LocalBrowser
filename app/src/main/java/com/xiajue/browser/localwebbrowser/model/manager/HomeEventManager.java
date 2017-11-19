package com.xiajue.browser.localwebbrowser.model.manager;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.widget.Toolbar;
import android.text.ClipboardManager;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.xiajue.browser.localwebbrowser.R;
import com.xiajue.browser.localwebbrowser.model.Config;
import com.xiajue.browser.localwebbrowser.model.bean.HomeListBean;
import com.xiajue.browser.localwebbrowser.model.database.DatabaseDao;
import com.xiajue.browser.localwebbrowser.model.utils.L;
import com.xiajue.browser.localwebbrowser.model.utils.OpenFileUtils;
import com.xiajue.browser.localwebbrowser.model.utils.StringUtils;
import com.xiajue.browser.localwebbrowser.view.activity.viewInterface.IHomeView;

import java.io.File;

/**
 * Created by Moing_Admin on 2017/10/23.
 * <p>
 * HomeActivity中的一些事件触发的操作
 * 此类仅为了把一些需要经常修改的操作集中起来，方便修改，扩展以及查找BUG。
 * 单例模式纯粹为省事
 */

public class HomeEventManager {
    private Context mContext;
    private IHomeView mIHomeView;
    private DatabaseDao mDatabaseDao;

    public static HomeEventManager manager;

    public static void initializeInstance(Context context, IHomeView iHomeView) {
        if (manager == null) {
            synchronized (HomeEventManager.class) {
                if (manager == null) {
                    manager = new HomeEventManager(context, iHomeView);
                }
            }
        }
    }

    public static HomeEventManager getInstance() {
        return manager;
    }

    private HomeEventManager(Context context, IHomeView iHomeView) {
        this.mContext = context;
        this.mIHomeView = iHomeView;
        mDatabaseDao = DatabaseDao.getInstance(context);
    }

    private String[] TYPES = {"html", "htm", "mht", "jpg", "jpeg", "png", "gif", "txt", "java",
            "c", "xml"};

    /**
     * 当条目点击
     */
    public void selectItem(final int position) {
        mIHomeView.getAdapter().closeAllItems();
        final HomeListBean bean = mIHomeView.getList().get(position);
        //打开时
        //如果文件不存在、提示:
        if (!new File(bean.getAbsPath()).exists()) {
            DialogManager.showInquiry(mContext, mContext.getString(R.string.file_not_exist),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mDatabaseDao.delete(bean);// 从home数据表删除
                            mDatabaseDao.delete(bean, DatabaseDao.DATA_COLLECTION);// 从收藏数据表删除
                            mDatabaseDao.delete(bean, DatabaseDao.DATA_REMOVE);//从移除数据表删除
                            mIHomeView.getList().remove(position);
                            mIHomeView.getAdapter().notifyDataSetChanged();
                        }
                    });
            return;
        }
        //可以过滤只希望打开的类型:TYPES
        boolean isLost = false;
        for (int i = 0; i < TYPES.length; i++) {
            String name = bean.getName().toLowerCase();
            int index = name.lastIndexOf('.') + 1;
            if (index <= 0 || index >= name.length()) {
                return;
            }
            String end = name.substring(index, name.length());
            if (end.equals(TYPES[i])) {
                isLost = true;
                break;
            }
        }
        if (!isLost) {
            //不打开时调用此方法
            doSomeThings(bean.getAbsPath());
            return;
        }
        //正常打开
        mIHomeView.getWebView().loadUrl("file://" + bean.getAbsPath());
        L.e(mIHomeView.getList().get(position).getAbsPath());
        mIHomeView.getDrawerLayout().closeDrawer(Gravity.START);
        setToolbarTitle(bean.getName());
        if (!mIHomeView.getWebView().isShown()) {
            mIHomeView.getWebView().setVisibility(View.VISIBLE);
        }
        mIHomeView.getViewPager().setCurrentItem(1);
    }

    /**
     * 过滤掉的类型
     */
    private void doSomeThings(String path) {
        OpenFileUtils.openFile(mContext, new File(path));
    }

    /**
     * 当条目移除
     */
    public void removeItem(HomeListBean bean) {
        mIHomeView.getAdapter().closeAllItems();
        if (!bean.isRemove) {
            //未remove
            if (!mDatabaseDao.isExist(bean, DatabaseDao.DATA_REMOVE)) {
                bean.isRemove = true;
                //添加到remove数据表
                mDatabaseDao.add(bean, DatabaseDao.DATA_REMOVE);
                //从home列表删除
                mDatabaseDao.delete(bean);
            }
        } else {
            //已remove
            if (mDatabaseDao.isExist(bean, DatabaseDao.DATA_REMOVE)) {
                //从remove数据表移除并添加到home数据表
                bean.isRemove = false;
                mDatabaseDao.delete(bean, DatabaseDao.DATA_REMOVE);
                mDatabaseDao.add(bean);
            }
        }
    }

    /**
     * 当删除条目时
     */
    public void deleteItem(HomeListBean bean) {
        String absPath = bean.getAbsPath();//文件路径
        boolean delete = new File(absPath).delete();//删除文件
        Toast.makeText(mContext, delete ? mContext.getString(R.string.delete_success) :
                mContext.getString(R.string.delete_failure), Toast
                .LENGTH_SHORT)
                .show();//显示土司
//        if (delete) {
        //无论删除成功与否都从数据库删除数据
        mDatabaseDao.delete(bean);// 从home数据表删除
        mDatabaseDao.delete(bean, DatabaseDao.DATA_COLLECTION);// 从收藏数据表删除
        mDatabaseDao.delete(bean, DatabaseDao.DATA_REMOVE);//从移除数据表删除
//        }
        mIHomeView.getAdapter().closeAllItems();
    }

    /**
     * 当条目被收藏
     */
    public void collectionItem(HomeListBean bean) {
        if (mDatabaseDao.isExist(bean, DatabaseDao.DATA_COLLECTION)) {
            //显示已取消收藏并改变图标
            mDatabaseDao.delete(bean, DatabaseDao.DATA_COLLECTION);
            Toast.makeText(mContext, R.string.cancel_collection, Toast.LENGTH_SHORT).show();
            bean.isCollection = false;
        } else {
            //显示已收藏并改变图标
            mDatabaseDao.add(bean, DatabaseDao.DATA_COLLECTION);
            Toast.makeText(mContext, R.string.collection, Toast.LENGTH_SHORT).show();
            bean.isCollection = true;
        }
    }

    /**
     * 添加条目到侧滑菜单列表
     *
     * @param bean       item
     * @param toDatabase 是否存入数据库
     */
    public void addToDrawerLayoutList(HomeListBean bean, boolean toDatabase) {
        //add to listView
        if (!bean.isRemove) {
            mIHomeView.getList().add(bean);
        }
        //add to database
        if (toDatabase && !mDatabaseDao.isExist(bean)) {
            mDatabaseDao.add(bean);
        }
    }

    /**
     * 设置toolbar的标题
     */
    public void setToolbarTitle(String title, boolean... donSet) {
        //如果设置了第二个参数,则不执行条件语句
        if (donSet.length <= 0) {
            if (mIHomeView.getWebView() != null && mIHomeView.getWebView().getUrl() != null) {
                String url = mIHomeView.getWebView().getUrl().trim();
                if (url.isEmpty() || url.contains(Config.WEB_ABOUT_BLANK) || title.contains(Config
                        .WEB_ABOUT_BLANK) || url.equals(Config.getSetWebViewBgColorString
                        (mContext)) || title.equals(Config.getSetWebViewBgColorString
                        (mContext))) {
                    title = mContext.getString(R.string.web);
                }
            }
        }
        mIHomeView.getActivity().setToolbarTitle(title);
    }

    /**
     * 菜单 方法
     */

    /**
     * 刷新
     */
    public void menuRefresh() {
        if (isLost()) return;
        mIHomeView.getWebView().reload();
    }

    /**
     * 关闭网页
     */
    public void menuCloseWeb() {
        if (isLost()) return;
        mIHomeView.getWebFragment().closeLoad();
    }

    /**
     * 复制链接
     */
    public void menuCopyLink() {
        if (isLost()) return;
        String url = mIHomeView.getWebView().getUrl();
        //设置剪切板文字
        ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context
                .CLIPBOARD_SERVICE);
        cm.setText(url);
        Toast.makeText(mContext, R.string.copy_success, Toast.LENGTH_SHORT).show();
    }

    /**
     * 从浏览器打开
     */
    public void menuBrowserOpen() {
        if (isLost()) return;
        String url = mIHomeView.getWebView().getUrl();
        try {
            OpenFileUtils.openLinkFromBrowser(((Activity) mContext), url);
        } catch (Exception e) {
            Toast.makeText(mContext, mContext.getString(R.string.url_error) + url, Toast
                    .LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * 保存网页
     */
    public void menuSaveWeb() {
        if (isLost()) return;
        if (mIHomeView.getWebView().getUrl().contains("file://")) {
            //如果已经是离线网页
            return;
        }
        //获得保存位置
        String fileName = Settings.getFileSavePath(mContext,
                mIHomeView.getWebView().getTitle(), ".mht");
        if (!new File(fileName).exists()) {
            try {
                saveWebFile(fileName);
            } catch (Resources.NotFoundException e) {
                Toast.makeText(mContext, R.string.save_local_web_failure, Toast
                        .LENGTH_SHORT)
                        .show();
            }
        } else {
            DialogManager.showInquiry(mContext, mContext.getString(R.string.file_exist),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String fileName = Settings.getFileSavePath(mContext,
                                    mIHomeView.getWebView().getTitle() + StringUtils.formatDate
                                            (System.currentTimeMillis()), ".mht");
                            saveWebFile(fileName);
                        }
                    });
        }
    }

    private void saveWebFile(String fileName) {
        //保存离线网页
        mIHomeView.getWebView().saveWebArchive(fileName);
        // 添加到侧滑菜单listView中
        HomeListBean bean = new HomeListBean(new File(fileName).getName(), fileName,
                SystemClock.currentThreadTimeMillis(), false, false);
        //添加到菜单列表
        addToDrawerLayoutList(bean, true);
        mIHomeView.getAdapter().notifyDataSetChanged();
        Toast.makeText(mContext, R.string.save_local_web_success, Toast
                .LENGTH_SHORT).show();
    }

    /**
     * 退出
     */
    public void menuExit() {
        mIHomeView.getActivity().finish();
    }

    private boolean isFull = false;

    /**
     * 全屏浏览网页
     */
    public void menuFullScreen() {
        if (isLost()) return;
        final Toolbar toolbar = mIHomeView.getActivity().getToolbar();
        final LinearLayout bottomView = mIHomeView.getActivity().getBottomView();
        if (!isFull) {
            //设置“全屏”--toolbar和bottomView分别向上移动隐藏和向下移动隐藏
            isFull = true;
            //toolbar animation
            Animation toUpHide = AnimationUtils.loadAnimation(mContext, R.anim.to_up_hide);
            toUpHide.setFillAfter(true);
            toolbar.startAnimation(toUpHide);
            toUpHide.setAnimationListener(new MyAnimationLisenter() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    toolbar.clearAnimation();//不增加此句代码可能会发生异常
                    toolbar.setVisibility(View.GONE);
                }
            });
            //bottom animation
            Animation toDownHide = AnimationUtils.loadAnimation(mContext, R.anim.to_down_hide);
            toDownHide.setFillAfter(true);
            bottomView.startAnimation(toDownHide);
            toDownHide.setAnimationListener(new MyAnimationLisenter() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    bottomView.clearAnimation();//不增加此句代码会发生异常
                    bottomView.setVisibility(View.GONE);
                }
            });
            //隐藏状态栏
            //此方法隐藏状态栏，用户下拉显示后会自动消失
            mIHomeView.getActivity().getWindow().addFlags(WindowManager.LayoutParams
                    .FLAG_FULLSCREEN);
            //此方法不会自动消失
//            mIHomeView.getActivity().getRootView().setSystemUiVisibility(View
//                    .SYSTEM_UI_FLAG_FULLSCREEN);
            //取消viewPager滑动切换
            mIHomeView.getViewPager().setScroll(false);
            mIHomeView.getWebFragment().getShowUnFull().setVisibility(View.VISIBLE);
        }
    }

    /**
     * 取消全屏
     */
    public void unFullScreen() {
        Toolbar toolbar = mIHomeView.getActivity().getToolbar();
        View bottomView = mIHomeView.getActivity().getBottomView();
        if (isFull) {
            //反向操作
            isFull = false;
            //bottom animation
            Animation toUpShow = AnimationUtils.loadAnimation(mContext, R.anim.to_up_show);
            toUpShow.setFillAfter(true);
            bottomView.setVisibility(View.VISIBLE);
            bottomView.startAnimation(toUpShow);
            //toolbar animation
            Animation toDownShow = AnimationUtils.loadAnimation(mContext, R.anim.to_down_show);
            toDownShow.setFillAfter(true);
            toolbar.setVisibility(View.VISIBLE);
            toolbar.startAnimation(toDownShow);
            //显示状态栏
//            mHideAsyncTask.cancel(true);
            mIHomeView.getActivity().getWindow().clearFlags(WindowManager.LayoutParams
                    .FLAG_FULLSCREEN);
//            mIHomeView.getActivity().getRootView().setSystemUiVisibility(View
//                    .SYSTEM_UI_FLAG_VISIBLE);
            //恢复viewPager滑动切换
            mIHomeView.getViewPager().setScroll(true);
            mIHomeView.getWebFragment().getShowUnFull().setVisibility(View.GONE);
        }
    }

    public boolean showUnFullButton() {
        final View button = mIHomeView.getWebFragment().getBottomRoot();
        final View showUnFull = mIHomeView.getWebFragment().getShowUnFull();
        //如果在显示中则不显示
        if (button.isShown() || !isFull) {
            return false;
        }
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim
                .activity_enter_anim);
        button.setVisibility(View.VISIBLE);
        button.startAnimation(animation);
        //若干毫秒后隐藏unFull按钮
        if (isFull) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        Thread.sleep(Config.HIDE_STATUS_TIME);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    //隐藏按钮
                    Animation animation = AnimationUtils.loadAnimation(mContext, R.anim
                            .activity_exit_anim);
                    animation.setFillAfter(true);
                    button.startAnimation(animation);
                    animation.setAnimationListener(new MyAnimationLisenter() {
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            if (isFull) {
                                button.clearAnimation();
                                button.setVisibility(View.GONE);
                                showUnFull.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
            }.execute();
        }
        return true;
    }

    /**
     * 一些菜单都需要跳过的情况
     */
    private boolean isLost() {
        if (mIHomeView.getViewPager().getCurrentItem() != 1) {
            return true;
        }
        String url = mIHomeView.getWebView().getUrl();
        if (url == null || url.isEmpty() || url.contains(Config
                .WEB_ABOUT_BLANK) || url.equals(Config.getSetWebViewBgColorString(mContext))) {
            Toast.makeText(mContext, R.string.muYou, Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    public void destroy() {
        mIHomeView = null;
        manager = null;
    }

    public void download(String url, String downloadPath) {
        MDownloadManager.getInstance(mContext).setNotify(true).downloadFile(url, downloadPath);
        //存入列表
        if (mIHomeView != null) {
            HomeListBean bean = new HomeListBean(StringUtils.getName(downloadPath), downloadPath,
                    System
                            .currentTimeMillis(), false, false);
            addToDrawerLayoutList(bean, true);
        }
    }
}
