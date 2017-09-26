package com.xiajue.browser.localwebbrowser;

import android.test.AndroidTestCase;

import com.xiajue.browser.localwebbrowser.model.bean.HomeListBean;
import com.xiajue.browser.localwebbrowser.model.database.DatabaseDao;

/**
 * xiaJue 2017/9/18创建
 */
public class DataTest extends AndroidTestCase {
    public void testAdd() {
        DatabaseDao databaseDao = DatabaseDao.getInstance(getContext());
        HomeListBean bean = new HomeListBean("home name", "home absPath",true,true);
        databaseDao.delete(bean);
    }
}
