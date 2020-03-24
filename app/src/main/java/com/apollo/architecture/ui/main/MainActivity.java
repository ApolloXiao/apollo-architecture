package com.apollo.architecture.ui.main;

import android.os.Bundle;
import android.widget.TextView;

import androidx.lifecycle.ViewModel;

import com.apollo.architecture.R;
import com.apollo.architecture.ui.base.BaseActivity;

public class MainActivity extends BaseActivity {

    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView = findViewById(R.id.text);
        textView.setText(mainViewModel.getText());
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content, new MainFragment())
                    .commitAllowingStateLoss();
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected ViewModel initViewModel() {
        mainViewModel = createViewModel(MainViewModel.class);
        return mainViewModel;
    }

}
