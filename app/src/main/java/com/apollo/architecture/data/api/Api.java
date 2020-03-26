package com.apollo.architecture.data.api;

import androidx.lifecycle.LiveData;

import com.apollo.architecture.data.model.BaseRepositoryModel;
import com.apollo.architecture.data.model.UserInfo;

import java.util.List;

import retrofit2.http.GET;

public interface Api {
    String HOST = "https://www.wanandroid.com/";

    @GET("wxarticle/chapters/json")
    LiveData<BaseRepositoryModel<List<UserInfo>>> getPublicNumberList();

    @GET("article/listproject/0/json")
    LiveData<BaseRepositoryModel<UserInfo>> getTabInfo();
}
