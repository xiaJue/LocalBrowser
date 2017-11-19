package com.xiajue.browser.localwebbrowser.model.bean;

/**
 * Created by Moing_Admin on 2017/11/14.
 */

public class CommonWebsiteBean {
    private String title;
    private String address;

    public CommonWebsiteBean(String title, String address) {
        this.title = title;
        this.address = address;
    }

    public CommonWebsiteBean() {
    }

    public String getTitle() {
        return title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
