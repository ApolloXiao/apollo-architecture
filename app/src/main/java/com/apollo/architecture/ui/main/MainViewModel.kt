package com.apollo.architecture.ui.main

import androidx.lifecycle.MutableLiveData
import com.apollo.architecture.model.bean.Article
import com.apollo.architecture.model.repository.ArticleRepository
import com.apollo.architecture.ui.base.BaseViewModel

class MainViewModel(private val articleRepository: ArticleRepository) : BaseViewModel() {
    val articleList = MutableLiveData<List<Article>>()

    fun fetchArticleLists() {
        launchResultOnUI(
                { articleRepository.getArticleList() },
                { articleList.value = it }
        )
    }
}