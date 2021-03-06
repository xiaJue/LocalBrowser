package com.xiajue.browser.localwebbrowser.model.database.greenUtils;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.xiajue.browser.localwebbrowser.model.bean.CollectionBean;
import com.xiajue.browser.localwebbrowser.model.bean.HistoryBean;
import com.xiajue.browser.localwebbrowser.model.bean.HomeListBean;
import com.xiajue.browser.localwebbrowser.model.bean.RemoveBean;

import com.xiajue.browser.localwebbrowser.model.database.greenUtils.CollectionBeanDao;
import com.xiajue.browser.localwebbrowser.model.database.greenUtils.HistoryBeanDao;
import com.xiajue.browser.localwebbrowser.model.database.greenUtils.HomeListBeanDao;
import com.xiajue.browser.localwebbrowser.model.database.greenUtils.RemoveBeanDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig collectionBeanDaoConfig;
    private final DaoConfig historyBeanDaoConfig;
    private final DaoConfig homeListBeanDaoConfig;
    private final DaoConfig removeBeanDaoConfig;

    private final CollectionBeanDao collectionBeanDao;
    private final HistoryBeanDao historyBeanDao;
    private final HomeListBeanDao homeListBeanDao;
    private final RemoveBeanDao removeBeanDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        collectionBeanDaoConfig = daoConfigMap.get(CollectionBeanDao.class).clone();
        collectionBeanDaoConfig.initIdentityScope(type);

        historyBeanDaoConfig = daoConfigMap.get(HistoryBeanDao.class).clone();
        historyBeanDaoConfig.initIdentityScope(type);

        homeListBeanDaoConfig = daoConfigMap.get(HomeListBeanDao.class).clone();
        homeListBeanDaoConfig.initIdentityScope(type);

        removeBeanDaoConfig = daoConfigMap.get(RemoveBeanDao.class).clone();
        removeBeanDaoConfig.initIdentityScope(type);

        collectionBeanDao = new CollectionBeanDao(collectionBeanDaoConfig, this);
        historyBeanDao = new HistoryBeanDao(historyBeanDaoConfig, this);
        homeListBeanDao = new HomeListBeanDao(homeListBeanDaoConfig, this);
        removeBeanDao = new RemoveBeanDao(removeBeanDaoConfig, this);

        registerDao(CollectionBean.class, collectionBeanDao);
        registerDao(HistoryBean.class, historyBeanDao);
        registerDao(HomeListBean.class, homeListBeanDao);
        registerDao(RemoveBean.class, removeBeanDao);
    }
    
    public void clear() {
        collectionBeanDaoConfig.clearIdentityScope();
        historyBeanDaoConfig.clearIdentityScope();
        homeListBeanDaoConfig.clearIdentityScope();
        removeBeanDaoConfig.clearIdentityScope();
    }

    public CollectionBeanDao getCollectionBeanDao() {
        return collectionBeanDao;
    }

    public HistoryBeanDao getHistoryBeanDao() {
        return historyBeanDao;
    }

    public HomeListBeanDao getHomeListBeanDao() {
        return homeListBeanDao;
    }

    public RemoveBeanDao getRemoveBeanDao() {
        return removeBeanDao;
    }

}
