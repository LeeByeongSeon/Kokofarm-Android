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
        JSONObject buffer = DataCacheManager.getInstance().getCacheData("buffer");

//        Log.e("buffer", buffer.toString());

        int cnt = 0;
        double totalAvgWeight = 0.0;        // ?????? ?????? ????????????
        double totalAvgDevi = 0.0;          // ?????? ?????? ????????????
        List<Double> dongWeightList = new ArrayList<Double>();
        double dongDiff = 0.0;              // ?????? ??????

        int currFeed = 0;                   // ?????? ?????????
        int prevFeed = 0;                   // ?????? ?????????
        int allFeed = 0;                    // ?????? ?????????
        int currWater = 0;                  // ?????? ?????????
        int prevWater = 0;                  // ?????? ?????????
        int allWater = 0;                   // ?????? ?????????

        int waterPerHour = 0;               // 1?????? ?????????

        int feedMax = 0;                    // ????????? ??????
        int feedRemain = 0;                 // ????????? ??????

        HashMap<String, List<Float>> dongMap = new HashMap<String, List<Float>>() {{
            put("avgWeight", new ArrayList<>());
            put("feed", new ArrayList<>());
            put("water", new ArrayList<>());
        }};

        // ?????? ?????? ???????????? ?????? ???????????? ??????
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

        totalAvgWeight = FloatCompute.divide(totalAvgWeight, cnt);

        for(double d : dongWeightList){
            double diff = totalAvgWeight - d;
            dongDiff += Math.pow(diff, 2);
        }
        dongDiff = Math.sqrt(FloatCompute.divide(dongDiff, cnt));       // ?????? ??????

        JSONObject avgJson = DataCacheManager.getInstance().getCacheData("avgWeight", new HashMap<String, String>() {{
            put("userType", "user");
            put("userID", "kk0071");
            put("setComm", "avgWeight");
            put("code", "20220312082856_KF007101");
        }});
    }


    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }
}