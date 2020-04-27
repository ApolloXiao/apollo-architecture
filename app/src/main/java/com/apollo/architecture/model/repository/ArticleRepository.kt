package com.apollo.architecture.model.repository

import com.apollo.architecture.model.api.SimpleService
import com.apollo.architecture.model.bean.Article
import com.apollo.architecture.model.bean.BaseResponse

class ArticleRepository(private val service: SimpleService) {
    suspend fun getArticleList(): BaseResponse<List<Article>> {
        return service.getArticleList()
    }
}