package com.apollo.architecture.model.source.user;

import androidx.lifecycle.LiveData;

import com.apollo.architecture.model.bean.Response;
import com.apollo.architecture.model.bean.UserInfo;

import java.util.List;

import javax.inject.Inject;

public class LocalUserDataSource implements UserDataSource {

    @Inject
    public LocalUserDataSource(){

    }

    @Override
    public LiveData<Response<List<UserInfo>>> getPublicNumberList() {
        return null;
    }
}
