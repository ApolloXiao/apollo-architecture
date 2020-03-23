package com.apollo.architecture.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apollo.architecture.R;

import javax.inject.Inject;

public class MainFragment extends Fragment {
    @Inject
    ViewModelProvider.Factory factory;

    private MainViewModel mainViewModel;

    private TextView textView;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        textView = new TextView(getActivity());
        return textView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainViewModel = new ViewModelProvider(requireActivity(),factory).get(MainViewModel.class);
        textView.setText(mainViewModel.getText());
    }
}
