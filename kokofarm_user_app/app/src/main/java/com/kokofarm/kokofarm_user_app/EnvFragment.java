package com.kokofarm.kokofarm_user_app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kokofarm.kokofarm_user_app.databinding.FragmentEnvBinding;
import com.kokofarm.kokofarm_user_app.databinding.FragmentFeedChartBinding;

public class EnvFragment extends Fragment {

    private FragmentEnvBinding binding;

    public EnvFragment() {

    }

    public static EnvFragment newInstance() {

        return new EnvFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEnvBinding.inflate(inflater);

        return binding.getRoot();
    }

    // fragment 메모리 누수 방지
    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }
}