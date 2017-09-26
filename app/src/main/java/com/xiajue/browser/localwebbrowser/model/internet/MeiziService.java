package com.xiajue.browser.localwebbrowser.model.internet;

import com.xiajue.browser.localwebbrowser.model.Config;
import com.xiajue.browser.localwebbrowser.model.bean.MeiziBeanList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * xiaJue 2017/9/20创建
 */
public interface MeiziService {
    /**
     * 发起一个get请求获取网络数据并解析成javabean
     */
    @GET(Config.MEIZI_IMAGE_JSON_URL)
    Call<MeiziBeanList> getMeiziBean(@Path("size") int idx, @Path("pages") int n);
}
