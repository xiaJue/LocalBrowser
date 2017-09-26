package com.xiajue.browser.localwebbrowser.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * xiaJue 2017/9/15创建
 */
@Entity
public class HomeListBean implements IBean {

    protected String name;
    @Id
    @Unique
    protected String absPath;

    private long lastModifide;//最后修改时间

    public boolean isCollection;//是否被收藏
    public boolean isRemove;//是否被移除

    public HomeListBean() {
    }

    @Generated(hash = 1044736383)
    public HomeListBean(String name, String absPath, long lastModifide,
            boolean isCollection, boolean isRemove) {
        this.name = name;
        this.absPath = absPath;
        this.lastModifide = lastModifide;
        this.isCollection = isCollection;
        this.isRemove = isRemove;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setAbsPath(String absPath) {
        this.absPath = absPath;
    }

    @Override
    public String getAbsPath() {
        return absPath;
    }

    @Override
    public String getName() {
        return name;
    }

    public boolean getIsCollection() {
        return this.isCollection;
    }

    public void setIsCollection(boolean isCollection) {
        this.isCollection = isCollection;
    }

    public boolean getIsRemove() {
        return this.isRemove;
    }

    public void setIsRemove(boolean isRemove) {
        this.isRemove = isRemove;
    }

    public long getLastModified() {
        return this.lastModifide;
    }

    public void setLastModified(long lastModifide) {
        this.lastModifide = lastModifide;
    }

    public long getLastModifide() {
        return this.lastModifide;
    }

    public void setLastModifide(long lastModifide) {
        this.lastModifide = lastModifide;
    }
}
