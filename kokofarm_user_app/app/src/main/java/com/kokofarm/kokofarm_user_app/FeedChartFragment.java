package com.kokofarm.kokofarm_user_app;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.kokofarm.kokofarm_user_app.databinding.FragmentFeedChartBinding;
import com.kokofarm.kokofarm_user_app.kkf_utils.DateUtil;
import com.kokofarm.kokofarm_user_app.manager.DataCacheManager;
import com.kokofarm.kokofarm_user_app.manager.PageManager;
import com.github.mikephil.charting.charts.CombinedChart;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class FeedChartFragment extends Fragment {

    private FragmentFeedChartBinding binding;
    String id;

    public FeedChartFragment() {

    }

    public FeedChartFragment(String id){ this.id = id; }

    public static FeedChartFragment newInstance(String id) {

        return new FeedChartFragment(id);
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
        binding = FragmentFeedChartBinding.inflate(inflater);

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
        PageManager.getInstance().showFarmTopContents(id);

        JSONObject buffer = DataCacheManager.getInstance().getCacheData("buffer");

        try {
            JSONObject dongJson = buffer.getJSONObject(DataCacheManager.getInstance().getSelectFarm() + id);

            JSONObject sensorHistory = DataCacheManager.getInstance().getCacheData(dongJson.getString("beComeinCode"), "sensorHistory");
            JSONObject dailyChartJson = new JSONObject();
            JSONObject todayChartJson = new JSONObject();

            long todayStamp = DateUtil.get_inst().get_timestamp(DateUtil.get_inst().get_plus_minus_minute_time(-60*25)); // 하루전 - 1시간

            for (Iterator<String> it = sensorHistory.keys(); it.hasNext(); ) {

                String id = it.next();
                JSONObject timeJSON = sensorHistory.getJSONObject(id);

                if(timeJSON.getString("shFeedData").length() < 5){
                    continue;
                }

                // feed_feed : 급이량, feed_water : 급수량
                JSONObject shFeedData = new JSONObject(timeJSON.getString("shFeedData"));

                // 하루치 24개 데이터를 일자로 묶음
                String date = id.substring(0, 10) + " 00:00:00";
                JSONObject dailyData = dailyChartJson.optJSONObject(date) == null ? new JSONObject() : dailyChartJson.optJSONObject(date);
                int f = dailyData.optInt("feed_feed") + shFeedData.optInt("feed_feed");
                int w = dailyData.optInt("feed_feed") + shFeedData.optInt("feed_feed");
                dailyData.put("feed_feed", f);
                dailyData.put("feed_water", w);
                dailyChartJson.put(date, dailyData);

                // 음수 노이즈 제거 (소장님 요청)
                if(shFeedData.getInt("feed_feed") < 0) {
                    shFeedData.put("feed_feed", 0);
                }
                // 어제부터 현재시간까지의 데이터만 담음 - 오늘 변화량 출력 위함
                long stamp = DateUtil.get_inst().get_timestamp(id);
                if(stamp > todayStamp){
                    todayChartJson.put(id, shFeedData);
                }
            }

//            Log.e("chartJson", dailyChartJson.toString());

            // 오늘 급이량
            CombinedChart chart = binding.feedChartToday.combineChart;
            CombinedChartMaker maker = new CombinedChartMaker(chart);
            maker.setDateStyle(60*60f, "MM-dd HH:mm");
            maker.setGraphStyle("bar");
            maker.setBarWidth(0.9f);
            maker.setZoom(0f);
            maker.setStartIdx(0);
            maker.makeTimeLineChart(todayChartJson,
                    new HashMap<String, String>() {{
                        put("feed_feed", PageManager.getInstance().getRString(R.string.feed));
//                        put("feed_feed", "급이량");
                    }});
            maker.setMarker(context, R.layout.marker_text_view);
            chart.invalidate();

            // 일령별 급이량
            chart = binding.feedChartDaily.combineChart;
            maker = new CombinedChartMaker(chart);
            maker.setDateStyle(24*60*60f, "MM-dd");
            maker.setGraphStyle("bar");
            maker.setBarWidth(0.9f);
            maker.setZoom(0f);
            maker.setStartIdx(1);
            maker.makeTimeLineChart(dailyChartJson,
                    new HashMap<String, String>() {{
                        put("feed_feed", PageManager.getInstance().getRString(R.string.feed));
//                        put("feed_feed", "급이량");
                    }});
            maker.setMarker(context, R.layout.marker_text_view);
            chart.invalidate();

            // 오늘 급수량
            chart = binding.waterCharToday.combineChart;
            maker = new CombinedChartMaker(chart);
            maker.setDateStyle(60*60f, "MM-dd HH:mm");
            maker.setGraphStyle("bar");
            maker.setBarWidth(0.9f);
            maker.setZoom(0f);
            maker.setStartIdx(0);
            maker.makeTimeLineChart(todayChartJson,
                    new HashMap<String, String>() {{
                        put("feed_water", PageManager.getInstance().getRString(R.string.water));
//                        put("feed_water", "급수량");
                    }});
            maker.setMarker(context, R.layout.marker_text_view);
            chart.invalidate();

            // 일령별 급수량
            chart = binding.waterChartDaily.combineChart;
            maker = new CombinedChartMaker(chart);
            maker.setDateStyle(24*60*60f, "MM-dd");
            maker.setGraphStyle("bar");
            maker.setBarWidth(0.9f);
            maker.setZoom(0f);
            maker.setStartIdx(1);
            maker.makeTimeLineChart(dailyChartJson,
                    new HashMap<String, String>() {{
                        put("feed_water", PageManager.getInstance().getRString(R.string.water));
//                        put("feed_water", "급수량");
                    }});
            maker.setMarker(context, R.layout.marker_text_view);
            chart.invalidate();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}