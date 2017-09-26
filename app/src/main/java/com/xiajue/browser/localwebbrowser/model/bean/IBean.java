package com.xiajue.browser.localwebbrowser.model.bean;

/**
 * xiaJue 2017/9/18创建
 */
public interface IBean {

    void setName(String name);

    void setAbsPath(String absPath);

    String getAbsPath();

    String getName();

    long getLastModified();

    void setLastModified(long modified);
}
