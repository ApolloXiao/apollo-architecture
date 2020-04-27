package com.apollo.architecture.model.api

import com.apollo.architecture.model.bean.Article
import com.apollo.architecture.model.bean.BaseResponse
import retrofit2.http.GET

interface SimpleService {

    companion object {
        //鸿洋的玩Android的API
        const val BASE_URL = "https://www.wanandroid.com/"
    }

    @GET("wxarticle/chapters/json")
    suspend fun getArticleList(): BaseResponse<List<Article>>

}