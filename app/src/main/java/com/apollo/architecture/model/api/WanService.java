package com.apollo.architecture.model.api;

import com.apollo.architecture.model.bean.Response;
import com.apollo.architecture.model.bean.Article;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface WanService {
    String HOST = "https://www.wanandroid.com/";

    @GET("wxarticle/chapters/json")
    Call<Response<List<Article>>> getArticleList();
}
