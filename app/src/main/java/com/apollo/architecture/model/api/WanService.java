package com.apollo.architecture.model.api;

import androidx.lifecycle.LiveData;

import com.apollo.architecture.model.bean.Response;
import com.apollo.architecture.model.bean.UserInfo;

import java.util.List;

import retrofit2.http.GET;

public interface WanService {
    String HOST = "https://www.wanandroid.com/";

    @GET("wxarticle/chapters/json")
    LiveData<Response<List<UserInfo>>> getPublicNumberList();

    @GET("article/listproject/0/json")
    LiveData<Response<UserInfo>> getTabInfo();
}
