package com.xiajue.browser.localwebbrowser.model.bean;

import java.util.List;

/**
 * xiaJue 2017/9/20创建
 */
public class MeiziBeanList {
    private List<MeiziBean> results;

    public MeiziBeanList(List<MeiziBean> results) {
        this.results = results;
    }

    public MeiziBeanList() {
    }

    public List<MeiziBean> getResults() {
        return results;
    }

    public void setResults(List<MeiziBean> results) {
        this.results = results;
    }
}
