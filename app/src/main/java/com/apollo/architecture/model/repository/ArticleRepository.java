package com.apollo.architecture.model.repository;

import com.apollo.architecture.model.api.Callback;
import com.apollo.architecture.model.api.WanService;
import com.apollo.architecture.model.bean.Response;
import com.apollo.architecture.model.bean.Article;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ArticleRepository extends BaseRepository{

    private WanService service;

    @Inject
    public ArticleRepository(WanService service) {
        this.service = service;
    }

    public void getArticleList(Callback<Response<List<Article>>>callback) {
        executeRes(service.getArticleList(),callback);
    }
}
