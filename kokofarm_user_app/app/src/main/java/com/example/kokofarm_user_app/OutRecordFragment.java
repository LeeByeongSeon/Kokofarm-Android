package com.example.kokofarm_user_app;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.kokofarm_user_app.databinding.FragmentOutRecordBinding;
import com.example.kokofarm_user_app.kkf_utils.FloatCompute;
import com.example.kokofarm_user_app.manager.DataCacheManager;
import com.example.kokofarm_user_app.manager.PageManager;
import com.example.kokofarm_user_app.piece.ComeoutCardView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class OutRecordFragment extends Fragment {

    private FragmentOutRecordBinding binding;

    // Fragment manager
    private FragmentManager fragmentManager;

    final int pager = 20;
    int currPage = 1;
    int maxPage = 1;

    Context context = null;

    public OutRecordFragment() {

    }

    public static OutRecordFragment newInstance() {

        return new OutRecordFragment();
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
        binding = FragmentOutRecordBinding.inflate(inflater);

        setFragmentData(container.getContext());

        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setFragmentData(Context context){

        this.context = context;

        JSONObject comeoutList;

        // 선택된 농장이 없을 경우
        if(DataCacheManager.getInstance().getSelectFarm().equals("")){
            comeoutList = DataCacheManager.getInstance().getComeoutList("", 0, 10);
        }
        else{
            comeoutList = DataCacheManager.getInstance().getComeoutList(DataCacheManager.getInstance().getSelectFarm(), 0, 10);
        }

        try {
            maxPage = (int) Math.ceil(comeoutList.getInt("cnt") / pager);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 카드뷰 페이징 버튼 처리
        binding.outRecordTopPaging.btnPrev.setOnClickListener(view -> {
            showPage(Math.max(currPage - 1, 1));
        });
        binding.outRecordTopPaging.btnNext.setOnClickListener(view -> {
            showPage(Math.min(currPage + 1, maxPage));
        });
        binding.outRecordBottomPaging.btnPrev.setOnClickListener(view -> {
            showPage(Math.max(currPage - 1, 1));
        });
        binding.outRecordBottomPaging.btnNext.setOnClickListener(view -> {
            showPage(Math.min(currPage + 1, maxPage));
        });

        showPage(1);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showPage(int page){

        currPage = page;

        // Limit의 인수를 설정 - 추후에 페이지 오버 시 적용
        int start = pager * (page - 1);
        int end = pager;

        JSONObject comeoutList;

        // 선택된 농장이 없을 경우
        if(DataCacheManager.getInstance().getSelectFarm().equals("")){
            comeoutList = DataCacheManager.getInstance().getComeoutList("", start, end);
        }
        else{
            comeoutList = DataCacheManager.getInstance().getComeoutList(DataCacheManager.getInstance().getSelectFarm(), start, end);
        }

        int comeoutCnt = 0;
        double avgDay = 0d;
        double avgWeight = 0d;

        LinearLayout outRecordList = binding.outRecordList;
        outRecordList.removeAllViews();

        try {
            int cnt = 0;
            int dongCnt = comeoutList.getInt("dongCnt");        // 전체 동 수
            comeoutCnt = comeoutList.getInt("cnt");             // 전체 출하 횟수
            int chechMax = dongCnt * 5;

            for (Iterator<String> it = comeoutList.keys(); it.hasNext(); ){

                String code = it.next();

                if(code.equals("dongCnt") || code.equals("cnt")){
                    continue;
                }

                JSONObject record = comeoutList.getJSONObject(code);

                ComeoutCardView ccv = new ComeoutCardView(context);
                ccv.initData(record);
                outRecordList.addView(ccv);

                if(cnt < chechMax){
                    avgDay += record.getInt("interm");
                    avgWeight += record.getDouble("awWeight");
                }

                cnt++;

            }

            if(cnt > 0){
                cnt = Math.min(cnt, chechMax);

                avgDay = FloatCompute.divide(avgDay, cnt);            // 5회차 평균 일령
                avgWeight = FloatCompute.divide(avgWeight, cnt);      // 5회차 평균 중량
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 선택된 농장이 없을 경우
        if(DataCacheManager.getInstance().getSelectFarm().equals("")){
            PageManager.getInstance().showManagerTopContents(PageManager.getInstance().getRString(R.string.release_history));
        }
        else{
            PageManager.getInstance().setTopContentsCollapsing(PageManager.getInstance().getRString(R.string.release_history));
            PageManager.getInstance().setTopLeftTitle(PageManager.getInstance().getRString(R.string.release_count));
            PageManager.getInstance().setTopLeftContents(comeoutCnt + "");
            PageManager.getInstance().setTopCenterTitle(PageManager.getInstance().getRString(R.string.avg_day_5));
            PageManager.getInstance().setTopCenterContents(String.format("%.1f", avgDay) + PageManager.getInstance().getRString(R.string.day_txt));
            PageManager.getInstance().setTopRightTitle(PageManager.getInstance().getRString(R.string.avg_weight_5));
            PageManager.getInstance().setTopRightContents(String.format("%.1f", avgWeight) + "g");
        }
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }
}