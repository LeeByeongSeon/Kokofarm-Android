package com.example.kokofarm_user_app;

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setFragmentData(Context context){

        Log.e("setFragmentData", "setFragmentData");

        JSONObject buffer = DataCacheManager.getInstance().getCacheData("buffer");

        try {
            JSONObject dongJson = buffer.getJSONObject(DataCacheManager.getInstance().getSelectFarm() + id);
            
            double avgWeight = dongJson.getDouble("beAvgWeight");
            double devi = 0.0;                  // ????????????
            double vc = 0.0;                    // ????????????

            int currFeed = 0;                   // ?????? ?????????
            int prevFeed = 0;                   // ?????? ?????????
            int allFeed = 0;                    // ?????? ?????????
            int currWater = 0;                  // ?????? ?????????
            int prevWater = 0;                  // ?????? ?????????
            int allWater = 0;                   // ?????? ?????????

            int waterPerHour = 0;               // 1?????? ?????????

            int comeinCount = dongJson.getInt("cmInsu") + dongJson.getInt("cmExtraSu");                // ?????????

            int deathCount = dongJson.getInt("cmDeathCount");                 // ?????? ???
            int cullCount = dongJson.getInt("cmCullCount");;                  // ?????? ???
            int thinoutCount = dongJson.getInt("cmThinoutCount");;               // ?????? ???

            int live = comeinCount - deathCount - cullCount - thinoutCount;

            int feedMax = 0;                    // ????????? ??????
            int feedRemain = 0;                 // ????????? ??????

//            Log.e("live", live + "");
//            Log.e("comeinCount", comeinCount + "");
//            Log.e("live/comeinCount", ((float)live/comeinCount) + "");

            binding.dongTvDong.setText(dongJson.getString("beDongid") + "???");
            binding.dongTvDay.setText(dongJson.getString("interm") + "??????");
            binding.dongTvAvgWeight.setText(String.format(Locale.getDefault(), "%.1fg", dongJson.getDouble("beAvgWeight")));
            binding.dongTvDevi.setText(String.format(Locale.getDefault(), "%.1f", dongJson.getDouble("beDevi")));

            // ????????????
            binding.cdvDongBreed.tvInsu.setText(Integer.toString(comeinCount));
            binding.cdvDongBreed.tvLive.setText(Integer.toString(live));
            binding.cdvDongBreed.tvLivePer.setText(String.format(Locale.getDefault(), "%.1f", ((float)live/comeinCount) * 100f) + "%");
            binding.cdvDongBreed.tvExtra.setText(dongJson.getString("cmExtraSu"));
            binding.cdvDongBreed.tvDeath.setText(Integer.toString(deathCount));
            binding.cdvDongBreed.tvCull.setText(Integer.toString(cullCount));
            binding.cdvDongBreed.tvThinout.setText(Integer.toString(thinoutCount));

            // ????????????
            binding.dongTvAvgTemp.setText(String.format(Locale.getDefault(), "%.1f", dongJson.getDouble("beAvgTemp")));
            binding.dongTvAvgHumi.setText(String.format(Locale.getDefault(), "%.1f", dongJson.getDouble("beAvgHumi")));
            binding.dongTvAvgCo2.setText(String.format(Locale.getDefault(), "%.1f", dongJson.getDouble("beAvgCo2")));
            binding.dongTvAvgNh3.setText(String.format(Locale.getDefault(), "%.1f", dongJson.getDouble("beAvgNh3")));
            binding.dongTvAvgDust.setText(String.format(Locale.getDefault(), "%.1f", dongJson.getDouble("beAvgDust")));

//            Log.e("dongJson", dongJson.toString());


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