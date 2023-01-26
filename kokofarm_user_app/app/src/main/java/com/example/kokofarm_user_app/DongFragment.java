package com.example.kokofarm_user_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.example.kokofarm_user_app.databinding.FragmentDongBinding;
import com.example.kokofarm_user_app.manager.DataCacheManager;
import com.example.kokofarm_user_app.manager.PageManager;

import org.json.JSONException;
import org.json.JSONObject;

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
        Log.e("onCreate", "onCreate: Dong");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.e("onCreateView", "onCreateView: Dong");

        binding = FragmentDongBinding.inflate(inflater);
        setFragmentData(container.getContext());

        binding.cdvDongBreed.layCdv.setOnClickListener(view -> {
            toggleBreedInfo();
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView(){
        Log.e("onDestroyView", "onDestroyView: Dong");
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onBackPressed(){
        
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setFragmentData(Context context){

        // 선택된 농장이 없으면 관리자 화면으로 보냄
        if(DataCacheManager.getInstance().getSelectFarm().equals("")){
            PageManager.getInstance().movePage("manager");
            return;
        }
        // 상단 데이터 출력
        PageManager.getInstance().showFarmTopContents(id);

        Log.e("setFragmentData", "setFragmentData");

        JSONObject buffer = DataCacheManager.getInstance().getCacheData("buffer");

        try {
            JSONObject dongJson = buffer.getJSONObject(DataCacheManager.getInstance().getSelectFarm() + id);
            
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

            int comeinCount = dongJson.getInt("cmInsu") + dongJson.getInt("cmExtraSu");                // 입추수

            int deathCount = dongJson.getInt("cmDeathCount");                 // 폐사 수
            int cullCount = dongJson.getInt("cmCullCount");;                  // 도태 수
            int thinoutCount = dongJson.getInt("cmThinoutCount");;               // 솎기 수

            int live = comeinCount - deathCount - cullCount - thinoutCount;

            int feedMax = 0;                    // 사료빈 중량
            int feedRemain = 0;                 // 사료빈 잔량

//            Log.e("live", live + "");
//            Log.e("comeinCount", comeinCount + "");
//            Log.e("live/comeinCount", ((float)live/comeinCount) + "");

//            Log.e("dongJson", dongJson.toString());

            int interm = dongJson.getInt("beInterm");

            binding.dongTvDong.setText(dongJson.getString("beDongid") + PageManager.getInstance().getRString(R.string.dong_txt));
            binding.dongTvDay.setText(interm + PageManager.getInstance().getRString(R.string.day_txt));
            binding.dongTvAvgWeight.setText(String.format(Locale.getDefault(), "%.1fg", dongJson.getDouble("beAvgWeight")));
            binding.dongTvDevi.setText(String.format(Locale.getDefault(), "%.1f", dongJson.getDouble("beDevi")));
            binding.dongTvComeinDate.setText(dongJson.getString("cmIndate"));

            // 생육정보
            binding.cdvDongBreed.tvInsu.setText(Integer.toString(comeinCount));
            binding.cdvDongBreed.tvLive.setText(Integer.toString(live));
            binding.cdvDongBreed.tvLivePer.setText(String.format(Locale.getDefault(), "%.1f", ((float)live/comeinCount) * 100f) + "%");
            binding.cdvDongBreed.tvExtra.setText(dongJson.getString("cmExtraSu"));
            binding.cdvDongBreed.tvDeath.setText(Integer.toString(deathCount));
            binding.cdvDongBreed.tvCull.setText(Integer.toString(cullCount));
            binding.cdvDongBreed.tvThinout.setText(Integer.toString(thinoutCount));

            // 어제 및 예측중량
            binding.dongTvMeanDay2.setText((interm + 1) + PageManager.getInstance().getRString(R.string.day_txt));
            binding.dongTvMeanDay3.setText((interm + 2) + PageManager.getInstance().getRString(R.string.day_txt));

            double w = dongJson.getDouble("awWeight");         //어제
            double m1 = dongJson.getDouble("awEstiT1");         //오늘 예측()
            double m2 = dongJson.getDouble("awEstiT2");         //내일
            double m3 = dongJson.getDouble("awEstiT3");         //모레

            binding.dongTvMeanWeight1.setText(String.format(Locale.getDefault(), "%.1fg", w));      //어제
            binding.dongTvMeanWeight2.setText(String.format(Locale.getDefault(), "%.1fg", m2));      //내일
            binding.dongTvMeanWeight3.setText(String.format(Locale.getDefault(), "%.1fg", m3));      //모레

            binding.dongTvMeanDiff2.setText(String.format(Locale.getDefault(), "%.1f", m2 - m1));
            binding.dongTvMeanDiff3.setText(String.format(Locale.getDefault(), "%.1f", m3 - m2));

            // 센서정보
            binding.dongTvAvgTemp.setText(String.format(Locale.getDefault(), "%.1f", dongJson.getDouble("beAvgTemp")));
            binding.dongTvAvgHumi.setText(String.format(Locale.getDefault(), "%.1f", dongJson.getDouble("beAvgHumi")));
            binding.dongTvAvgCo2.setText(String.format(Locale.getDefault(), "%.1f", dongJson.getDouble("beAvgCo2")));
            binding.dongTvAvgNh3.setText(String.format(Locale.getDefault(), "%.1f", dongJson.getDouble("beAvgNh3")));
            binding.dongTvAvgDust.setText(String.format(Locale.getDefault(), "%.1f", dongJson.getDouble("beAvgDust")));

            // 급이 급수
            binding.dongFeedWater.tvAllFeed.setText(dongJson.getInt("sfAllFeed") + "");
            binding.dongFeedWater.tvCurrFeed.setText(dongJson.getInt("sfDailyFeed") + " (Kg)");
            binding.dongFeedWater.tvPrevFeed.setText(dongJson.getInt("sfPrevFeed") + " (Kg)");
            binding.dongFeedWater.tvRemainFeed.setText(dongJson.getInt("sfFeed") + " (Kg)");

            binding.dongFeedWater.tvAllWater.setText(dongJson.getInt("sfAllWater") + "");
            binding.dongFeedWater.tvCurrWater.setText(dongJson.getInt("sfDailyWater") + " (L)");
            binding.dongFeedWater.tvPrevWater.setText(dongJson.getInt("sfPrevWater") + " (L)");

            binding.dongFeedWater.tvFeedPer.setText(String.format(Locale.getDefault(), "%.1fg", dongJson.getDouble("sfFeedPer")));
            binding.dongFeedWater.tvWaterPer.setText(String.format(Locale.getDefault(), "%.3fL", dongJson.getDouble("sfWaterPer")));

            JSONObject shFeedData = new JSONObject(dongJson.optString("shFeedData"));
            binding.dongFeedWater.tvHourWater.setText(shFeedData.getInt("feed_water") + " (L)");


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void toggleBreedInfo(){

        if(binding.cdvDongBreed.layExtra.getVisibility() == View.GONE){
            binding.cdvDongBreed.layExtra.setVisibility(View.VISIBLE);
            binding.cdvDongBreed.layDeath.setVisibility(View.VISIBLE);
            binding.cdvDongBreed.layCull.setVisibility(View.VISIBLE);
            binding.cdvDongBreed.layThinout.setVisibility(View.VISIBLE);
            binding.cdvDongBreed.imgAccordion.setImageResource(R.drawable.ic_baseline_arrow_up_24);
        }
        else{
            binding.cdvDongBreed.layExtra.setVisibility(View.GONE);
            binding.cdvDongBreed.layDeath.setVisibility(View.GONE);
            binding.cdvDongBreed.layCull.setVisibility(View.GONE);
            binding.cdvDongBreed.layThinout.setVisibility(View.GONE);
            binding.cdvDongBreed.imgAccordion.setImageResource(R.drawable.ic_baseline_arrow_down_24);
        }
    }
}