package com.apollo.architecture.data.http;

import com.apollo.architecture.data.model.BaseRepositoryModel;
import com.apollo.architecture.ui.base.BaseViewModel;

public abstract class Callback<T> {
   private BaseViewModel baseViewModel;

   public Callback(BaseViewModel baseViewModel){
       this.baseViewModel = baseViewModel;
   }
   public abstract void onSuccess(T t);
   public void onError(BaseRepositoryModel<T> baseRepositoryModel){
       //这里面对401等code作不同的逻辑处理
       if (baseViewModel!=null) {
           baseViewModel.showToast(baseRepositoryModel.getErrorMsg());
       }
   }
}
