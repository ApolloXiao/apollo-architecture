package com.apollo.architecture.ui.main;

import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

public class MainViewModel extends ViewModel {

    @Inject
    public MainViewModel(){

    }

    public String getText(){
        return "Text from ViewModel";
    }
}
