package com.apollo.architecture.model.source.user;

import androidx.lifecycle.LiveData;

import com.apollo.architecture.model.bean.Response;
import com.apollo.architecture.model.bean.UserInfo;

import java.util.List;

public interface UserDataSource {
    LiveData<Response<List<UserInfo>>> getPublicNumberList();
}
