package com.example.kkf_user_2_0.ui.dong_list;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.kkf_user_2_0.MainActivity;
import com.example.kkf_user_2_0.R;
import com.example.kkf_user_2_0.databinding.ActivityMainBinding;
import com.example.kkf_user_2_0.databinding.FragmentDongBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class DongFragment extends Fragment {
    private FragmentDongBinding binding;
    private BarChart barChart;
    private CombinedChart combinedChart;

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

        combinedChart.getDescription().setEnabled(false);
        combinedChart.setBackgroundColor(Color.WHITE);
        combinedChart.setDrawGridBackground(false);
        combinedChart.setDrawBarShadow(false);
        combinedChart.setHighlightFullBarEnabled(false);

        // draw bars behind lines
        combinedChart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.BUBBLE, CombinedChart.DrawOrder.CANDLE, CombinedChart.DrawOrder.LINE, CombinedChart.DrawOrder.SCATTER
        });

        Legend l = combinedChart.getLegend();
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        YAxis rightAxis = combinedChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis leftAxis = combinedChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        XAxis xAxis = combinedChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);

        CombinedData data = new CombinedData();

        data.setData(initLineData());
        data.setData(initBarData());

        xAxis.setAxisMaximum(data.getXMax() + 0.25f);

        combinedChart.setData(data);
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

    public LineData initLineData() {
        // line data 생성
        LineData d = new LineData();

        // line chart 데이터를 담는 list
        ArrayList<Entry> entries = new ArrayList<>();

        // 임시 데이터
        ArrayList<Integer> valList = new ArrayList<>();
        valList.add(10);
        valList.add(20);
        valList.add(30);
        valList.add(40);
        valList.add(50);
        valList.add(60);
        valList.add(70);
        valList.add(80);
        valList.add(90);

        for(int i=0; i< valList.size(); i++){
            entries.add(new Entry((Integer)valList.get(i), i));
        }

        // line data setting 값
        LineDataSet set = new LineDataSet(entries, "Line DataSet");
        set.setColor(Color.rgb(240, 238, 70));
        set.setLineWidth(2.5f);
        set.setDrawFilled(true);    // 영역 채우기
        set.setFillColor(Color.rgb(240, 238, 70));
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(false);   // value 표시
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.rgb(240, 238, 70));

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        return d;
    }

    public BarData initBarData() {

        // bar chart 데이터를 담는 list
        ArrayList<BarEntry> entries1 = new ArrayList<>();

        // 임시 data
        ArrayList<Integer> valList1 = new ArrayList<>();
        valList1.add(10);
        valList1.add(20);
        valList1.add(30);
        valList1.add(40);
        valList1.add(50);
        valList1.add(60);
        valList1.add(70);
        valList1.add(80);
        valList1.add(90);

        for(int i=0; i< valList1.size(); i++){
            entries1.add(new BarEntry((Integer)valList1.get(i), i));
        }

        // bar chart setting 값
        BarDataSet set1 = new BarDataSet(entries1, "Bar DataSet");
        set1.setColor(Color.rgb(60, 220, 78));
        set1.setValueTextColor(Color.rgb(60, 220, 78));
        set1.setValueTextSize(10f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);

        float barWidth = 5f; // x2 dataset
        BarData d = new BarData(set1);
        d.setBarWidth(barWidth);

        return d;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d("Destroy ::::::::", "DESTROY");
    }
}