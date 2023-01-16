package com.example.kokofarm_user_app;

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

import com.example.kokofarm_user_app.databinding.FragmentScaleChartBinding;
import com.example.kokofarm_user_app.kkf_utils.DateUtil;
import com.example.kokofarm_user_app.manager.DataCacheManager;
import com.example.kokofarm_user_app.manager.PageManager;
import com.example.kokofarm_user_app.piece.DongCardView;
import com.example.kokofarm_user_app.piece.ScaleCardView;
import com.github.mikephil.charting.charts.CombinedChart;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class ScaleChartFragment extends Fragment {

    private FragmentScaleChartBinding binding;
    String id;

    public ScaleChartFragment() {}

    public ScaleChartFragment(String id){ this.id = id; }

    public static ScaleChartFragment newInstance(String id) {
        return new ScaleChartFragment(id);
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
                             Bundle savedInstanceState) {
        binding = FragmentScaleChartBinding.inflate(inflater);

        setFragmentData(container.getContext());

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
        JSONObject cellJson = DataCacheManager.getInstance().getCacheData("cell");

//        Log.e("cellJson", cellJson.toString());
        LinearLayout scaleCardViewLay = binding.scaleLlView;

        try {
            JSONObject dongJson = cellJson.getJSONObject(DataCacheManager.getInstance().getSelectFarm() + id);

            for (Iterator<String> it = dongJson.keys(); it.hasNext(); ) {

                String id = it.next();
                JSONObject cell = dongJson.getJSONObject(id);

//                Log.e("cell", cell.toString());

                ScaleCardView scv = new ScaleCardView(context);
                scv.initData(id, cell);
                scaleCardViewLay.addView(scv);
            }

            dongJson = buffer.getJSONObject(DataCacheManager.getInstance().getSelectFarm() + id);
            String code = dongJson.getString("beComeinCode");

            // 평균중량 데이터
            JSONObject avgJson = DataCacheManager.getInstance().getCacheData(code, "avgWeight");
//            Log.e("avgJson", avgJson.toString());

            // 평균중량
            CombinedChart chart = binding.scaleChartAvgWeight.combineChart;
            CombinedChartMaker maker = new CombinedChartMaker(chart);
            maker.setDateStyle(60*60f, "MM-dd HH:mm");
            maker.setGraphStyle("line");
            maker.setZoom(10f);
            maker.setStartIdx(0);
            maker.makeTimeLineChart(avgJson,
                    new HashMap<String, String>() {{
                        put("awWeight", "평균중량");
                        put("refWeight", "권고중량");
                    }});
            maker.setMarker(context, R.layout.marker_text_view);
            chart.invalidate();

            // 정규분포 데이터
            String ndis = dongJson.getString("beNdis");
            JSONObject ndisJson = new JSONObject();
            int live = DataCacheManager.getInstance().getLiveCnt(dongJson);

            int all = 0;
            for(String s : ndis.split("\\|")) {
                all += Integer.parseInt(s);
            }

            int weight = 500;
            for(String s : ndis.split("\\|")) {
                int cnt = Integer.parseInt(s);

                if(cnt == 0){
                    weight += 50;
                    continue;
                }

                float per = (float) cnt / all;
                int t = Math.round(live * per);
                JSONObject jo = new JSONObject();
                jo.put("cnt", t);       // 그래프 데이터 출력을 위해 json 형식으로
                ndisJson.put(Integer.toString(weight), jo);
                weight += 50;
            }

            // 정규분포 차트
            chart = binding.scaleChartNdis.combineChart;
            maker = new CombinedChartMaker(chart);
            maker.setGraphStyle("combine");
            maker.setBarWidth(10f);
            maker.setZoom(0f);
            maker.setStartIdx(0);
            maker.makeTimeLineChart(ndisJson,
                    new HashMap<String, String>() {{
                        put("cnt", "마리수");
                    }});
            maker.setMarker(context, R.layout.marker_text_view);
            chart.invalidate();

            // 오늘 증체중량 데이터
            float prev = 0;
            JSONObject plusWeightJson = new JSONObject();
            long todayStamp = DateUtil.get_inst().get_timestamp(DateUtil.get_inst().get_plus_minus_minute_time(-60*25)); // 하루전 - 1시간
            for (Iterator<String> it = avgJson.keys(); it.hasNext(); ){

                String time = it.next();

                // 증체량 계산
                long stamp = DateUtil.get_inst().get_timestamp(time);
                if(stamp > todayStamp){
                    float diff = 0f;
                    float val = (float) avgJson.getJSONObject(time).getDouble("awWeight");

                    if(prev < 10f){     // 처음 값 없는 경우
                        diff = 0;
                    }
                    else{
                        diff = val - prev;
                    }

                    prev = val;
                    diff = diff > 0 ? diff : 0;

                    JSONObject jo = new JSONObject();
                    jo.put("diff", diff);
                    plusWeightJson.put(time, jo);
                }
            }

            // 오늘 증체중량
            chart = binding.scaleChartPlusWeight.combineChart;
            maker = new CombinedChartMaker(chart);
            maker.setDateStyle(60*60f, "MM-dd HH:mm");
            maker.setGraphStyle("bar");
            maker.setZoom(0f);
            maker.setStartIdx(1);
            maker.makeTimeLineChart(plusWeightJson,
                    new HashMap<String, String>() {{
                        put("diff", "증체중량");
                    }});
            maker.setMarker(context, R.layout.marker_text_view);
            chart.invalidate();

            // 일령별 센서
            JSONObject sensorHistory = DataCacheManager.getInstance().getCacheData(code, "sensorHistory");
            JSONObject sensorJson = new JSONObject();
            for (Iterator<String> it = sensorHistory.keys(); it.hasNext(); ){
                String time = it.next();

                JSONObject timeJSON = sensorHistory.getJSONObject(time);

                if(timeJSON.getString("shSensorData").length() < 5){
                    continue;
                }

                // cell_temp : 온도, cell_humi : 습도, cell_co2 : 이산화탄소, cell_nh3 : 암모니아
                JSONObject shSensorData = new JSONObject(timeJSON.getString("shSensorData"));
                sensorJson.put(time, shSensorData);
            }

            // 일령별 센서
            chart = binding.scaleChartSensor.combineChart;
            maker = new CombinedChartMaker(chart);
            maker.setDateStyle(60*60f, "MM-dd HH:mm");
            maker.setGraphStyle("line");
            maker.setZoom(0f);
            maker.setStartIdx(0);
            maker.makeTimeLineChart(sensorJson,
                    new HashMap<String, String>() {{
                        put("cell_temp", "온도");
                        put("cell_humi", "습도");
//                        put("cell_co2", "이산화탄소");
//                        put("cell_nh3", "암모니아");
                    }});
            maker.setMarker(context, R.layout.marker_text_view);
            chart.invalidate();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
