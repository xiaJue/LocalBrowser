package com.xiajue.browser.localwebbrowser.model.manager;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.SystemClock;
import android.text.ClipboardManager;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.xiajue.browser.localwebbrowser.R;
import com.xiajue.browser.localwebbrowser.model.Config;
import com.xiajue.browser.localwebbrowser.model.bean.HomeListBean;
import com.xiajue.browser.localwebbrowser.model.database.DatabaseDao;
import com.xiajue.browser.localwebbrowser.model.utils.L;
import com.xiajue.browser.localwebbrowser.model.utils.OpenFileUtils;
import com.xiajue.browser.localwebbrowser.view.activity.viewInterface.IHomeView;

import java.io.File;

/**
 * Created by Moing_Admin on 2017/10/23.
 * <p>
 * HomeActivity中的一些事件触发的操作
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

    private String[] TYPES = {"apk"};

    /**
     * 当条目点击
     */
    public void selectItem(int position) {
        mIHomeView.getAdapter().closeAllItems();
        HomeListBean bean = mIHomeView.getList().get(position);
        //可以过滤一些不希望打开的类型:TYPES
        for (int i = 0; i < TYPES.length; i++) {
            String name = bean.getName().toLowerCase();
            int index = name.lastIndexOf('.') + 1;
            if (index <= 0 || index >= name.length()) {
                return;
            }
            String end = name.substring(index, name.length());
//            L.e("path=" + name);
//            L.e("end=" + end);
            if (end.equals(TYPES[i])) {
                //不打开时调用此方法
                doSomeThings(bean.getAbsPath(), TYPES[i]);
                return;
            }
        }
        //打开时
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
    private void doSomeThings(String path, String end) {
        switch (end) {
            case "apk":
                OpenFileUtils.openFile(mContext, new File(path));
                break;
        }
    }

    /**
     * 当条目移除
     */
    public void removeItem(HomeListBean bean) {

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
                        .WEB_ABOUT_BLANK)) {
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
     * 关闭网页
     */
    public void menuCloseWeb() {
        if (mIHomeView.getViewPager().getCurrentItem() != 1) {
            return;
        }
        String url = mIHomeView.getWebView().getUrl();
        if (url == null || url.isEmpty()) {
            Toast.makeText(mContext, R.string.muYou, Toast.LENGTH_SHORT).show();
            return;
        }
        mIHomeView.getWebFragment().closeLoad();
    }

    /**
     * 复制链接
     */
    public void menuCopyLink() {
        if (mIHomeView.getViewPager().getCurrentItem() != 1) {
            return;
        }
        String url = mIHomeView.getWebView().getUrl();
        if (url == null || url.isEmpty()) {
            Toast.makeText(mContext, R.string.muYou, Toast.LENGTH_SHORT).show();
            return;
        }
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
        if (mIHomeView.getViewPager().getCurrentItem() != 1) {
            return;
        }
        String url = mIHomeView.getWebView().getUrl();
        if (url == null || url.isEmpty()) {
            Toast.makeText(mContext, R.string.muYou, Toast.LENGTH_SHORT).show();
            return;
        }
        if (url.contains(Config.WEB_ABOUT_BLANK)) {
            return;
        }
        OpenFileUtils.openLinkFromBrowser(((Activity) mContext), url);
    }

    /**
     * 保存网页
     */
    public void menuSaveWeb() {
        if (mIHomeView.getViewPager().getCurrentItem() != 1) {
            return;
        }
        String url = mIHomeView.getWebView().getUrl();
        if (url == null || url.isEmpty()) {
            Toast.makeText(mContext, R.string.muYou, Toast.LENGTH_SHORT).show();
            return;
        }
        if (mIHomeView.getWebView().getUrl().contains(Config.WEB_ABOUT_BLANK)) {
            return;
        }
        //获得保存位置
       new Thread(new Runnable() {
           @Override
           public void run() {
               String fileName = Settings.getFileSavePath(mContext,
                       mIHomeView.getWebView().getTitle(), ".mht");
               if (!new File(fileName).exists()) {
                   try {
                       //保存离线网页
                       mIHomeView.getWebView().saveWebArchive(fileName);
                       if (new File(fileName).exists()) {
                           Toast.makeText(mContext, R.string.save_local_web_success, Toast
                                   .LENGTH_SHORT).show();
                           // 添加到侧滑菜单listView中
                           HomeListBean bean = new HomeListBean(new File(fileName).getName(), fileName,
                                   SystemClock.currentThreadTimeMillis(), false, false);
                           //添加到菜单列表
                           addToDrawerLayoutList(bean, true);
                           mIHomeView.getAdapter().notifyDataSetChanged();
                       }else{
                           Toast.makeText(mContext, R.string.save_local_web_failure, Toast
                                   .LENGTH_SHORT)
                                   .show();
                       }
                   } catch (Resources.NotFoundException e) {
                       Toast.makeText(mContext, R.string.save_local_web_failure, Toast
                               .LENGTH_SHORT)
                               .show();
                   }
               } else {
                   Toast.makeText(mContext, R.string.save_local_web_exist, Toast.LENGTH_SHORT)
                           .show();
               }
           }
       });
    }

    /**
     * 退出
     */
    public void menuExit() {
        mIHomeView.getActivity().finish();
    }

}
