package com.example.kokofarm_user_app;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kokofarm_user_app.databinding.FragmentDongBinding;
import com.example.kokofarm_user_app.manager.DataCacheManager;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DongFragment extends Fragment implements OnBackPressedListener {

    FragmentDongBinding binding;
    String id;

    public DongFragment() {

    }

    public DongFragment(String id) {
        this.id = id;
    }

    public static DongFragment newInstance(String id) {

        return new DongFragment(id);
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

        binding = FragmentDongBinding.inflate(inflater);
        setFragmentData(container.getContext());

        return binding.getRoot();
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onBackPressed(){
        
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setFragmentData(Context context){

        JSONObject buffer = DataCacheManager.getInstance().getJsonData("buffer");

        try {
            JSONObject dongJson = buffer.getJSONObject(id);
            
            double avgWeight = dongJson.getDouble("beAvgWeight");
            double devi = 0.0;                  // 표준편차
            double vc = 0.0;                    // 변이계수

            int currFeed = 0;                   // 오늘 급이량
            int prevFeed = 0;                   // 어제 급이량
            int allFeed = 0;                    // 전체 급이량
            int currWater = 0;                  // 오늘 급수량
            int prevWater = 0;                  // 어제 급수량
            int allWater = 0;                   // 전체 급수량

            int waterPerHour = 0;               // 1시간 급수량

            int comeinCount = 0;                // 입추수
            int deathCount = 0;                 // 폐사 수
            int cullCount = 0;                  // 도태 수
            int thinoutCount = 0;               // 솎기 수

            int feedMax = 0;                    // 사료빈 중량
            int feedRemain = 0;                 // 사료빈 잔량

            binding.dongTvDong.setText(dongJson.getString("beDongid") + "동");
            binding.dongTvDay.setText(dongJson.getString("interm") + "일령");
            binding.dongTvAvgWeight.setText(String.format(Locale.getDefault(), "%.1fg", dongJson.getDouble("beAvgWeight")));
            binding.dongTvDevi.setText(String.format(Locale.getDefault(), "%.1f", dongJson.getDouble("beDevi")));

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}