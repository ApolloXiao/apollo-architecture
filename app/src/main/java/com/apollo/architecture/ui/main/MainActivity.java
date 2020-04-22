package com.apollo.architecture.ui.main;

import android.os.Bundle;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.apollo.architecture.R;
import com.apollo.architecture.model.bean.Article;
import com.apollo.architecture.ui.base.BaseActivity;

import java.util.List;

public class MainActivity extends BaseActivity {

    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView = findViewById(R.id.text);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content, new MainFragment())
                    .commitAllowingStateLoss();
        }
        mainViewModel.getArticleList().observe(this, articles -> {
            if (!articles.isEmpty()) {
                textView.setText(articles.get(0).getName());
            }
        });
        mainViewModel.fetchUserInfoList();
        textView.setOnClickListener(v -> mainViewModel.fetchUserInfoList());
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
