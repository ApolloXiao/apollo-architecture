package com.apollo.architecture.ui.main;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.apollo.architecture.R;
import com.apollo.architecture.ui.base.BaseFragment;

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
        mainViewModel.getArticleList().observe(this, articles -> {
            if (!articles.isEmpty()){
                textView.setText(articles.get(0).getName());
            }
        });
        mainViewModel.fetchUserInfoList();
        textView.setOnClickListener(v -> mainViewModel.fetchUserInfoList());
    }

    @Override
    protected ViewModel initViewModel() {
        mainViewModel = createViewModel(MainViewModel.class, this);
        return mainViewModel;
    }
}
