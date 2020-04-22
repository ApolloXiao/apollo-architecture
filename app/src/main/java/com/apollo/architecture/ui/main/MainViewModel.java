package com.apollo.architecture.ui.main;

import androidx.lifecycle.MutableLiveData;

import com.apollo.architecture.model.api.Callback;
import com.apollo.architecture.model.bean.Response;
import com.apollo.architecture.model.bean.Article;
import com.apollo.architecture.model.repository.ArticleRepository;
import com.apollo.architecture.ui.base.BaseViewModel;

import java.util.List;

import javax.inject.Inject;

public class MainViewModel extends BaseViewModel {
    private ArticleRepository repository;
    private MutableLiveData<List<Article>> articleList = new MutableLiveData<>();

    @Inject
    public MainViewModel(ArticleRepository repository) {
        this.repository = repository;
    }

    public MutableLiveData<List<Article>> getArticleList() {
        return articleList;
    }

    public void fetchUserInfoList() {
        repository.getArticleList(new Callback<Response<List<Article>>>(this) {
            @Override
            public void onSuccess(Response<List<Article>> listResponse) {
                articleList.setValue(listResponse.getData());
            }
        });
    }

}
