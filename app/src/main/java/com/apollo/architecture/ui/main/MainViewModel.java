package com.apollo.architecture.ui.main;

import androidx.lifecycle.LiveData;

import com.apollo.architecture.model.bean.Response;
import com.apollo.architecture.model.bean.UserInfo;
import com.apollo.architecture.model.repository.UserRepository;
import com.apollo.architecture.ui.base.BaseViewModel;

import java.util.List;

import javax.inject.Inject;

public class MainViewModel extends BaseViewModel {
    private UserRepository repository;

    @Inject
    public MainViewModel(UserRepository repository) {
        this.repository = repository;
    }

    public LiveData<Response<List<UserInfo>>> fetchPublicNumberList() {
        return repository.getPublicNumberList();
    }

}
