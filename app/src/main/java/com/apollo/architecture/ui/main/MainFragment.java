package com.apollo.architecture.ui.main;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.apollo.architecture.R;
import com.apollo.architecture.data.model.UserInfo;
import com.apollo.architecture.ui.base.BaseFragment;

import java.util.List;

public class MainFragment extends BaseFragment {

    private MainViewModel mainViewModel;

    private TextView textView;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_main;
    }

    @Override
    public void initView(View view, @Nullable Bundle savedInstanceState) {
        textView = view.findViewById(R.id.text);
        mainViewModel.getUserInfoList().observe(this, new Observer<List<UserInfo>>() {
            @Override
            public void onChanged(List<UserInfo> userInfo) {
                textView.setText(userInfo.get(0).getName());
            }
        });
        mainViewModel.getList();
    }

    @Override
    protected ViewModel initViewModel() {
        mainViewModel = createViewModel(MainViewModel.class, this);
        return mainViewModel;
    }
}
