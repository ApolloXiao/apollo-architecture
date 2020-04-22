package com.apollo.architecture.model.repository;

import androidx.lifecycle.LiveData;

import com.apollo.architecture.model.api.WanService;
import com.apollo.architecture.model.bean.Response;
import com.apollo.architecture.model.bean.UserInfo;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserRepository {

    private WanService service;

    @Inject
    public UserRepository(WanService service) {
        this.service = service;
    }

    public LiveData<Response<List<UserInfo>>> getPublicNumberList() {
        return service.getPublicNumberList();
    }
}
