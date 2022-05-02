package com.example.kokofarm_user_app;

import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kokofarm_user_app.databinding.FragmentDongBinding;
import com.google.android.material.navigation.NavigationView;

public class DongFragment extends Fragment implements OnBackPressedListener {

    FragmentDongBinding binding;

    public DongFragment() {

    }

    public static DongFragment newInstance() {

        return new DongFragment();
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

        binding = FragmentDongBinding.inflate(inflater);



        return binding.getRoot();
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onBackPressed(){
        
    }
}