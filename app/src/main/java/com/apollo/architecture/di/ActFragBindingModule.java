package com.apollo.architecture.di;

import com.apollo.architecture.ui.main.MainActivity;
import com.apollo.architecture.ui.main.MainFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActFragBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector()
    abstract MainActivity mainActivity();

    @FragmentScoped
    @ContributesAndroidInjector()
    abstract MainFragment mainFragment();
}
