package com.apollo.architecture.model.http;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.apollo.architecture.model.bean.Response;

import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;

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
                        public void onResponse(@NonNull Call<R> call, @NonNull retrofit2.Response<R> response) {
                            if (response.isSuccessful()) {
                                postValue(response.body());
                            } else {
                                Response netResponse = new Response();
                                netResponse.setErrorMsg(response.message());
                                netResponse.setErrorCode(response.code());
                                postValue((R) netResponse);
                            }

                        }

                        @Override
                        public void onFailure(@NonNull Call<R> call, @NonNull Throwable t) {
                            Response netResponse = new Response();
                            netResponse.setErrorMsg(t.getMessage());
                            netResponse.setErrorCode(999999999);
                            postValue((R) netResponse);
                        }
                    });

                }
            }
        };
    }
}