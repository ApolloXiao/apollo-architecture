package com.apollo.architecture.ui.main;

import android.os.Bundle;
import android.widget.TextView;

import androidx.lifecycle.ViewModel;

import com.apollo.architecture.R;
import com.apollo.architecture.data.http.Callback;
import com.apollo.architecture.data.model.UserInfo;
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
        fetchData(mainViewModel.fetchPublicNumberList(), new Callback<List<UserInfo>>(mainViewModel) {
            @Override
            public void onSuccess(List<UserInfo> userInfo) {
                textView.setText(userInfo.get(0).getName());
            }
        });
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
