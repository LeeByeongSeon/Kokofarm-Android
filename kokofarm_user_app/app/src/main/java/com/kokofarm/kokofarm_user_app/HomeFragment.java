package com.kokofarm.kokofarm_user_app;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.kokofarm.kokofarm_user_app.databinding.FragmentHomeBinding;
import com.kokofarm.kokofarm_user_app.kkf_utils.FloatCompute;
import com.kokofarm.kokofarm_user_app.manager.DataCacheManager;
import com.kokofarm.kokofarm_user_app.manager.PageManager;
import com.kokofarm.kokofarm_user_app.piece.DongCardView;
import com.github.mikephil.charting.charts.CombinedChart;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    DecimalFormat decimalFormat = new DecimalFormat("#.###");

    public HomeFragment() {

    }

    public static HomeFragment newInstance() {

        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.e("onCreate", "onCreate: Home");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater);

        // 화면 맨 위로
//        final View.OnClickListener listener = new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                binding.scrollView.fullScroll(View.FOCUS_UP);
//            }
//        };
////        PageManager.getInstance().setScrollUpBtnEvent(view -> binding.scrollView.fullScroll(View.FOCUS_UP));
//        PageManager.getInstance().setScrollUpBtnEvent(listener);

        Log.e("onCreateView", "onCreateView: Home");

        binding.cdvHomeBreed.layCdv.setOnClickListener(view -> {
            toggleBreedInfo();
        });

        setFragmentData(container.getContext());

//        // FCR Slider 관련
//        Slider slider = binding.homeRsFcr.sdFcr;
//        slider.addOnSliderTouchListener(sliderTouchListener);

        return binding.getRoot();
    }

    // fcr 값
//    public void setFcrVal(){
//        float fcrVal = binding.homeRsFcr.sdFcr.getValue();
//        String fcrStr = decimalFormat.format(fcrVal);
//        binding.homeRsFcr.tvSelectFcr.setText(fcrStr);
//
//        String feedStr = binding.homeFeedWater.tvFeedPer.toString();
//        float feed = Float.parseFloat(feedStr);
//        String mean = decimalFormat.format(feed / fcrVal);
//        binding.homeRsFcr.tvMean.setText(mean);
//    }

    public void setFcrVal(float val){

        if(val < 0.01f){
            return;
        }

//        binding.homeRsFcr.sdFcr.setValue(val);
//        String fcrStr = decimalFormat.format(val);
//        binding.homeRsFcr.tvSelectFcr.setText(fcrStr);

        String feedStr = binding.homeFeedWater.tvFeedPer.getText().toString();
        feedStr = feedStr.substring(0, feedStr.length() - 2);

//        float feed = Float.parseFloat(feedStr);
//        String mean = String.format("%.1f", feed / val);
//        binding.homeRsFcr.tvMean.setText(mean);

    }

    // FCR Slider 관련
//    private final Slider.OnSliderTouchListener sliderTouchListener =
//        new Slider.OnSliderTouchListener() {
//            @SuppressLint("RestrictedApi")
//            @Override
//            public void onStartTrackingTouch(Slider slider) {
//                Log.d("StartTrack", "Start");
//                float fcrVal = binding.homeRsFcr.sdFcr.getValue();
//                setFcrVal(fcrVal);
//            }
//
//            @SuppressLint("RestrictedApi")
//            public void onStopTrackingTouch(Slider slider) {
//                Log.d("StopTrack", "Stop");
//                float fcrVal = binding.homeRsFcr.sdFcr.getValue();
//                setFcrVal(fcrVal);
//            }
//        };

    // fragment 메모리 누수 방지
    @Override
    public void onDestroyView(){
        Log.e("onDestroyView", "onDestroyView: Home");
        super.onDestroyView();
        binding = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setFragmentData(Context context){

        // 선택된 농장이 없으면 관리자 화면으로 보냄
        if(DataCacheManager.getInstance().getSelectFarm().equals("")){
            PageManager.getInstance().movePage("manager");
            return;
        }

        JSONObject buffer = DataCacheManager.getInstance().getCacheData("buffer");

        // 동 선택버튼 생성
        PageManager.getInstance().setSelectBar(buffer);

//        Log.e("buffer", buffer.toString());

        String fName = "";                  // 농장 이름
        
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
        int extraCount = 0;                 // 덤 수
        int deathCount = 0;                 // 폐사 수
        int cullCount = 0;                  // 도태 수
        int thinoutCount = 0;               // 솎기 수

        int feedMax = 0;                    // 사료빈 중량
        int feedRemain = 0;                 // 사료빈 잔량

        float farmPerFeed = 0f;                // 농장 전체 수 당 급이량
        float farmPerWater = 0f;               // 농장 전체 수 당 급수량
        float farmFcr = 0f;                 // 농장 최고 일령 FCR 값
        float farmFcrWeight = 0f;           // 농장 FCR 예측중량

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
                    PageManager.getInstance().eventSelectBar(id.substring(6));
                });

                fName = dongJson.getString("fName");

                dongWeightList.add(dongJson.optDouble("beAvgWeight"));
                totalAvgWeight += dongJson.optDouble("beAvgWeight");
                totalAvgDevi += dongJson.optDouble("beDevi");

                currFeed += dongJson.optInt("sfDailyFeed");
                prevFeed += dongJson.optInt("sfPrevFeed");
                allFeed += dongJson.optInt("sfAllFeed");

                currWater += dongJson.optInt("sfDailyWater");
                prevWater += dongJson.optInt("sfPrevWater");
                allWater += dongJson.optInt("sfAllWater");

                comeinCount += dongJson.optInt("cmInsu") + dongJson.optInt("cmExtraSu");
                deathCount += dongJson.optInt("cmDeathCount");
                cullCount += dongJson.optInt("cmCullCount");
                thinoutCount += dongJson.optInt("cmThinoutCount");

                feedMax += dongJson.optInt("sfFeedMax");
                feedRemain += dongJson.optInt("sfFeed");

                String shFeedDataStr = dongJson.getString("shFeedData");
                if(shFeedDataStr.equals("") || shFeedDataStr.equals("0")){
                    waterPerHour = 0;
                }
                else{
                    JSONObject shFeedData = new JSONObject(dongJson.getString("shFeedData"));
                    waterPerHour += shFeedData.optInt("feed_water");
                }

                dongMap.get("avgWeight").add((float)dongJson.optDouble("beAvgWeight"));
                dongMap.get("feed").add((float)dongJson.optDouble("sfDailyFeed"));
                dongMap.get("water").add((float)dongJson.optDouble("sfDailyWater"));

                farmPerFeed = (float) dongJson.optDouble("fFeedPer");
                farmPerWater = (float) dongJson.optDouble("fWaterPer");
                farmFcrWeight = (float) dongJson.optDouble("fFcrWeight");

                // FCR 계산 - 오류방지
                if(farmPerFeed > 10f && farmFcrWeight > 10f){
                    farmFcr = (farmPerFeed / farmFcrWeight);
                    farmFcr = Math.round(farmFcr * 1000f) / 1000f;
                }
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

        int live = comeinCount - deathCount - cullCount - thinoutCount;

        // 생육정보
        binding.cdvHomeBreed.tvInsu.setText(Integer.toString(comeinCount));
        binding.cdvHomeBreed.tvLive.setText(Integer.toString(live));
        binding.cdvHomeBreed.tvLivePer.setText(String.format(Locale.getDefault(), "%.1f", ((float)live/comeinCount) * 100f) + "%");
        binding.cdvHomeBreed.tvExtra.setText(Integer.toString(extraCount));
        binding.cdvHomeBreed.tvDeath.setText(Integer.toString(deathCount));
        binding.cdvHomeBreed.tvCull.setText(Integer.toString(cullCount));
        binding.cdvHomeBreed.tvThinout.setText(Integer.toString(thinoutCount));

        // 급이 급수 정보
        binding.homeFeedWater.tvAllFeed.setText("" + allFeed);
        binding.homeFeedWater.tvCurrFeed.setText("" + currFeed + "(Kg)");
        binding.homeFeedWater.tvPrevFeed.setText("" + prevFeed + "(Kg)");
        binding.homeFeedWater.tvRemainFeed.setText("" + feedRemain + "(Kg)");
        binding.homeFeedWater.tvAllWater.setText("" + allWater);
        binding.homeFeedWater.tvCurrWater.setText("" + currWater + "(L)");
        binding.homeFeedWater.tvPrevWater.setText("" + prevWater + "(L)");
        binding.homeFeedWater.tvHourWater.setText("" + waterPerHour + "(L)");

        binding.homeFeedWater.tvFeedPer.setText(String.format(Locale.getDefault(), "%.1fg", farmPerFeed));
        binding.homeFeedWater.tvWaterPer.setText(String.format(Locale.getDefault(), "%.3fL", farmPerWater));

        // topContents 설정
        PageManager.getInstance().setTopContentsData(
                fName,
                String.format(Locale.getDefault(), "%.1fg", totalAvgWeight),
                live + getResources().getString(R.string.cnt_txt),
                String.format(Locale.getDefault(), "%.1fg", dongDiff)
        );
        PageManager.getInstance().showFarmTopContents("");

        // FCR 관련 정보
        binding.homeRsFcr.tvStandardFcr.setText(Float.toString(farmFcr));
        binding.homeRsFcr.tvFcrWeight.setText(Float.toString(farmFcrWeight));

        // 슬라이더 범위 설정
        float from = farmFcr > 1 ? farmFcr - 1 : 0;
        float to = farmFcr + 1;
//        binding.homeRsFcr.sdFcr.setValueFrom(from);
//        binding.homeRsFcr.sdFcr.setValueTo(to);
        setFcrVal(farmFcr);

        // 동별 평균중량 비교
        CombinedChart chart = binding.dongChart1;
        CombinedChartMaker maker = new CombinedChartMaker(chart);
        maker.makeSimpleChart(PageManager.getInstance().getRString(R.string.avg_weight), dongMap.get("avgWeight") ,0);
        maker.setMarker(context, R.layout.marker_text_view);
        chart.invalidate();

        // 동별 급이량 비교
        chart = binding.dongChart2.combineChart;
        maker = new CombinedChartMaker(chart);
        maker.makeSimpleChart(PageManager.getInstance().getRString(R.string.today_feed), dongMap.get("feed"), 1);
        maker.setMarker(context, R.layout.marker_text_view);
        chart.invalidate();

        // 동별 급수량 비교
        chart = binding.dongChart3.combineChart;
        maker = new CombinedChartMaker(chart);
        maker.makeSimpleChart(PageManager.getInstance().getRString(R.string.today_water), dongMap.get("water"), 2);
        maker.setMarker(context, R.layout.marker_text_view);
        chart.invalidate();

    }

    // view visible, gone
    public void setAutoTransition(ViewGroup viewGroup, int visibility){
        TransitionManager.beginDelayedTransition(viewGroup, new AutoTransition());
        viewGroup.setVisibility(visibility);
    }

    private void toggleBreedInfo(){
        if(binding.cdvHomeBreed.layExtra.getVisibility() == View.GONE){
            binding.cdvHomeBreed.layExtra.setVisibility(View.VISIBLE);
            binding.cdvHomeBreed.layDeath.setVisibility(View.VISIBLE);
            binding.cdvHomeBreed.layCull.setVisibility(View.VISIBLE);
            binding.cdvHomeBreed.layThinout.setVisibility(View.VISIBLE);
            binding.cdvHomeBreed.imgAccordion.setImageResource(R.drawable.ic_baseline_arrow_up_24);
        }
        else{
            binding.cdvHomeBreed.layExtra.setVisibility(View.GONE);
            binding.cdvHomeBreed.layDeath.setVisibility(View.GONE);
            binding.cdvHomeBreed.layCull.setVisibility(View.GONE);
            binding.cdvHomeBreed.layThinout.setVisibility(View.GONE);
            binding.cdvHomeBreed.imgAccordion.setImageResource(R.drawable.ic_baseline_arrow_down_24);
        }
    }
}