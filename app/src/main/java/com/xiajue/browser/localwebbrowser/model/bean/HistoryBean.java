package com.xiajue.browser.localwebbrowser.model.bean;

import com.xiajue.browser.localwebbrowser.model.utils.StringUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Moing_Admin on 2017/10/8.
 */
@Entity
public class HistoryBean {
    @Id
    public String lastLoad;
    public String title;
    public String url;

    @Generated(hash = 808260815)
    public HistoryBean(String lastLoad, String title, String url) {
        this.lastLoad = lastLoad;
        this.title = title;
        this.url = url;
    }

    @Generated(hash = 48590348)
    public HistoryBean() {
    }

    public String getLastLoad() {
        return this.lastLoad;
    }

    public String getLastLoadFormatString() {
        return StringUtils.formatDate(Long.valueOf(lastLoad));
    }

    public void setLastLoad(String lastLoad) {
        this.lastLoad = lastLoad;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
