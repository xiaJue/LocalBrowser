package com.xiajue.browser.localwebbrowser.model.internet;

import com.xiajue.browser.localwebbrowser.model.Config;
import com.xiajue.browser.localwebbrowser.model.bean.BingBeanList;
import com.xiajue.browser.localwebbrowser.model.bean.MeiziBeanList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * xiaJue 2017/9/5创建
 */
public class RetrofitUtils {
    private BingService mBingService;
    private Retrofit mBingRetrofit;

    private MeiziService mMeiziService;
    private Retrofit mMeiziRetrofit;

    private OkHttpClient client;

    public RetrofitUtils() {
        client = new OkHttpClient();
        mBingRetrofit = new Retrofit.Builder().baseUrl(Config.BING_BASE_URL).addConverterFactory
                (GsonConverterFactory.create()).
                client(client).build();
        mBingService = mBingRetrofit.create(BingService.class);
        mMeiziRetrofit = new Retrofit.Builder().baseUrl(Config.MEIZI_IMAGE_BASE_URL)
                .addConverterFactory
                        (GsonConverterFactory.create()).
                        client(client).build();
        mMeiziService = mMeiziRetrofit.create(MeiziService.class);
    }

    private Call<BingBeanList> mBingCall;

    /**
     * 获取并解析json数据
     */
    public void getBingBean(Callback callback) {
        mBingCall = mBingService.getBingBean(0, 1);//只解析第一条数据
        mBingCall.enqueue(callback);
    }

    private Call<MeiziBeanList> mMeizhiCall;

    /**
     * 获取并解析json数据
     */
    public void getMeiziBean(Callback callback) {
        mMeizhiCall = mMeiziService.getMeiziBean(1, 1);//只解析第一条数据
        mMeizhiCall.enqueue(callback);
    }

    public void cancel() {
        if (mMeizhiCall != null && mBingCall != null) {
            mMeizhiCall.cancel();
            mBingCall.cancel();
        }
    }

    public static void getContentFromInternet(final String url, final okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        okhttp3.Call call = client.newCall(request);
        call.enqueue(callback);
    }
}
