package com.apollo.architecture.ui.main;

import androidx.lifecycle.MutableLiveData;

import com.apollo.architecture.data.model.UserInfo;
import com.apollo.architecture.data.repository.UserRepository;
import com.apollo.architecture.ui.base.BaseViewModel;

import java.util.List;

import javax.inject.Inject;

public class MainViewModel extends BaseViewModel {
    UserRepository repository;

    private MutableLiveData<List<UserInfo>> userInfoList = new MutableLiveData<>();

    @Inject
    public MainViewModel(UserRepository repository) {
        this.repository = repository;
    }

    public MutableLiveData<List<UserInfo>> getUserInfoList() {
        return userInfoList;
    }

    public void getList() {
        repository.getPublicNumberList().observe(lifecycleOwner, listBaseRepositoryModel -> {
            if (listBaseRepositoryModel.getErrorCode() > 0) {
                showToast(listBaseRepositoryModel.getErrorMsg());
                return;
            }
            userInfoList.setValue(listBaseRepositoryModel.getData());
        });
    }

}
