package com.apollo.architecture.model.api;

import com.apollo.architecture.BuildConfig;
import com.apollo.architecture.model.bean.Response;
import com.apollo.architecture.model.repository.BaseRepository;
import com.apollo.architecture.ui.base.BaseViewModel;

public abstract class Callback<T> {
    private BaseViewModel baseViewModel;

    public Callback(BaseViewModel baseViewModel) {
        this.baseViewModel = baseViewModel;
    }

    public abstract void onSuccess(T t);

    public void onError(Response response) {
        //这里面对401等code作不同的逻辑处理
        if (baseViewModel != null) {
            if (!BuildConfig.DEBUG && response.getErrorCode() != BaseRepository.CODE_ERROR_DEFAULT) {
                baseViewModel.showToast(response.getErrorMsg());
            }
        }
    }
}
