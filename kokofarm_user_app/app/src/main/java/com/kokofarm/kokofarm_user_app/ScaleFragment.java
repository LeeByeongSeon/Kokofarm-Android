package com.kokofarm.kokofarm_user_app;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.kokofarm.kokofarm_user_app.databinding.FragmentScaleBinding;
import com.kokofarm.kokofarm_user_app.manager.DataCacheManager;
import com.kokofarm.kokofarm_user_app.manager.PageManager;
import com.kokofarm.kokofarm_user_app.piece.ScaleDongCardView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class ScaleFragment extends Fragment {

    private FragmentScaleBinding binding;
    private RecyclerView recyclerView;

    public ScaleFragment() {

    }

    public static ScaleFragment newInstance() {

        return new ScaleFragment();
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

        binding = FragmentScaleBinding.inflate(inflater);

        setFragmentData(container.getContext());

        return binding.getRoot();
    }

    // fragment 메모리 누수 방지
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
        PageManager.getInstance().showFarmTopContents("");

        JSONObject buffer = DataCacheManager.getInstance().getCacheData("buffer");

        LinearLayout scaleDongCardViewLay = binding.scaleLlView;

        try {
            for (Iterator<String> it = buffer.keys(); it.hasNext(); ) {

                String id = it.next();
                JSONObject dongJson = buffer.getJSONObject(id);

                ScaleDongCardView sdcv = new ScaleDongCardView(context);
                sdcv.initData(id);
                scaleDongCardViewLay.addView(sdcv);

                sdcv.setOnClickListener(view -> {
                    PageManager.getInstance().eventSelectBar(id.substring(6));
                });

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}