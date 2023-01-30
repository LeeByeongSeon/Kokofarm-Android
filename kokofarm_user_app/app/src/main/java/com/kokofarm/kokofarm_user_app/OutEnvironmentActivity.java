package com.kokofarm.kokofarm_user_app;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.kokofarm.kokofarm_user_app.databinding.ActivityOutEnvironmentBinding;
import com.kokofarm.kokofarm_user_app.kkf_utils.DateUtil;
import com.kokofarm.kokofarm_user_app.manager.DataCacheManager;
import com.github.mikephil.charting.charts.CombinedChart;
import com.kokofarm.kokofarm_user_app.manager.PageManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

public class OutEnvironmentActivity extends AppCompatActivity {

    private ActivityOutEnvironmentBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivityOutEnvironmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Toolbar set
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.nav_env);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setData();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setData(){

        String tfarm = "KF0027";
        JSONObject outJson = DataCacheManager.getInstance().getCacheData("outSensor");

        try {

            if(outJson.isNull(tfarm)){
                return;
            }

            JSONObject json = outJson.getJSONObject(tfarm);

            String temp = String.format(Locale.getDefault(), "%.1f", json.getDouble("soTemp"));
            String humi = String.format(Locale.getDefault(), "%.1f", json.getDouble("soHumi"));
            String nh3 = String.format(Locale.getDefault(), "%.1f", json.getDouble("soNh3"));
            String h2s = String.format(Locale.getDefault(), "%.1f", json.getDouble("soH2s"));

            String dust = DataCacheManager.getInstance().getDustStatus(json.getInt("soDust"));
            String udust = DataCacheManager.getInstance().getDustStatus(json.getInt("soUDust"));

            String windDirec = DataCacheManager.getInstance().getWindDirection(json.getInt("soWindDirection"));
            String windSpeed = String.format(Locale.getDefault(), "%.1f", json.getDouble("soWindSpeed"));

            String solar = String.format(Locale.getDefault(), "%.0f", json.getDouble("soSolar"));

            binding.cdvOutSensor.tvOutTemp.setText(temp);
            binding.cdvOutSensor.tvOutHumi.setText(humi);
            binding.cdvOutSensor.tvOutNh3.setText(nh3);
            binding.cdvOutSensor.tvOutH2s.setText(h2s);
            binding.cdvOutSensor.tvOutDust.setText(dust);
            binding.cdvOutSensor.tvOutUdust.setText(udust);
            binding.cdvOutSensor.tvWindDirec.setText(windDirec);
            binding.cdvOutSensor.tvWindSpeed.setText(windSpeed);
            binding.cdvOutSensor.tvSolar.setText(solar);

            String farmID = json.getString("soFarmid");
            String dongID = json.getString("soDongid");

            JSONObject buffer = DataCacheManager.getInstance().getCacheData("buffer").getJSONObject(farmID + dongID);
            String code = buffer.getString("beComeinCode");

            JSONObject sensorHistory = DataCacheManager.getInstance().getCacheData("sensorHistory");
//            Log.e("sensorHistory", sensorHistory.toString());

            JSONObject extSensorJson = new JSONObject();
            JSONObject dayExtSensorJson = new JSONObject();

            long now_stamp = DateUtil.get_inst().get_now_timestamp();

            for (Iterator<String> it = sensorHistory.keys(); it.hasNext(); ){
                String time = it.next();
                JSONObject timeJson = sensorHistory.getJSONObject(time);
                String extSensorStr = timeJson.getString("shExtSensorData");
                JSONObject j = new JSONObject(extSensorStr);

                extSensorJson.put(time, j);

                long stamp = DateUtil.get_inst().get_timestamp(time);

                if(now_stamp - stamp <= 60 * 60 * 24){
                    dayExtSensorJson.put(time, j);
                }

            }

            // 전체 일자 
            CombinedChart chart = binding.envCcDo;
            CombinedChartMaker maker = new CombinedChartMaker(chart);
            maker.setDateStyle(60*60f, "MM-dd HH:mm");
            maker.setYAxisRange(0, 100);
            maker.makeTimeLineChart(extSensorJson,
                    new HashMap<String, String>() {{
                        put("ext_temp", PageManager.getInstance().getRString(R.string.temp_txt));
                        put("ext_humi", PageManager.getInstance().getRString(R.string.humi_txt));
                    }});
            maker.setMarker(this, R.layout.marker_text_view);
            chart.invalidate();

            // 최근 24시간
            chart = binding.envCcTo;
            maker = new CombinedChartMaker(chart);
            maker.setYAxisRange(0, 100);
            maker.setDateStyle(60*60f, "MM-dd HH:mm");
            maker.makeTimeLineChart(dayExtSensorJson,
                    new HashMap<String, String>() {{
                        put("ext_temp", PageManager.getInstance().getRString(R.string.temp_txt));
                        put("ext_humi", PageManager.getInstance().getRString(R.string.humi_txt));
                    }});
            maker.setMarker(this, R.layout.marker_text_view);
            chart.invalidate();


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
