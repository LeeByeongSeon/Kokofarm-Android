package com.example.kokofarm_user_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.kokofarm_user_app.databinding.FragmentScaleChartBinding;

public class ScaleChartFragment extends Fragment {

    private FragmentScaleChartBinding binding;
    String id;

    public ScaleChartFragment() {}

    public ScaleChartFragment(String id){ this.id = id; }

    public static ScaleChartFragment newInstance(String id) {
        return new ScaleChartFragment(id);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(getArguments() != null){

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentScaleChartBinding.inflate(inflater);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }
}
