package com.apollo.architecture.di;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.ViewModelProvider;

import dagger.Binds;
import dagger.Module;

@Module()
public abstract class ApplicationModule {
    @Binds
    abstract Context bindContext(Application application);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory viewModelFactory);
}
