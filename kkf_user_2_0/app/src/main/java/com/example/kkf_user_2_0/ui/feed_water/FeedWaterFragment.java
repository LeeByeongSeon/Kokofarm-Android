package com.example.kkf_user_2_0.ui.feed_water;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.kkf_user_2_0.MainActivity;
import com.example.kkf_user_2_0.databinding.FragmentFeedWaterBinding;

public class FeedWaterFragment extends Fragment {
    private FragmentFeedWaterBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentFeedWaterBinding.inflate(inflater, container,false);
        View view = binding.getRoot();

        setHasOptionsMenu(true);

        return view;
    }
}
