package com.kokofarm.kokofarm_user_app;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.kokofarm.kokofarm_user_app.databinding.FragmentHomeBinding;
import com.kokofarm.kokofarm_user_app.databinding.FragmentManagerBinding;
import com.kokofarm.kokofarm_user_app.manager.DataCacheManager;
import com.kokofarm.kokofarm_user_app.manager.PageManager;
import com.kokofarm.kokofarm_user_app.piece.FarmCardView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class ManagerFragment extends Fragment {

    private FragmentManagerBinding binding;

    public ManagerFragment() {

    }

    public static ManagerFragment newInstance() {

        return new ManagerFragment();
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
        binding = FragmentManagerBinding.inflate(inflater);

//        binding.cdvHomeBreed.layCdv.setOnClickListener(view -> {
//            toggleBreedInfo();
//        });

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
        
        // 관리자 화면 진입 시 선택 농장, 동 초기화
        DataCacheManager.getInstance().setSelectFarm("");
        DataCacheManager.getInstance().setSelectDong("");

        // 전체 농장 버퍼데이터 불러오기
        DataCacheManager.getInstance().getAllBuffer();
        JSONObject farmList = DataCacheManager.getInstance().getFarmList();
        HashMap<String, HashMap<String, JSONObject>> cacheDataMap = DataCacheManager.getInstance().getCacheDataMap();

        int in = 0;
        int out = 0;
        int dongInCount = 0;

        try {

            LinearLayout farmListZone = binding.farmListZone;

            for (Iterator<String> it = farmList.keys(); it.hasNext(); ){
                String id = it.next();     // ex) KF0006

                JSONObject info = farmList.getJSONObject(id);
                int comein = info.getInt("comein");

                // 입추 출하 분기 - 추후에 출하 농장 추가 시 0인거 아닌거 분기하면 됨
                if(comein > 0){
                    in++;
                    dongInCount += comein;
                    JSONObject rowJson = cacheDataMap.get(id).get("buffer");

                    FarmCardView fcv = new FarmCardView(context);
                    fcv.initData(id, info, rowJson);
                    farmListZone.addView(fcv);

                    fcv.setOnClickListener(view -> {
                        DataCacheManager.getInstance().setSelectFarm(id);
                        PageManager.getInstance().movePage("home");
                    });
                }
                else{
                    out++;
                }
            }

            int farmCnt = in + out;

            PageManager.getInstance().setTopContentsCollapsing(PageManager.getInstance().getRString(R.string.farm_status));
            PageManager.getInstance().setTopLeftTitle(PageManager.getInstance().getRString(R.string.total_farm));
            PageManager.getInstance().setTopLeftContents(farmCnt + "");
            PageManager.getInstance().setTopCenterTitle(PageManager.getInstance().getRString(R.string.enter_farm));
            PageManager.getInstance().setTopCenterContents(in + "");
            PageManager.getInstance().setTopRightTitle(PageManager.getInstance().getRString(R.string.enter_dong));
            PageManager.getInstance().setTopRightContents(dongInCount + "");

            PageManager.getInstance().hideToolbar(false);
            PageManager.getInstance().hideTopInfo(false);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void toggleBreedInfo(){
//        if(binding.cdvHomeBreed.layExtra.getVisibility() == View.GONE){
//            binding.cdvHomeBreed.layExtra.setVisibility(View.VISIBLE);
//            binding.cdvHomeBreed.layDeath.setVisibility(View.VISIBLE);
//            binding.cdvHomeBreed.layCull.setVisibility(View.VISIBLE);
//            binding.cdvHomeBreed.layThinout.setVisibility(View.VISIBLE);
//            binding.cdvHomeBreed.imgAccordion.setImageResource(R.drawable.ic_baseline_arrow_up_24);
//        }
//        else{
//            binding.cdvHomeBreed.layExtra.setVisibility(View.GONE);
//            binding.cdvHomeBreed.layDeath.setVisibility(View.GONE);
//            binding.cdvHomeBreed.layCull.setVisibility(View.GONE);
//            binding.cdvHomeBreed.layThinout.setVisibility(View.GONE);
//            binding.cdvHomeBreed.imgAccordion.setImageResource(R.drawable.ic_baseline_arrow_down_24);
//        }
    }
}