package com.example.kokofarm_user_app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kokofarm_user_app.databinding.FragmentScaleBinding;

import java.util.ArrayList;

public class ScaleFragment extends Fragment {

    private FragmentScaleBinding binding;
    private RecyclerView recyclerView;

    public ScaleFragment() {

    }

    public static ScaleFragment newInstance() {

        return new ScaleFragment();
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

        binding = FragmentScaleBinding.inflate(inflater);

        recyclerView = binding.scaleListView;

        RecyclerAdapter recyclerAdapter = new RecyclerAdapter();
        for(int i=1; i<=9; i++){
            String str = "0"+i;
            recyclerAdapter.addItem(str);
        }
        recyclerView.setAdapter(recyclerAdapter);

        return binding.getRoot();
    }
}