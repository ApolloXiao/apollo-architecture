package com.apollo.architecture.ui.base;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.apollo.architecture.data.http.Callback;
import com.apollo.architecture.data.model.BaseRepositoryModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public abstract class BaseFragment extends DaggerFragment {
    @Inject
    ViewModelProvider.Factory factory;

    private AppCompatDialog loadingDialog;
    private LiveData liveData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getContentViewId(), container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewModelEvent();
        initView(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dismissLoading();
    }

    public abstract int getContentViewId();

    public abstract void initView(View view, @Nullable Bundle savedInstanceState);

    protected abstract ViewModel initViewModel();

    protected <T extends ViewModel> T createViewModel(Class<T> modelClass, ViewModelStoreOwner owner) {
        return new ViewModelProvider(owner, factory).get(modelClass);
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
        if (loadingDialog == null && getActivity() != null) {
            loadingDialog = new AppCompatDialog(getActivity());
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
        if (getActivity() != null) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }

    protected void showToast(int resId) {
        if (getActivity() != null) {
            Toast.makeText(getActivity(), getString(resId), Toast.LENGTH_SHORT).show();
        }
    }

    protected <T> LiveData<T> fetchData(@NonNull LiveData<BaseRepositoryModel<T>> liveData, @NonNull Callback<T> callback) {
        if (this.liveData != null) {
            this.liveData.removeObservers(this);
            this.liveData = null;
        }
        this.liveData = liveData;
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
                                if (getActivity() != null
                                        && getActivity() instanceof BaseActivity) {
                                    BaseActivity baseActivity = (BaseActivity) getActivity();
                                    baseActivity.finish();
                                }
                                break;
                            }
                            case BaseActionEvent.FINISH_WITH_RESULT: {
                                if (getActivity() != null
                                        && getActivity() instanceof BaseActivity) {
                                    BaseActivity baseActivity = (BaseActivity) getActivity();
                                    baseActivity.finishWithResult();
                                }
                                break;
                            }
                        }
                    }
                });
            }
        }
    }
}