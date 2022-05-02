package com.example.kokofarm_user_app;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kokofarm_user_app.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment implements View.OnClickListener, OnBackPressedListener {

    private FragmentHomeBinding binding;

    public HomeFragment() {

    }

    public static HomeFragment newInstance() {

        return new HomeFragment();
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
        binding = FragmentHomeBinding.inflate(inflater);

        binding.homeSu.setOnClickListener(this::onClick);
        binding.homeDong1.setOnClickListener(this::onClick);

        return binding.getRoot();
    }

    // fragment 메모리 누수 방지
    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.home_su:
                if(binding.homeDeathSu.getVisibility() != view.VISIBLE){
                    binding.homeSu.setRippleColorResource(R.color.opacity5);
                    setAutoTransition(binding.homeDeathSu, view.VISIBLE);
                    setAutoTransition(binding.homeCullSu, view.VISIBLE);
                    setAutoTransition(binding.homeOutSu, view.VISIBLE);
                    binding.homeAccordion.setImageResource(R.drawable.ic_baseline_arrow_up_24);
                } else {
                    setAutoTransition(binding.homeDeathSu, view.GONE);
                    setAutoTransition(binding.homeCullSu, view.GONE);
                    setAutoTransition(binding.homeOutSu, view.GONE);
                    binding.homeAccordion.setImageResource(R.drawable.ic_baseline_arrow_down_24);
                }

                break;

            case R.id.home_dong_1:
//                ((MainActivity)getActivity()).replaceFragment(DongFragment.newInstance());
                moveDongFragment();
                break;
        }
    }

    @Override
    public void onBackPressed(){
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.finish();
    }

    // view visible, gone
    public void setAutoTransition(ViewGroup viewGroup, int visibility){
        TransitionManager.beginDelayedTransition(viewGroup, new AutoTransition());
        viewGroup.setVisibility(visibility);
    }

    public void moveDongFragment(){
        Navigation.findNavController(binding.getRoot()).navigate(R.id.nav_dong);
    }
}