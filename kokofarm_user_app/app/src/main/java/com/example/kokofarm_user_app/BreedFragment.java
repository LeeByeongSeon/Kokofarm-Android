package com.example.kokofarm_user_app;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.example.kokofarm_user_app.databinding.FragmentBreedBinding;

public class BreedFragment extends Fragment {

    private FragmentBreedBinding binding;

    public BreedFragment() {

    }

    public static BreedFragment newInstance() {

        return new BreedFragment();
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
        binding = FragmentBreedBinding.inflate(inflater);

        binding.breedInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(view.getContext());
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                WindowManager.LayoutParams winLayout = new WindowManager.LayoutParams();
                winLayout.copyFrom(dialog.getWindow().getAttributes());
                winLayout.width = WindowManager.LayoutParams.MATCH_PARENT;
                winLayout.height = WindowManager.LayoutParams.WRAP_CONTENT;

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_layout);
                dialog.show();

//               dialog.findViewById(R.id.dialog_cancel);

            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }
}