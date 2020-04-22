package com.apollo.architecture.di;

import com.apollo.architecture.BuildConfig;
import com.apollo.architecture.model.api.WanService;
import com.apollo.architecture.model.api.HttpLoggingInterceptor;
import com.apollo.architecture.model.http.LiveDataCallAdapterFactory;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class RetrofitModule {

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        return getOkHttpClient();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        return getRetrofit(okHttpClient, WanService.HOST);
    }

    @Provides
    @Singleton
    WanService provideApi(Retrofit retrofit) {
        return retrofit.create(WanService.class);
    }


    //-------------------------------私有方法分界线-------------------------------------

    /**
     * 方便Retrofit自定义
     *
     * @param okHttpClient OkHttpClient
     * @param baseUrl      api baseUrl
     * @return Retrofit
     */
    private Retrofit getRetrofit(OkHttpClient okHttpClient, String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(LiveDataCallAdapterFactory.create())
                .build();
    }

    /**
     * 方便OkHttpClient自定义
     *
     * @return OkHttpClient
     */
    private OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }
        //设置超时
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
        return builder.build();
    }
}
