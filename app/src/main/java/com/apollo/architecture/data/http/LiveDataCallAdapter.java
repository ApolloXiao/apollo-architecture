package com.apollo.architecture.data.http;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.apollo.architecture.data.model.BaseRepositoryModel;

import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;

public class LiveDataCallAdapter<R> implements CallAdapter<R, LiveData<R>> {
    private Type type;

    public LiveDataCallAdapter(Type type) {
        this.type = type;
    }

    @Override
    public Type responseType() {
        return type;
    }

    @Override
    public LiveData<R> adapt(@NonNull Call<R> call) {
        return new LiveData<R>() {
            AtomicBoolean started = new AtomicBoolean(false);

            @Override
            protected void onActive() {
                super.onActive();
                if (started.compareAndSet(false, true)) {
                    call.enqueue(new Callback<R>() {
                        @Override
                        public void onResponse(@NonNull Call<R> call, @NonNull Response<R> response) {
                            postValue(response.body());
                        }

                        @Override
                        public void onFailure(@NonNull Call<R> call, @NonNull Throwable t) {
                            BaseRepositoryModel netResponse = new BaseRepositoryModel();
                            netResponse.setErrorMsg(t.getMessage());
                            postValue((R) netResponse);
                        }
                    });

                }
            }
        };
    }
}
