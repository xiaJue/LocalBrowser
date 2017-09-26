package com.xiajue.browser.localwebbrowser.model.database;

import android.content.Context;

import com.xiajue.browser.localwebbrowser.model.bean.CollectionBean;
import com.xiajue.browser.localwebbrowser.model.bean.HomeListBean;
import com.xiajue.browser.localwebbrowser.model.bean.IBean;
import com.xiajue.browser.localwebbrowser.model.bean.RemoveBean;
import com.xiajue.browser.localwebbrowser.model.database.greenUtils.CollectionBeanDao;
import com.xiajue.browser.localwebbrowser.model.database.greenUtils.DaoMaster;
import com.xiajue.browser.localwebbrowser.model.database.greenUtils.DaoSession;
import com.xiajue.browser.localwebbrowser.model.database.greenUtils.HomeListBeanDao;
import com.xiajue.browser.localwebbrowser.model.database.greenUtils.RemoveBeanDao;

import java.util.ArrayList;
import java.util.List;

/**
 * xiaJue 2017/9/18创建
 */
public class DatabaseDao {
    public static final int DATA_HOME = 121;
    public static final int DATA_COLLECTION = 817;
    public static final int DATA_REMOVE = 467;

    public DatabaseDao(Context context) {
        DaoSession daoSession = DaoMaster.newDevSession(context, "web_list.db");

        mHomeDao = daoSession.getHomeListBeanDao();
        mCollectionBeanDao = daoSession.getCollectionBeanDao();
        mRemoveBeanDao = daoSession.getRemoveBeanDao();
    }

    private static DatabaseDao mDatabaseDao;

    public static DatabaseDao getInstance(Context context) {
        if (mDatabaseDao == null) {
            synchronized (DatabaseDao.class) {
                if (mDatabaseDao == null) {
                    mDatabaseDao = new DatabaseDao(context);
                }
            }
        }
        return mDatabaseDao;
    }

    //操作类
    private HomeListBeanDao mHomeDao;
    private CollectionBeanDao mCollectionBeanDao;
    private RemoveBeanDao mRemoveBeanDao;

    /**
     * 插入一条数据
     *
     * @param bean IBean的子类
     */
    public void add(IBean bean) {
        //if else 按照继承关系排序-否则子类无法执行
        if (bean instanceof CollectionBean) {
            mCollectionBeanDao.insert((CollectionBean) bean);
        } else if (bean instanceof RemoveBean) {
            mRemoveBeanDao.insert((RemoveBean) bean);
        } else if (bean instanceof HomeListBean) {
            mHomeDao.insert((HomeListBean) bean);
        }
    }

    /**
     * 插入一条数据
     *
     * @param bean IBean的子类
     * @param type 需要存入的类型-该方法会自动转换
     */
    public void add(IBean bean, int type) {
        switch (type) {
            case DATA_COLLECTION:
                add(new CollectionBean(bean.getName(), bean.getAbsPath(), bean.getLastModified()));
                break;
            case DATA_REMOVE:
                add(new RemoveBean(bean.getName(), bean.getAbsPath(), bean.getLastModified()));
                break;
            case DATA_HOME:
                add(new HomeListBean(bean.getName(), bean.getAbsPath(), bean.getLastModified(),
                        false, false));
                break;
        }
    }

    /**
     * 删除一条数据
     *
     * @param bean IBean的子类
     */
    public void delete(IBean bean) {
        //if else 按照继承关系排序-否则子类无法执行
        if (bean instanceof CollectionBean) {
            mCollectionBeanDao.delete((CollectionBean) bean);
        } else if (bean instanceof RemoveBean) {
            mRemoveBeanDao.delete((RemoveBean) bean);
        } else if (bean instanceof HomeListBean) {
            mHomeDao.delete((HomeListBean) bean);
        }
    }

    /**
     * 查询所有的数据
     *
     * @param bean IBean的子类
     * @param type 需要存入的类型-该方法会自动转换
     */
    public void delete(IBean bean, int type) {
        switch (type) {
            case DATA_COLLECTION:
                delete(new CollectionBean(bean.getName(), bean.getAbsPath(), bean.getLastModified
                        ()));
            case DATA_REMOVE:
                delete(new RemoveBean(bean.getName(), bean.getAbsPath(), bean.getLastModified()));
            case DATA_HOME:
                delete(new HomeListBean(bean.getName(), bean.getAbsPath(), bean.getLastModified()
                        , false, false));
        }
    }

    public void deleteAll(IBean bean) {
        //if else 按照继承关系排序-否则子类无法执行
        if (bean instanceof CollectionBean) {
            mCollectionBeanDao.deleteAll();
        } else if (bean instanceof RemoveBean) {
            mRemoveBeanDao.deleteAll();
        } else if (bean instanceof HomeListBean) {
            mHomeDao.deleteAll();
        }
    }

    /**
     * 查询所有的数据
     *
     * @param bean IBean 的子类,传入一个对象即可 如:new HomeListBean()
     * @return List
     */
    public List select(IBean bean) {
        //if else 按照继承关系排序-否则子类无法执行
        if (bean instanceof CollectionBean) {
            return mCollectionBeanDao.queryBuilder().build().list();
        } else if (bean instanceof RemoveBean) {
            return mRemoveBeanDao.queryBuilder().build().list();
        } else if (bean instanceof HomeListBean) {
            return mHomeDao.queryBuilder().build().list();
        }
        return null;
    }

    /**
     * 模糊查询
     */
    public List contains(IBean bean) {
        //if else 按照继承关系排序-否则子类无法执行
        if (bean instanceof CollectionBean) {
            return mCollectionBeanDao.queryBuilder().where(CollectionBeanDao.Properties.Name.like
                    ("%" + bean.getName() + "%")).build().list();
        } else if (bean instanceof RemoveBean) {
            return mRemoveBeanDao.queryBuilder().where(RemoveBeanDao.Properties.Name.like
                    ("%" + bean.getName() + "%")).build().list();
        } else if (bean instanceof HomeListBean) {
            return mHomeDao.queryBuilder().where(HomeListBeanDao.Properties.Name.like
                    ("%" + bean.getName() + "%")).build().list();
        }
        return null;
    }

    /**
     * 查询所有的数据
     *
     * @param bean IBean的子类
     * @param type 需要存入的类型-该方法会自动转换
     */
    public List select(IBean bean, int type) {
        switch (type) {
            case DATA_COLLECTION:
                return select(new CollectionBean(bean.getName(), bean.getAbsPath(), bean
                        .getLastModified()));
            case DATA_REMOVE:
                return select(new RemoveBean(bean.getName(), bean.getAbsPath(), bean
                        .getLastModified()));
            case DATA_HOME:
                return select(new HomeListBean(bean.getName(), bean.getAbsPath(), bean
                        .getLastModified(), false, false));
        }
        return null;
    }

    /**
     * 查询数据是否存在
     *
     * @param bean IBean子类
     * @return true of false
     */
    public boolean isExist(IBean bean) {
        //if else 按照继承关系排序-否则子类无法执行
        if (bean instanceof CollectionBean) {
            List<CollectionBean> list = mCollectionBeanDao.queryBuilder().where(CollectionBeanDao
                    .Properties.AbsPath
                    .eq(bean.getAbsPath())).build().list();
            return list.size() > 0;
        } else if (bean instanceof RemoveBean) {
            return mRemoveBeanDao.queryBuilder().where(RemoveBeanDao.Properties.AbsPath
                    .eq(bean.getAbsPath())).build().list().size() > 0;
        } else if (bean instanceof HomeListBean) {
            return mHomeDao.queryBuilder().where(HomeListBeanDao.Properties.AbsPath
                    .eq(bean.getAbsPath())).build().list().size() > 0;
        }
        return false;
    }

    /**
     * 查询数据是否存在
     *
     * @param bean IBean的子类
     * @param type 需要存入的类型-该方法会自动转换
     */
    public boolean isExist(IBean bean, int type) {
        switch (type) {
            case DATA_COLLECTION:
                return isExist(new CollectionBean(bean.getName(), bean.getAbsPath(), bean
                        .getLastModified()));
            case DATA_REMOVE:
                return isExist(new RemoveBean(bean.getName(), bean.getAbsPath(), bean
                        .getLastModified()));
            case DATA_HOME:
                return isExist(new HomeListBean(bean.getName(), bean.getAbsPath(), bean
                        .getLastModified(), false, false));
        }
        return false;
    }

    /**
     * 将list转型
     */
    public List transformBean(List<IBean> list, int type) {
        List beanList = new ArrayList();
        for (IBean bean : list) {
            switch (type) {
                case DATA_COLLECTION:
                    beanList.add(new CollectionBean(bean.getName(), bean.getAbsPath(), bean
                            .getLastModified()));
                    break;
                case DATA_REMOVE:
                    beanList.add(new RemoveBean(bean.getName(), bean.getAbsPath(), bean
                            .getLastModified()));
                    break;
                case DATA_HOME:
                    beanList.add(new HomeListBean(bean.getName(), bean.getAbsPath(), bean
                            .getLastModified(), isExist
                            (bean, DATA_COLLECTION), isExist(bean, DATA_REMOVE)));
            }
        }
        return beanList;
    }
}
