package com.example.kokofarm_user_app;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.kokofarm_user_app.databinding.FragmentHomeBinding;
import com.example.kokofarm_user_app.kkf_utils.BackTasker;
import com.example.kokofarm_user_app.kkf_utils.FloatCompute;
import com.example.kokofarm_user_app.manager.DataCacheManager;
import com.example.kokofarm_user_app.piece.DongCardView;
import com.github.mikephil.charting.charts.CombinedChart;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater);

        Log.e("onCreateView", "onCreateView");

        binding.homeCdvFarmComein.setOnClickListener(this::onClick);
        //binding.homeDongList.homeDong1.setOnClickListener(this::onClick);

        DataCacheManager.getInstance().loadBufferData();

        setFragmentData(container.getContext());

        return binding.getRoot();
    }

    // fragment 메모리 누수 방지
    @Override
    public void onDestroyView(){
//        Log.e("onDestroyView", "onDestroyView: ");
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.home_cdv_farm_comein:
                if(binding.homeLoDeath.getVisibility() != view.VISIBLE){
                    binding.homeCdvFarmComein.setRippleColorResource(R.color.opacity05_bl);
                    setAutoTransition(binding.homeLoDeath, view.VISIBLE);
                    setAutoTransition(binding.homeLoCull, view.VISIBLE);
                    setAutoTransition(binding.homeLoThinout, view.VISIBLE);
                    binding.homeAccordion.setImageResource(R.drawable.ic_baseline_arrow_up_24);
                } else {
                    setAutoTransition(binding.homeLoDeath, view.GONE);
                    setAutoTransition(binding.homeLoCull, view.GONE);
                    setAutoTransition(binding.homeLoThinout, view.GONE);
                    binding.homeAccordion.setImageResource(R.drawable.ic_baseline_arrow_down_24);
                }

                break;

//            case R.id.home_dong_1:
////                ((MainActivity)getActivity()).replaceFragment(DongFragment.newInstance());
//                moveDongFragment();
//
//                break;
        }
    }

    @Override
    public void onBackPressed(){
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setFragmentData(Context context){
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

        int comeinCount = 0;                // 입추수
        int deathCount = 0;                 // 폐사 수
        int cullCount = 0;                  // 도태 수
        int thinoutCount = 0;               // 솎기 수

        int feedMax = 0;                    // 사료빈 중량
        int feedRemain = 0;                 // 사료빈 잔량

        HashMap<String, List<Float>> dongMap = new HashMap<String, List<Float>>() {{
            put("avgWeight", new ArrayList<>());
            put("feed", new ArrayList<>());
            put("water", new ArrayList<>());
        }};

        // 동별 요약 카드뷰를 넣을 레이아웃 위치
        LinearLayout dongCardViewLay = binding.homeDongBody;

        try {
            for (Iterator<String> it = buffer.keys(); it.hasNext(); ) {
                cnt++;

                String id = it.next();
                JSONObject dongJson = buffer.getJSONObject(id);

                DongCardView cdv = new DongCardView(context);
                cdv.initData(id);
                dongCardViewLay.addView(cdv);

                cdv.setOnClickListener(view -> {
                    ((MainActivity)getActivity()).replaceFragment(DongFragment.newInstance(id));
                    moveDongFragment();
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

                comeinCount += dongJson.getInt("cmInsu");
                deathCount += dongJson.getInt("cmDeathCount");
                cullCount += dongJson.getInt("cmCullCount");
                thinoutCount += dongJson.getInt("cmThinoutCount");

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

        totalAvgWeight = FloatCompute.divide(totalAvgWeight, cnt);

        for(double d : dongWeightList){
            double diff = totalAvgWeight - d;
            dongDiff += Math.pow(diff, 2);
        }
        dongDiff = Math.sqrt(FloatCompute.divide(dongDiff, cnt));       // 동별 편차

        // 데이터 입력 작업
//        binding.homeTvFarmAvgMin.setText(String.format("%.1f", totalAvgWeight - totalAvgDevi) + "g");
//        binding.homeTvFarmAvgMax.setText(String.format("%.1f", totalAvgWeight + totalAvgDevi) + "g");

        binding.homeTvFarmComein.setText(comeinCount + "수");
        binding.homeTvFarmDeath.setText(deathCount + "수");
        binding.homeTvFarmCull.setText(cullCount + "수");
        binding.homeTvFarmThinout.setText(thinoutCount + "수");

        binding.homeFeedWater.homeTvAllFeed.setText("" + allFeed);
        binding.homeFeedWater.homeTvCurrFeed.setText("" + currFeed);
        binding.homeFeedWater.homeTvPrevFeed.setText("" + prevFeed);
        binding.homeFeedWater.homeTvAllWater.setText("" + allWater);
        binding.homeFeedWater.homeTvCurrWater.setText("" + currWater);
        binding.homeFeedWater.homeTvPrevWater.setText("" + prevWater);

        //DataCacheManager.getInstance().getFeedPerData("KF0071");

        String feedPerJson = DataCacheManager.getInstance().getFeedPerData("KF0071");

        Log.e("feedPerJson", feedPerJson);

        JSONObject avgJson = DataCacheManager.getInstance().getCacheData("avgWeight", new HashMap<String, String>() {{
            put("userType", "user");
            put("userID", "kk0071");
            put("setComm", "avgWeight");
            put("code", "20220312082856_KF007101");
        }});

//        CombinedChart chart = binding.dongChart1;
//        CombinedChartMaker maker = new CombinedChartMaker(chart);
//        maker.setDateStyle(60*60f, "MM-dd HH:mm");
//        maker.makeTimeLineChart(avgJson,
//                new HashMap<String, String>() {{
//                    put("awWeight", "평균중량");
//                    put("refWeight", "권고중량");
//                }});
//        maker.setMarker(context, R.layout.marker_text_view);
//        chart.invalidate();

        CombinedChart chart = binding.dongChart1;
        CombinedChartMaker maker = new CombinedChartMaker(chart);
        maker.makeSimpleChart("평균중량", dongMap.get("avgWeight") ,0);
        maker.setMarker(context, R.layout.marker_text_view);
        chart.invalidate();

        chart = binding.dongChart2.combineChart;
        maker = new CombinedChartMaker(chart);
        maker.makeSimpleChart("일일 급이량", dongMap.get("feed"), 1);
        maker.setMarker(context, R.layout.marker_text_view);
        chart.invalidate();

        chart = binding.dongChart3.combineChart;
        maker = new CombinedChartMaker(chart);
        maker.makeSimpleChart("일일 급수량", dongMap.get("water"), 2);
        maker.setMarker(context, R.layout.marker_text_view);
        chart.invalidate();

    }

    // view visible, gone
    public void setAutoTransition(ViewGroup viewGroup, int visibility){
        TransitionManager.beginDelayedTransition(viewGroup, new AutoTransition());
        viewGroup.setVisibility(visibility);
    }

    public void moveDongFragment(){
        Navigation.findNavController(binding.getRoot()).navigate(R.id.nav_dong);
        // 스크롤 설정 필요
    }
}