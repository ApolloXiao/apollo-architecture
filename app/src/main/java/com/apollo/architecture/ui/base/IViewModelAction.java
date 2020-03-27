package com.apollo.architecture.ui.base;

import androidx.lifecycle.MutableLiveData;

public interface IViewModelAction {

    void startLoading();

    void startLoading(String message);

    void startLoading(int resId);

    void dismissLoading();

    void showToast(String message);

    void showToast(int resId);

    void finish();

    void finishWithResult();

    MutableLiveData<BaseActionEvent> getActionLiveData();
}
