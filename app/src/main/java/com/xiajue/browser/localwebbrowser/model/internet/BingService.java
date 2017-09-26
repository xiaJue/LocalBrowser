package com.xiajue.browser.localwebbrowser.model.internet;

import com.xiajue.browser.localwebbrowser.model.Config;
import com.xiajue.browser.localwebbrowser.model.bean.BingBeanList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * xiaJue 2017/9/20创建
 */
public interface BingService {
    /**
     * 发起一个get请求获取网络数据并解析成javabean
     */
    @GET(Config.BING_IMAGE_JSON_URL)
    Call<BingBeanList> getBingBean(@Query("idx") int idx, @Query("n") int n);
}
