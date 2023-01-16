package com.example.kokofarm_user_app;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.example.kokofarm_user_app.databinding.FragmentBreedBinding;
import com.example.kokofarm_user_app.databinding.FragmentBreedListBinding;
import com.example.kokofarm_user_app.kkf_utils.FloatCompute;
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
import java.util.Locale;

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBreedBinding.inflate(inflater);

        setFragmentData(container.getContext());

        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setFragmentData(Context context){

        // 선택된 농장이 없으면 관리자 화면으로 보냄
        if(DataCacheManager.getInstance().getSelectFarm().equals("")){
            PageManager.getInstance().movePage("manager");
            return;
        }
        // 상단 데이터 출력
        PageManager.getInstance().showFarmTopContents("");

        JSONObject buffer = DataCacheManager.getInstance().getCacheData("buffer");

//        Log.e("buffer", buffer.toString());

        int cnt = 0;
        double totalAvgWeight = 0.0;        // 농장 전체 평균중량
        double totalAvgDevi = 0.0;          // 농장 평균 표준편차
        List<Double> dongWeightList = new ArrayList<Double>();
        double dongDiff = 0.0;              // 동별 편차

        int currFeed = 0;                   // 오늘 급이량
        int prevFeed = 0;                   // 어제 급이량
        int allFeed = 0;                    // 전체 급이량
        int currWater = 0;                  // 오늘 급수량
        int prevWater = 0;                  // 어제 급수량
        int allWater = 0;                   // 전체 급수량

        int waterPerHour = 0;               // 1시간 급수량

        int feedMax = 0;                    // 사료빈 중량
        int feedRemain = 0;                 // 사료빈 잔량

        HashMap<String, List<Float>> dongMap = new HashMap<String, List<Float>>() {{
            put("avgWeight", new ArrayList<>());
            put("feed", new ArrayList<>());
            put("water", new ArrayList<>());
        }};

        // 동별 요약 카드뷰를 넣을 레이아웃 위치
        LinearLayout dongCardViewLay = binding.breedDongList;

        try {
            for (Iterator<String> it = buffer.keys(); it.hasNext(); ) {
                cnt++;

                String id = it.next();
                JSONObject dongJson = buffer.getJSONObject(id);

                DongCardView cdv = new DongCardView(context);
                cdv.initData(id);
                dongCardViewLay.addView(cdv);

                cdv.setOnClickListener(view -> {
                    PageManager.getInstance().eventSelectBar(id.substring(6));
                });

                dongWeightList.add(dongJson.getDouble("beAvgWeight"));
                totalAvgWeight += dongJson.getDouble("beAvgWeight");
                totalAvgDevi += dongJson.getDouble("beDevi");

                currFeed += dongJson.getInt("sfDailyFeed");
                prevFeed += dongJson.getInt("sfPrevFeed");
                allFeed += dongJson.getInt("sfAllFeed");

                currWater += dongJson.getInt("sfDailyWater");
                prevWater += dongJson.getInt("sfPrevWater");
                allWater += dongJson.getInt("sfAllWater");

                feedMax += dongJson.getInt("sfFeedMax");
                feedRemain += dongJson.getInt("sfFeed");

                JSONObject shFeedData = new JSONObject(dongJson.getString("shFeedData"));
                waterPerHour += shFeedData.getInt("feed_water");

                dongMap.get("avgWeight").add((float)dongJson.getDouble("beAvgWeight"));
                dongMap.get("feed").add((float)dongJson.getDouble("sfDailyFeed"));
                dongMap.get("water").add((float)dongJson.getDouble("sfDailyWater"));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

//        totalAvgWeight = FloatCompute.divide(totalAvgWeight, cnt);

//        for(double d : dongWeightList){
//            double diff = totalAvgWeight - d;
//            dongDiff += Math.pow(diff, 2);
//        }
//        dongDiff = Math.sqrt(FloatCompute.divide(dongDiff, cnt));       // 동별 편차
    }


    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }
}