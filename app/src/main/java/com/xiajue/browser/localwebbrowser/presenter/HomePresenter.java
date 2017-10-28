package com.xiajue.browser.localwebbrowser.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tencent.smtt.sdk.WebView;
import com.xiajue.browser.localwebbrowser.R;
import com.xiajue.browser.localwebbrowser.adapter.HomeListAdapter;
import com.xiajue.browser.localwebbrowser.model.bean.CollectionBean;
import com.xiajue.browser.localwebbrowser.model.bean.HomeListBean;
import com.xiajue.browser.localwebbrowser.model.bean.RemoveBean;
import com.xiajue.browser.localwebbrowser.model.database.DatabaseDao;
import com.xiajue.browser.localwebbrowser.model.manager.DialogManager;
import com.xiajue.browser.localwebbrowser.model.manager.DownloadManager;
import com.xiajue.browser.localwebbrowser.model.manager.HomeEventManager;
import com.xiajue.browser.localwebbrowser.model.manager.PopupManager;
import com.xiajue.browser.localwebbrowser.model.manager.Settings;
import com.xiajue.browser.localwebbrowser.model.manager.SettingsUtils;
import com.xiajue.browser.localwebbrowser.model.utils.L;
import com.xiajue.browser.localwebbrowser.model.utils.OpenFileUtils;
import com.xiajue.browser.localwebbrowser.model.utils.SPUtils;
import com.xiajue.browser.localwebbrowser.view.activity.viewInterface.IHomeView;

import java.io.File;
import java.util.List;

import ru.bartwell.exfilepicker.ExFilePicker;
import ru.bartwell.exfilepicker.data.ExFilePickerResult;

import static com.xiajue.browser.localwebbrowser.model.manager.DownloadManager.getFileName;

/**
 * xiaJue 2017/9/15创建
 */
public class HomePresenter {
    private IHomeView mIHomeView;
    private Context mContext;
    private PopupManager mPopupManager;
    private DialogManager mDialogManager;
    private DatabaseDao mDatabaseDao;
    private HomeEventManager mEventManager;

    private static final int EX_FILE_PICKER_RESULT = 250;
    private String list_path;

    public HomePresenter(IHomeView mIHomeView) {
        this.mIHomeView = mIHomeView;
        mContext = (Context) mIHomeView;
        mPopupManager = new PopupManager(mContext);
        mDatabaseDao = DatabaseDao.getInstance(mContext);
        mDialogManager = new DialogManager();
        list_path = SPUtils.getInstance(mContext).getString("list_path");
        //init imageLoader
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault
                (mContext);
        ImageLoader.getInstance().init(configuration);

        HomeEventManager.initializeInstance(mContext, mIHomeView);
//        mEventManager.initializeWebView(mIHomeView.getWebView());
        mEventManager = HomeEventManager.getInstance();
    }

    /**
     * 各种点击事件--click
     */
    public void onClickListener(View v) {
        switch (v.getId()) {
            case R.id.home_collection_see:
                //更新列表-只显示收藏的条目
                showCollection();
                break;
            case R.id.home_remove_see:
                //更新列表-只显示移除的条目
                showRemove();
                break;
            case R.id.home_all_see:
                //更新列表-显示所有条目
                mIHomeView.getActivity().isNeedCloseSearch(true);
                break;
            case R.id.home_search:
                //隐藏搜索按钮，显示搜索输入框
                showSearchEditText();
                break;
            case R.id.home_listPath_tv:
                //打开一个文件选择器选择目录
                openDirSelect();
                break;
            case R.id.home_home_iv:
                mIHomeView.getViewPager().setCurrentItem(0);
                break;
            case R.id.home_web_iv:
                mIHomeView.getViewPager().setCurrentItem(1);
                break;
            case R.id.home_about_iv:
                mIHomeView.getViewPager().setCurrentItem(2);
                break;
            case R.id.home_toolbar_text:
                Toast.makeText(mContext, mIHomeView.getActivity().getToolbarTitle(), Toast
                        .LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * click-显示收藏列表
     */
    private void showCollection() {
        //更新列表-只显示收藏的条目
        mIHomeView.getActivity().setDrawerListShowAllButton();
        List collections = mDatabaseDao.select(new CollectionBean());
        mIHomeView.getList().clear();
        mIHomeView.getList().addAll(mDatabaseDao.transformBean(collections, DatabaseDao
                .DATA_HOME));
        mIHomeView.getAdapter().notifyDataSetChanged();
    }

    /**
     * click-显示“移除”列表
     */
    private void showRemove() {
        //更新列表-只显示移除的条目
        mIHomeView.getActivity().setDrawerListShowAllButton();
        List removes = mDatabaseDao.select(new RemoveBean());
        mIHomeView.getList().clear();
        mIHomeView.getList().addAll(mDatabaseDao.transformBean(removes, DatabaseDao
                .DATA_HOME));
        mIHomeView.getAdapter().notifyDataSetChanged();
    }

    /**
     * click-显示搜索输入框
     */
    private void showSearchEditText() {
        //隐藏搜索按钮，显示搜索输入框
        mIHomeView.getActivity().isNeedCloseSearch(false);
        mIHomeView.getActivity().getSearchEdit().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //对数据进行搜索-将搜索结果更新到列表中
                String text = mIHomeView.getActivity().getSearchEdit().getText().toString();
                List list = mDatabaseDao.contains(new HomeListBean(text, "", 0, false,
                        false));//查询条件只需要name即可
                mIHomeView.getList().clear();
                mIHomeView.getList().addAll(list);
                mIHomeView.getAdapter().notifyDataSetChanged();//刷新
            }
        });
    }

    /**
     * click-打开选择目录
     */
    public void openDirSelect() {
        //打开一个目录选择器
        ExFilePicker exFilePicker = new ExFilePicker();
//        exFilePicker.setShowOnlyExtensions(Config.EXTENSION_STRING);
        exFilePicker.setCanChooseOnlyOneItem(true);
        exFilePicker.setChoiceType(ExFilePicker.ChoiceType.DIRECTORIES);
        exFilePicker.setNewFolderButtonDisabled(true);
        exFilePicker.setQuitButtonEnabled(true);
        String list_path = SPUtils.getInstance(mContext).getString("list_path");
        if (list_path != null) {
            exFilePicker.setStartDirectory(list_path);
        }
        exFilePicker.start((Activity) mContext, EX_FILE_PICKER_RESULT);
    }

    /**
     * click-activity Result 选择目录返回后
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == EX_FILE_PICKER_RESULT) {
            ExFilePickerResult result = ExFilePickerResult.getFromIntent(data);
            if (result != null && result.getCount() > 0) {
                // Here is object contains selected files names and path
                String name = result.getNames().get(0);
                final String path = result.getPath() + name;
                //更新数据到列表
                list_path = path;
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        if (SettingsUtils.isDonLoad(mContext, true)) {
                            mDatabaseDao.deleteAll(new HomeListBean());//删除所有数据
                            put2listFromFiles(true);//从file-list中获取数据-存储到数据库中
                        } else {
                            put2listFromFiles();//从file-list中获取数据-存储到数据库中
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        mIHomeView.getActivity().setPathText(path, 0);
                        mIHomeView.getAdapter().notifyDataSetChanged();
                        mIHomeView.getActivity().setListProgressBarVisible(View.GONE);
                        mIHomeView.getActivity().setDrawerListSize(View.GONE, mIHomeView
                                .getListView().getFirstVisiblePosition() + "/" + mIHomeView
                                .getList().size());
                    }
                }.execute();
                mIHomeView.getActivity().setListProgressBarVisible(View.VISIBLE);
                mIHomeView.getActivity().setPathText(null, R.string.data_loading);
                SPUtils.getInstance(mContext).put("list_path", path);
            }
        }
    }

    /**
     * click-activity Result 从文件目录列表中加载Home数据到list
     */
    public void put2listFromFiles(boolean... isClear) {
        if (isClear.length > 0 && isClear[0]) {
//            L.e("clear");
            mIHomeView.getList().clear();
        }
        File file = new File(list_path);
        if (list_path == null || !file.exists()) {
            return;
        }
        String[] lists = file.list();
        for (int i = 0; i < lists.length; i++) {
            //绝对路径
            String path = list_path + File.separator + lists[i];
            //跳过文件夹
            File theFile = new File(path);
            if (!theFile.isFile()) {
                continue;
            }
            //最后访问时间
            long modified = theFile.lastModified();
            //创建和初始化Bean
            HomeListBean bean = new HomeListBean(lists[i], path, modified, false, false);
            //是否被收藏
            bean.isCollection = mDatabaseDao.isExist(bean);
            //是否被remove
            bean.isRemove = mDatabaseDao.isExist(bean, DatabaseDao.DATA_REMOVE);//是否移除
            mEventManager.addToDrawerLayoutList(bean, true);
        }
    }

    /**
     * popupMenu选择时
     */
    public void onItemSelect(int selectIndex) {
        switch (selectIndex) {
            case 0:
                //关闭网页
                mEventManager.menuCloseWeb();
                break;
            case 1:
                mEventManager.menuCopyLink();
                break;
            case 2:
                //从浏览器打开链接
                mEventManager.menuBrowserOpen();
                break;
            case 3:
                //保存离线网页
                mEventManager.menuSaveWeb();
                break;
            case 4:
                mEventManager.menuExit();
                break;
        }
    }

    /**
     * menu-菜单选择时
     */
    public void onMenuItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_home_refresh:
                //刷新页面
                if (mIHomeView.getViewPager().getCurrentItem() == 1)
                    mIHomeView.getWebView().reload();
                break;
        }
    }

    /**
     * right-item-click当listView条目右边的隐藏按钮被点击时
     */
    public HomeListAdapter.OnRightButtonClickListener onRightButtonClick() {
        return new HomeListAdapter.OnRightButtonClickListener() {
            @Override
            public void onRemove(final int position) {
                //显示一个普通的dialog提示是否移除
                showRemoveOperation(position);
            }

            @Override
            public void onDelete(final int position) {
                //显示一个普通的dialog提示是否永久删除
                showDeleteOperation(position);
            }

            @Override
            public void onCollection(int position) {
                //收藏或取消收藏
                doCollection(position);
            }
        };
    }

    /**
     * right-item-click 显示是否移除的dialog
     */
    private void showRemoveOperation(final int position) {
        final HomeListBean bean = mIHomeView.getList().get(position);
        mDialogManager.showInquiry(mContext, !bean.isRemove ? mContext.getString(R.string
                .is_remove) : mContext.getString(R.string.is_add2all_list), new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mEventManager.removeItem(bean);
                        removeListViewItem(position);
                    }
                });
    }

    /**
     * right-item-click 移除列表中的条目
     *
     * @param position position
     */
    private void removeListViewItem(int position) {
        mIHomeView.getList().remove(position);
        mIHomeView.getAdapter().notifyDataSetChanged();
    }

    /**
     * right-item-click显示是否删除的dialog
     */
    private void showDeleteOperation(final int position) {
        mDialogManager.showInquiry(mContext, mContext.getString(R.string.is_delete), new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        HomeListBean bean = mIHomeView.getList().get(position);
                        mEventManager.deleteItem(bean);
                        removeListViewItem(position);
                    }
                });
    }

    /**
     * right-item-click将内容收藏或取消收藏
     */
    private void doCollection(int position) {
        HomeListBean bean = mIHomeView.getList().get(position);
        mEventManager.collectionItem(bean);
    }

    /**
     * item-click 列表条目点击
     */
    public AdapterView.OnItemClickListener onItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mEventManager.selectItem(position);
            }
        };
    }

    /**
     * long-click-web 长按webView
     */
    public View.OnLongClickListener onLongClickWebView() {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final WebView.HitTestResult result = mIHomeView.getWebView().getHitTestResult();
                int type = result.getType();
                if (type == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE ||
                        type == WebView.HitTestResult.IMAGE_TYPE) {
                    //长按保存图片的效果
                    longClickSaveImage(v, result);
                    return true;
                } else if (type == WebView.HitTestResult.SRC_ANCHOR_TYPE) {
                    //浏览器打开链接
                    mPopupManager.showLongClickLinkPopup(v, x, y, new PopupManager
                            .OnLinkPopupMenuClickListener() {
                        @Override
                        public void onOpenLink() {
                            String extra = result.getExtra();
                            OpenFileUtils.openLinkFromBrowser(((Activity) mContext), extra);
                        }
                    });
                    return true;
                }
                return false;
            }
        };
    }

    /**
     * long-click-web 长按弹出保存图片popupWindow
     */
    private void longClickSaveImage(View v, final WebView.HitTestResult result) {
        //图片类型:弹出保存图片的popup
        mPopupManager.showLongClickImagePopup(v, x, y, new PopupManager
                .OnImagePopupMenuClickListener() {
            @Override
            public void onSaveImage() {
                //保存图片到本地
                L.e(result.getExtra());
                String path = result.getExtra();
                //下载图片
                DownloadManager.download(Settings.getImageSavePath(mContext, getFileName
                        (path), ""), path, new DownloadManager.DownloadCallback() {
                    @Override
                    public void success(File file) {
                        //下载成功
                        ((Activity) mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, R.string.save_success, Toast
                                        .LENGTH_SHORT).show();
                            }
                        });
                        //通知系统图库更新
                        mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                                Uri.parse
                                        ("file://" + file)));
                    }

                    @Override
                    public void failure() {
                        //下载失败
                        ((Activity) mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, R.string.save_failure, Toast
                                        .LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
    }

    private int x, y;//webView长按时的位置

    /**
     * long-click-web 触摸webView-获取-webView长按时的位置
     */
    public View.OnTouchListener onTouchEvent() {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                x = (int) event.getX();
                y = (int) event.getY();
                return false;
            }
        };
    }

    /**
     * 从数据库加载Home数据到list
     */
    public void put2listFromDatabase(List list) {
        List homes = mDatabaseDao.select(new HomeListBean());
        list.clear();
        list.addAll(homes);
        //侧滑菜单的显示设置 是否为空/显示数量
        int visibility = list.size() > 0 ? View.GONE : View.VISIBLE;
        mIHomeView.getActivity().setDrawerListSize(visibility, mIHomeView.getListView()
                .getFirstVisiblePosition() + "/" + mIHomeView
                .getList()
                .size());
    }

    /**
     * 显示无网页时的首页
     */
    public void showHome() {
        mIHomeView.getViewPager().setCurrentItem(0);
    }

    /**
     * 销毁-防止内存泄漏
     */
    public void destroy() {
        mIHomeView.getHomeFragment().cancelLoadImage();
    }

}
