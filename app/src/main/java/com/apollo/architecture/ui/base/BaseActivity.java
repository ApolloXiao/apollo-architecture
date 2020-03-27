package com.apollo.architecture.ui.base;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.apollo.architecture.data.http.Callback;
import com.apollo.architecture.data.model.BaseRepositoryModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public abstract class BaseActivity extends DaggerAppCompatActivity {

    @Inject
    ViewModelProvider.Factory factory;

    private AppCompatDialog loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        initViewModelEvent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissLoading();
    }

    protected abstract int getContentViewId();

    protected abstract ViewModel initViewModel();

    protected <T extends ViewModel> T createViewModel(Class<T> modelClass) {
        return new ViewModelProvider(this, factory).get(modelClass);
    }

    protected List<ViewModel> initViewModelList() {
        return null;
    }

    protected void startLoading() {
        startLoading(null);
    }

    protected void startLoading(int resId) {
        startLoading(getString(resId));
    }

    protected void startLoading(String message) {
        //TODO 加个有进度的UI
        if (loadingDialog == null) {
            loadingDialog = new AppCompatDialog(this);
            loadingDialog.setCancelable(false);
            loadingDialog.setCanceledOnTouchOutside(false);
        }
        loadingDialog.setTitle(message);
        loadingDialog.show();
    }

    protected void dismissLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    protected void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(int resId) {
        Toast.makeText(this, getString(resId), Toast.LENGTH_SHORT).show();
    }

    protected void finishWithResult() {
        setResult(RESULT_OK);
        finish();
    }

    protected <T> LiveData<T> fetchData(@NonNull LiveData<BaseRepositoryModel<T>> liveData, @NonNull Callback<T> callback) {
        liveData.removeObservers(this);
        MutableLiveData<T> data = new MutableLiveData<>();
        liveData.observe(this, baseRepositoryModel -> {
            if (baseRepositoryModel != null) {
                if (baseRepositoryModel.getErrorCode() > 0 ||
                        !TextUtils.isEmpty(baseRepositoryModel.getErrorMsg())) {
                    callback.onError(baseRepositoryModel);
                    return;
                }
                T t = baseRepositoryModel.getData();
                if (t != null) {
                    data.setValue(t);
                    callback.onSuccess(t);
                }
            }
        });
        return data;
    }

    private void initViewModelEvent() {
        List<ViewModel> viewModelList = initViewModelList();
        if (viewModelList != null && viewModelList.size() > 0) {
            observeEvent(viewModelList);
        } else {
            ViewModel viewModel = initViewModel();
            List<ViewModel> modelList = new ArrayList<>();
            modelList.add(viewModel);
            observeEvent(modelList);
        }
    }

    private void observeEvent(List<ViewModel> viewModelList) {
        for (ViewModel viewModel : viewModelList) {
            if (viewModel instanceof IViewModelAction) {
                IViewModelAction viewModelAction = (IViewModelAction) viewModel;
                viewModelAction.getActionLiveData().observe(this, baseActionEvent -> {
                    if (baseActionEvent != null) {
                        switch (baseActionEvent.getAction()) {
                            case BaseActionEvent.SHOW_LOADING_DIALOG: {
                                startLoading(baseActionEvent.getMessage());
                                break;
                            }
                            case BaseActionEvent.SHOW_LOADING_DIALOG_RES_ID: {
                                startLoading(baseActionEvent.getResId());
                                break;
                            }
                            case BaseActionEvent.DISMISS_LOADING_DIALOG: {
                                dismissLoading();
                                break;
                            }
                            case BaseActionEvent.SHOW_TOAST: {
                                showToast(baseActionEvent.getMessage());
                                break;
                            }
                            case BaseActionEvent.SHOW_TOAST_FOR_RES_ID: {
                                showToast(baseActionEvent.getResId());
                                break;
                            }
                            case BaseActionEvent.FINISH: {
                                finish();
                                break;
                            }
                            case BaseActionEvent.FINISH_WITH_RESULT: {
                                finishWithResult();
                                break;
                            }
                        }
                    }
                });
            }
        }
    }
}
