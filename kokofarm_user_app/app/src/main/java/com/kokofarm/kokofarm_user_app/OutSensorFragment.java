package com.kokofarm.kokofarm_user_app;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.kokofarm.kokofarm_user_app.databinding.FragmentLoginBinding;
import com.kokofarm.kokofarm_user_app.databinding.FragmentOutSensorBinding;
import com.kokofarm.kokofarm_user_app.manager.DataCacheManager;
import com.kokofarm.kokofarm_user_app.manager.PageManager;
import com.github.mikephil.charting.charts.CombinedChart;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

public class OutSensorFragment extends Fragment {

    private FragmentOutSensorBinding binding;

    public OutSensorFragment() {

    }

    public static OutSensorFragment newInstance() {

        return new OutSensorFragment();
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
        binding = FragmentOutSensorBinding.inflate(inflater);

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

        String farm = DataCacheManager.getInstance().getSelectFarm();

        // 선택된 농장이 없을 경우
        if(farm.equals("")){
            PageManager.getInstance().showManagerTopContents(PageManager.getInstance().getRString(R.string.ext_sensor_txt));
            return;
        }
        else{
            PageManager.getInstance().showFarmTopContents("");
        }

        try {
            JSONObject outSensor = DataCacheManager.getInstance().getCacheData("outSensor").getJSONObject(farm);

            double soTemp = outSensor.optDouble("soTemp", -1d);
            double soHumi = outSensor.optDouble("soHumi", -1d);
            double soNh3 = outSensor.optDouble("soNh3", -1d);
            double soH2s = outSensor.optDouble("soH2s", -1d);
            double soDust = outSensor.optDouble("soDust", -1d);
            double soUDust = outSensor.optDouble("soUDust", -1d);
            double soWindDirection = outSensor.optDouble("soWindDirection", -1d);
            double soWindSpeed = outSensor.optDouble("soWindSpeed", -1d);
            double soSolar = outSensor.optDouble("soSolar", -1d);

            // 0보다 작은 경우 - 로 처리함
            binding.cdvOutSensor.tvOutTemp.setText(soTemp < 0f ? "-" : String.format(Locale.getDefault(), "%.1f", soTemp));
            binding.cdvOutSensor.tvOutHumi.setText(soHumi < 0f ? "-" : String.format(Locale.getDefault(), "%.1f", soHumi));
            binding.cdvOutSensor.tvOutNh3.setText(soNh3 < 0f ? "-" : String.format(Locale.getDefault(), "%.1f", soNh3));
            binding.cdvOutSensor.tvOutH2s.setText(soH2s < 0f ? "-" : String.format(Locale.getDefault(), "%.1f", soH2s));
            binding.cdvOutSensor.tvOutDust.setText(soDust < 0f ? "-" : String.format(Locale.getDefault(), "%.1f", soDust));
            binding.cdvOutSensor.tvOutUdust.setText(soUDust < 0f ? "-" : String.format(Locale.getDefault(), "%.1f", soUDust));
            binding.cdvOutSensor.tvWindDirec.setText(DataCacheManager.getInstance().getWindDirection((int)soWindDirection));
            binding.cdvOutSensor.tvWindSpeed.setText(soWindSpeed < 0f ? "-" : String.format(Locale.getDefault(), "%.1f", soWindSpeed));
            binding.cdvOutSensor.tvSolar.setText(soSolar < 0f ? "-" : String.format(Locale.getDefault(), "%.1f", soSolar));

            String key = outSensor.getString("soFarmid") + outSensor.getString("soDongid");

            JSONObject buffer = DataCacheManager.getInstance().getCacheData("buffer");

            String code = buffer.getJSONObject(key).getString("beComeinCode");

            // 일령별 센서
            JSONObject sensorHistory = DataCacheManager.getInstance().getCacheData(code, "sensorHistory");
            JSONObject sensorJson = new JSONObject();
            for (Iterator<String> it = sensorHistory.keys(); it.hasNext(); ){
                String time = it.next();

                JSONObject timeJSON = sensorHistory.getJSONObject(time);

                if(timeJSON.getString("shSensorData").length() < 5){
                    continue;
                }

                // ext_temp : 온도, ext_humi : 습도, ext_nh3 : 암모니아, ext_h2s : 황화수소
                // ext_dust : 미세먼지, ext_udust : 초미세먼지, ext_wdirec : 풍향, ext_wspeed : 풍속, ext_solar : 일사량
                JSONObject shExtSensorData = new JSONObject(timeJSON.getString("shExtSensorData"));
                sensorJson.put(time, shExtSensorData);
            }

            // 일령별 센서
            CombinedChart chart = binding.envChartDaily;
            CombinedChartMaker maker = new CombinedChartMaker(chart);
            maker.setDateStyle(60*60f, "MM-dd HH:mm");
            maker.setGraphStyle("line");
            maker.setZoom(0f);
            maker.setStartIdx(0);
            maker.makeTimeLineChart(sensorJson,
                    new HashMap<String, String>() {{
                        put("ext_temp", PageManager.getInstance().getRString(R.string.temp_txt));
                        put("ext_humi", PageManager.getInstance().getRString(R.string.humi_txt));
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