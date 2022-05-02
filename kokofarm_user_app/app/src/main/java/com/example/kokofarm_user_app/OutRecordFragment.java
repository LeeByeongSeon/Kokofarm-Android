package com.example.kokofarm_user_app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kokofarm_user_app.databinding.FragmentOutRecordBinding;

public class OutRecordFragment extends Fragment {

    private FragmentOutRecordBinding binding;

    public OutRecordFragment() {

    }

    public static OutRecordFragment newInstance() {

        return new OutRecordFragment();
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
        binding = FragmentOutRecordBinding.inflate(inflater);


        return binding.getRoot();
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }
}