package com.apollo.architecture.data.source.user;

import androidx.lifecycle.LiveData;

import com.apollo.architecture.data.model.BaseRepositoryModel;
import com.apollo.architecture.data.model.UserInfo;

import java.util.List;

public interface UserDataSource {
    LiveData<BaseRepositoryModel<List<UserInfo>>> getPublicNumberList();
}
