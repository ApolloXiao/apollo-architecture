package com.apollo.architecture.ui.main;

import androidx.lifecycle.LiveData;

import com.apollo.architecture.data.model.BaseRepositoryModel;
import com.apollo.architecture.data.model.UserInfo;
import com.apollo.architecture.data.repository.UserRepository;
import com.apollo.architecture.ui.base.BaseViewModel;

import java.util.List;

import javax.inject.Inject;

public class MainViewModel extends BaseViewModel {
    private UserRepository repository;

    @Inject
    public MainViewModel(UserRepository repository) {
        this.repository = repository;
    }

    public LiveData<BaseRepositoryModel<List<UserInfo>>> fetchPublicNumberList() {
        return repository.getPublicNumberList();
    }

}
