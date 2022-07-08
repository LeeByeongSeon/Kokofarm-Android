package com.example.kokofarm_user_app;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.app.BundleCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kokofarm_user_app.databinding.FragmentBreedListBinding;

public class BreedListFragment extends Fragment {

    private FragmentBreedListBinding binding;
    private RecyclerView recyclerView;
    String id;

    public BreedListFragment() {}

    public BreedListFragment(String id){ this.id = id; }

    public static BreedListFragment newInstance(String id){
        return new BreedListFragment(id);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(getArguments() != null){

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        binding = FragmentBreedListBinding.inflate(inflater);
        
        // 사육일지 다이얼로그 띄우는 부분
//        binding.breedBreedList.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Dialog dialog = new Dialog(view.getContext());
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//                WindowManager.LayoutParams winLayout = new WindowManager.LayoutParams();
//                winLayout.copyFrom(dialog.getWindow().getAttributes());
//                winLayout.width = WindowManager.LayoutParams.MATCH_PARENT;
//                winLayout.height = WindowManager.LayoutParams.WRAP_CONTENT;
//
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setContentView(R.layout.dialog_layout);
//                dialog.show();
//
////               dialog.findViewById(R.id.dialog_cancel);
//
//            }
//        });

        // 리사이클러뷰
        recyclerView = binding.breedRcvList;
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(container.getContext());
        for(int i=1; i<=20; i++){
            String str;
            if(i<10){
                str = "0"+i;
            } else {
                str = String.valueOf(i);
            }
            recyclerAdapter.addItem(str);
        }
        recyclerView.setAdapter(recyclerAdapter);
        
        
        return binding.getRoot();
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }
}
