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
        textView.setText(mainViewModel.getText());
    }

    @Override
    protected ViewModel initViewModel() {
        mainViewModel = createViewModel(MainViewModel.class, getActivity());
        return mainViewModel;
    }
}
