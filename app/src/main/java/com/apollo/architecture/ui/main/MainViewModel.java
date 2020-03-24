package com.apollo.architecture.ui.main;

import com.apollo.architecture.ui.base.BaseViewModel;

import javax.inject.Inject;

public class MainViewModel extends BaseViewModel {

    @Inject
    public MainViewModel(){

    }

    public String getText(){
        return "Text from ViewModel";
    }
}
