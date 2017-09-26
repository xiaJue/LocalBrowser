package com.xiajue.browser.localwebbrowser.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

/**
 * xiaJue 2017/9/16创建
 */
@Entity
public class CollectionBean extends HomeListBean {
    private String name;
    @Unique
    @Id
    private String absPath;

    private long lastModifide;//最后修改时间

    @Generated(hash = 1984120584)
    public CollectionBean(String name, String absPath, long lastModifide) {
        this.name = name;
        this.absPath = absPath;
        this.lastModifide = lastModifide;
    }

    @Generated(hash = 1423617684)
    public CollectionBean() {
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
