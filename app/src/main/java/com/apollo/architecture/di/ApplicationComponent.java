package com.apollo.architecture.di;

import android.app.Application;

import com.apollo.architecture.ui.base.MyApplication;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {ApplicationModule.class,
        ActFragBindingModule.class,
        ViewModelBindingModule.class,
        AndroidSupportInjectionModule.class})
public interface ApplicationComponent extends AndroidInjector<MyApplication> {
    @Component.Builder
    interface Builder {

        @BindsInstance
        ApplicationComponent.Builder application(Application application);

        ApplicationComponent build();
    }
}
