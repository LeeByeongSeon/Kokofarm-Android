package com.example.kokofarm_user_app;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
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

public class BreedListFragment extends Fragment {

    private FragmentBreedListBinding binding;
    private RecyclerView recyclerView;
    String id;

    private List<BreedCardView> breedCardList = new ArrayList<>();
    final int pager = 5;        // 한페이지에 표시할 카드 수
    int currPage = 1;
    boolean order = false;      // true : 오름차순, false : 내림차순

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        binding = FragmentBreedListBinding.inflate(inflater);

        setFragmentData(container.getContext());

        int maxPage = (int) Math.ceil(breedCardList.size() / pager);

        // 카드뷰 페이징 버튼 처리
        binding.breedTopPaging.btnPrev.setOnClickListener(view -> {
            printCardView(Math.max(currPage - 1, 1));
        });
        binding.breedTopPaging.btnNext.setOnClickListener(view -> {
            printCardView(Math.min(currPage + 1, maxPage));
        });
        binding.breedBottomPaging.btnPrev.setOnClickListener(view -> {
            printCardView(Math.max(currPage - 1, 1));
        });
        binding.breedBottomPaging.btnNext.setOnClickListener(view -> {
            printCardView(Math.min(currPage + 1, maxPage));
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView(){
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
        // 상단 데이터 출력
        PageManager.getInstance().showFarmTopContents(id);

        JSONObject buffer = DataCacheManager.getInstance().getCacheData("buffer");

        try {
            JSONObject dongJson = buffer.getJSONObject(DataCacheManager.getInstance().getSelectFarm() + id);
            String code = dongJson.getString("beComeinCode");
            int live = dongJson.getInt("cmInsu") + dongJson.getInt("cmExtraSu");
            JSONObject comeinDetail = DataCacheManager.getInstance().getCacheData(code, "comeinDetail");

            String startDate = dongJson.getString("cmIndate").substring(0, 10);
            String endDate = dongJson.optString("cmOutdate");
            endDate = !endDate.equals("") ? endDate : DateUtil.get_inst().get_now(); // 입추상태면 현재시간까지
            endDate = DateUtil.get_inst().get_plus_minus_minute_time(endDate, 24 * 60);
            endDate = endDate.substring(0, 10);

            JSONObject emptyJson = new JSONObject();
            emptyJson.put("cdDeath", 0);
            emptyJson.put("cdCull", 0);
            emptyJson.put("cdThinout", 0);

            int cnt = 1;
            while(!startDate.equals(endDate)) {
                JSONObject jo = comeinDetail.optJSONObject(startDate);

                BreedCardView bcv = new BreedCardView(context);

                if(jo == null){
                    bcv.initData(startDate, live, cnt, emptyJson);
                }
                else{
                    live -= jo.optInt("cdDeath");
                    live -= jo.optInt("cdCull");
                    live -= jo.optInt("cdThinout");
                    bcv.initData(startDate, live, cnt, jo);
                }

//                breedCardZone.addView(bcv);

                String finalStartDate = startDate;
                bcv.setOnClickListener(view -> {
                    PageManager.getInstance().eventBreedCardSelect(code, finalStartDate);
                });

                breedCardList.add(bcv);

                cnt++;
                startDate = DateUtil.get_inst().get_plus_minus_minute_time(startDate + " 00:00:00", 24 * 60);
                startDate = startDate.substring(0, 10);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        printCardView(1);
    }

    // 해당 인덱스의 카드뷰를 출력
    private void printCardView(int page){

        currPage = page;

        LinearLayout breedCardZone = binding.breedCardZone;
        breedCardZone.removeAllViews();

        if(order){
            int start = pager * (page - 1);
            int end = Math.min(start + pager, breedCardList.size());
            for (int i=start; i<end; i++){
                breedCardZone.addView(breedCardList.get(i));
            }
        }
        else{
            int start = breedCardList.size() - pager * (page - 1) - 1;
            int end = Math.max(start - pager, 0);
            for (int i=start; i>=end; i--){
                breedCardZone.addView(breedCardList.get(i));
            }
        }

    }
}
