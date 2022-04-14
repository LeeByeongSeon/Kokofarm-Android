package com.example.kkf_user_2_0.ui.ext_env;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.kkf_user_2_0.databinding.FragmentExtEnvBinding;

public class ExtEnvFragment extends Fragment {

    FragmentExtEnvBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentExtEnvBinding.inflate(inflater, container,false);
        View view = binding.getRoot();



        return view;
    }
}
