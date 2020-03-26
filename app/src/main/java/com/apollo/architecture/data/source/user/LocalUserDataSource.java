package com.apollo.architecture.data.source.user;

import androidx.lifecycle.LiveData;

import com.apollo.architecture.data.model.BaseRepositoryModel;
import com.apollo.architecture.data.model.UserInfo;

import java.util.List;

import javax.inject.Inject;

public class LocalUserDataSource implements UserDataSource {

    @Inject
    public LocalUserDataSource(){

    }

    @Override
    public LiveData<BaseRepositoryModel<List<UserInfo>>> getPublicNumberList() {
        return null;
    }
}
