package com.apollo.architecture.data.source.user;

import androidx.lifecycle.LiveData;

import com.apollo.architecture.data.api.Api;
import com.apollo.architecture.data.model.BaseRepositoryModel;
import com.apollo.architecture.data.model.UserInfo;

import java.util.List;

import javax.inject.Inject;

public class RemoteUserDataSource implements UserDataSource {
    private  Api api;

    @Inject
    public RemoteUserDataSource(Api api){
        this.api = api;
    }

    @Override
    public LiveData<BaseRepositoryModel<List<UserInfo>>> getPublicNumberList() {
        return api.getPublicNumberList();
    }
}
