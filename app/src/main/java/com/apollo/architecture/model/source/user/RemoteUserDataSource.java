package com.apollo.architecture.model.source.user;

import androidx.lifecycle.LiveData;

import com.apollo.architecture.model.api.WanService;
import com.apollo.architecture.model.bean.Response;
import com.apollo.architecture.model.bean.UserInfo;

import java.util.List;

import javax.inject.Inject;

public class RemoteUserDataSource implements UserDataSource {
    private WanService wanService;

    @Inject
    public RemoteUserDataSource(WanService wanService){
        this.wanService = wanService;
    }

    @Override
    public LiveData<Response<List<UserInfo>>> getPublicNumberList() {
        return wanService.getPublicNumberList();
    }
}
