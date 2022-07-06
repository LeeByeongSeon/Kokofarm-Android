package com.example.kokofarm_user_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.kokofarm_user_app.databinding.FragmentFeedChartBinding;

public class FeedChartFragment extends Fragment {

    private FragmentFeedChartBinding binding;
    String id;

    public FeedChartFragment() {

    }

    public FeedChartFragment(String id){ this.id = id; }

    public static FeedChartFragment newInstance(String id) {

        return new FeedChartFragment(id);
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
        binding = FragmentFeedChartBinding.inflate(inflater);

        return binding.getRoot();
    }

    // fragment 메모리 누수 방지
    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }
}