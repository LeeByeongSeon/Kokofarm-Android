package com.example.kkf_user_2_0.ui.dong_list;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.kkf_user_2_0.CombinedChartMaker;
import com.example.kkf_user_2_0.R;
import com.example.kkf_user_2_0.UtilFunction;
import com.example.kkf_user_2_0.databinding.FragmentDongBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;

import java.util.HashMap;

public class DongFragment extends Fragment {
    private FragmentDongBinding binding;
    private CombinedChart combinedChart;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DongViewModel dongViewModel =
                new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(DongViewModel.class);

        binding = FragmentDongBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

//        final TextView textView = binding.textDashboard;
//        dongViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        /*
        * CombinedChart : 혼합 차트
        * https://github.com/PhilJay/MPAndroidChart
        * */
        combinedChart = binding.combineChart;

        //test
        String jsonData = UtilFunction.get_api_data(new HashMap<String, String>() {{
            put("userType", "user");
            put("userID", "kk0071");
            put("setComm", "avgWeight");
            put("code", "20220312082856_KF007101");
        }});

        CombinedChartMaker maker = new CombinedChartMaker(combinedChart);
        maker.setDateStyle(60*60f, "MM-dd HH:mm");
        maker.makeChart(jsonData,
                new HashMap<String, String>() {{
                    put("awWeight", "평균중량");
                    put("refWeight", "권고중량");
                }});
        maker.setMarker(container.getContext(), R.layout.marker_text_view);
        combinedChart.invalidate();

        return view;
    }

    // 객체 자체는 사라지지 않고 메모리에 남음
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        Log.d("Destroy ::::::::", "DESTROY VIEW");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d("Destroy ::::::::", "DESTROY");
    }
}