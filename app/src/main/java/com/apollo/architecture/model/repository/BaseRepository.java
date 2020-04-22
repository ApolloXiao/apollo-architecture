package com.apollo.architecture.model.repository;

import androidx.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseRepository {
    public final static int CODE_ERROR_DEFAULT = -999999;
    public final static int CODE_ERROR_EMPTY_RESULT = -888888;

    protected  <T> void executeRes(Call<T> call, com.apollo.architecture.model.api.Callback<T> callback) {
        if (call == null) return;
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
                T body = response.body();
                if (response.isSuccessful() && body != null) {
                    if (body instanceof com.apollo.architecture.model.bean.Response) {
                        com.apollo.architecture.model.bean.Response result = (com.apollo.architecture.model.bean.Response) body;
                        if (result.getErrorCode() == 0) {
                            if (result.getData() != null) {
                                callback.onSuccess(body);
                            } else {
                                handelErrorRes(callback, CODE_ERROR_EMPTY_RESULT, "Response Data is null");
                            }
                        } else {
                            handelErrorRes(callback, result.getErrorCode(), result.getErrorMsg());
                        }
                        return;
                    }
                    if (callback != null) {
                        callback.onSuccess(body);
                    }
                } else {
                    if (callback != null) {
                        handelErrorRes(callback, response.code(), response.message());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
                handelErrorRes(callback, CODE_ERROR_DEFAULT, t.getMessage());
            }
        });
    }

    private void handelErrorRes(com.apollo.architecture.model.api.Callback callback,
                                int errorCode,
                                String errorMsg) {
        if (callback == null) return;
        com.apollo.architecture.model.bean.Response errorResponse = new com.apollo.architecture.model.bean.Response();
        errorResponse.setErrorMsg(errorMsg);
        errorResponse.setErrorCode(errorCode);
        callback.onError(errorResponse);
    }
}
