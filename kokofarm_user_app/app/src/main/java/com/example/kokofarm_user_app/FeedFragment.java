package com.example.kokofarm_user_app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kokofarm_user_app.databinding.FragmentFeedBinding;

public class FeedFragment extends Fragment implements View.OnClickListener {

    private FragmentFeedBinding binding;

    public FeedFragment() {

    }

    public static FeedFragment newInstance() {

        return new FeedFragment();
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
        binding = FragmentFeedBinding.inflate(inflater);

        //binding.feedDongList.homeDong1.setOnClickListener(this::onClick);

        return binding.getRoot();
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){

//            case R.id.home_dong_1:
//                moveDongFragment(R.id.nav_feed_dong);
//
//                break;
        }
    }

    // fragment 메모리 누수 방지
    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }

    public void moveDongFragment(int id){
        Navigation.findNavController(binding.getRoot()).navigate(id);
    }
}