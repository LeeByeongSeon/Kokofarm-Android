package com.example.kokofarm_user_app;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.core.app.BundleCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kokofarm_user_app.databinding.FragmentBreedInputLayoutBinding;
import com.example.kokofarm_user_app.databinding.FragmentBreedListBinding;
import com.example.kokofarm_user_app.kkf_utils.DateUtil;
import com.example.kokofarm_user_app.manager.DataCacheManager;
import com.example.kokofarm_user_app.manager.PageManager;
import com.example.kokofarm_user_app.piece.BreedCardView;
import com.example.kokofarm_user_app.piece.DongCardView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class BreedInputFragment extends Fragment {

    private FragmentBreedInputLayoutBinding binding;
    String selectDate;
    String selectCode;

    public BreedInputFragment() {}

    public BreedInputFragment(String selectCode, String selectDate){
        this.selectCode = selectCode;
        this.selectDate = selectDate;
    }

    public static BreedInputFragment newInstance(String selectCode, String selectDate){
        return new BreedInputFragment(selectCode, selectDate);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(getArguments() != null){

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        binding = FragmentBreedInputLayoutBinding.inflate(inflater);

        setFragmentData(container.getContext());

        return binding.getRoot();
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setFragmentData(Context context){

        JSONObject comeinDetail = DataCacheManager.getInstance().getCacheData(selectCode, "comeinDetail");

        try {
            String key = selectCode.split("_")[1];

            JSONObject dongBuffer = DataCacheManager.getInstance().getCacheData("buffer").getJSONObject(key);
            String name = dongBuffer.getString("fdName");

            binding.tvDongName.setText(name);
            binding.tvDate.setText(selectDate);
            binding.etAlreadyFeed.setText(dongBuffer.getString("cmAlreadyFeed"));
            binding.etIn.setText(dongBuffer.getString("cmInsu"));
            binding.etExtra.setText(dongBuffer.getString("cmExtraSu"));
            binding.tvType.setText(dongBuffer.getString("cmIntype"));

            JSONObject jo = comeinDetail.optJSONObject(selectDate);

//            Log.e("jo", jo.toString());

            if(jo == null){
                binding.tvDeath.setText("0");
                binding.tvCull.setText("0");
                binding.tvThinout.setText("0");
            }
            else{
                binding.tvDeath.setText(jo.optString("cdDeath"));
                binding.tvCull.setText(jo.optString("cdCull"));
                binding.tvThinout.setText(jo.optString("cdThinout"));
            }

            binding.btnCancel.setOnClickListener(view -> {
                PageManager.getInstance().movePage();
            });

            binding.btnSave.setOnClickListener(view -> {

                // comein_detail 인서트
                String cdCode = selectCode;
                String cdDate = selectDate;
                String cdDeath = binding.etDeath.getText().toString();
                String cdCull = binding.etCull.getText().toString();
                String cdThinout = binding.etThinout.getText().toString();

                // comein_master 업데이트
                String cmInsu = binding.etIn.getText().toString();
                String cmExtraSu = binding.etExtra.getText().toString();
                String cmAlreadyFeed = binding.etAlreadyFeed.getText().toString();

                DataCacheManager.getInstance().inputApiData("saveBreedData", new HashMap<String, String>() {{
                    put("cdCode", cdCode);
                    put("cdDate", cdDate);
                    put("cdDeath", cdDeath);
                    put("cdCull", cdCull);
                    put("cdThinout", cdThinout);

                    put("cmInsu", cmInsu);
                    put("cmExtraSu", cmExtraSu);
                    put("cmAlreadyFeed", cmAlreadyFeed);
                }});

                DataCacheManager.getInstance().deleteCache(selectCode, "comeinDetail");
                PageManager.getInstance().movePage();
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}

