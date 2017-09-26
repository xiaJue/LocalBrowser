package com.xiajue.browser.localwebbrowser.model.bean;

/**
 * xiaJue 2017/9/20创建
 */
public class MeiziBean {
    private String url;
    private String createdAt;

    public MeiziBean(String url, String createdAt) {
        this.url = url;
        this.createdAt = createdAt;
    }

    public MeiziBean() {
    }

    public String getUrl() {
        return url;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
