package com.apollo.architecture.data.repository;

import androidx.lifecycle.LiveData;

import com.apollo.architecture.data.model.BaseRepositoryModel;
import com.apollo.architecture.data.model.UserInfo;
import com.apollo.architecture.data.source.user.LocalUserDataSource;
import com.apollo.architecture.data.source.user.RemoteUserDataSource;

import java.util.List;

import javax.inject.Inject;

public class UserRepository {

    private RemoteUserDataSource remoteUserDataSource;

    private LocalUserDataSource localUserDataSource;


    @Inject
    public UserRepository(RemoteUserDataSource remoteUserDataSource,
                          LocalUserDataSource localUserDataSource) {
        this.remoteUserDataSource = remoteUserDataSource;
        this.localUserDataSource = localUserDataSource;

    }

    public LiveData<BaseRepositoryModel<List<UserInfo>>> getPublicNumberList() {
        return remoteUserDataSource.getPublicNumberList();
    }
}
