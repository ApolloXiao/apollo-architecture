package com.apollo.architecture.model.repository;

import androidx.lifecycle.LiveData;

import com.apollo.architecture.model.bean.Response;
import com.apollo.architecture.model.bean.UserInfo;
import com.apollo.architecture.model.source.user.LocalUserDataSource;
import com.apollo.architecture.model.source.user.RemoteUserDataSource;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserRepository {

    private RemoteUserDataSource remoteUserDataSource;

    private LocalUserDataSource localUserDataSource;


    @Inject
    public UserRepository(RemoteUserDataSource remoteUserDataSource,
                          LocalUserDataSource localUserDataSource) {
        this.remoteUserDataSource = remoteUserDataSource;
        this.localUserDataSource = localUserDataSource;

    }

    public LiveData<Response<List<UserInfo>>> getPublicNumberList() {
        return remoteUserDataSource.getPublicNumberList();
    }
}
