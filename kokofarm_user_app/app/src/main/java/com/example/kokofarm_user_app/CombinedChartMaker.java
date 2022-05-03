package com.example.kokofarm_user_app;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class CombinedChartMaker {

    public static final int[] graphColors = new int[]{
            Color.parseColor("#F79F81"),
            Color.parseColor("#F3E2A9"),
            Color.parseColor("#FF00FF")
    };

    public static final int[] backColors = new int[]{
            Color.parseColor("#F5BCA9"),
            Color.parseColor("#F3F781"),
            Color.parseColor("#2E2E2E")
    };

    private CombinedChart chart;
    private long timeBase;
    private float timeTerm;
    private String timeFormat;

    public boolean isDateFormat = false;

    public CombinedChartMaker(CombinedChart chart){
        this.chart = chart;
    }

    public void setDateStyle(float timeTerm, String timeFormat){
        isDateFormat = true;
        this.timeTerm = timeTerm;
        this.timeFormat = timeFormat;
    }

    public void setTimeBase(long timeBase){
        this.timeBase = timeBase;
    }

    // x 축을 날짜 형식으로 변환
    class DateTimeAxisValueFormat extends IndexAxisValueFormatter {

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public String getFormattedValue(float val){

            return getDateFromValue(val);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getDateFromValue(float val){
        long timestamp = (long)(val * timeTerm) + timeBase;

        String ret = DateUtil.get_inst().timestamp_to_date(timestamp, timeFormat);

        if(ret.substring(11).equals("00:00:00")){
            return ret.substring(5, 7) + "월 " + ret.substring(8, 10) + "일";
        }

        return ret;
    }

    public void setMarker(Context context, int layoutResource){
        chart.setMarker(new ImplMarkerView(context, layoutResource));
    }

    // 마커뷰 생성
    class ImplMarkerView extends MarkerView {

        private TextView tvContent;

        public ImplMarkerView(Context context, int layoutResource) {
            super(context, layoutResource);
            tvContent = (TextView)findViewById(R.id.makerContent);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void refreshContent(Entry e, Highlight highlight) {

            if (e instanceof CandleEntry) { //선택한 점의 엔트리 값이 받아와짐 (x축, y축)
                CandleEntry ce = (CandleEntry) e;
                tvContent.setText("" + Utils.formatNumber(ce.getHigh(), 0, true));
            }
            else {
                if(isDateFormat){
                    String x = getDateFromValue(e.getX());
                    String y = Utils.formatNumber(e.getY(), 0, true);

                    tvContent.setText(x + "\n[" + y + "]");
                }
                else{
                    tvContent.setText("" + Utils.formatNumber(e.getY(), 0, true));
                }
            }

            super.refreshContent(e, highlight);
        }

        @Override
        public MPPointF getOffset() {
            return new MPPointF(-(getWidth() / 2), -getHeight());
        }

    }

    // legend 기본 설정
    private void initLegend(Chart chart){
        Legend l = chart.getLegend();
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
    }

    private void initCombinedChart(CombinedChart chart){

        chart.getDescription().setEnabled(false);
        chart.setBackgroundColor(Color.WHITE);
        chart.setDrawGridBackground(false);
        chart.setDrawBarShadow(false);
        chart.setHighlightFullBarEnabled(false);

        chart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.BUBBLE, CombinedChart.DrawOrder.CANDLE, CombinedChart.DrawOrder.LINE, CombinedChart.DrawOrder.SCATTER
        });

    }

    public LineDataSet makeLineDataSet(ArrayList<Entry> values, String label, int setIndex){

        LineDataSet ret = new LineDataSet(values, label);
        ret.setColor(graphColors[setIndex]);
        ret.setLineWidth(2.5f);
        ret.setDrawFilled(true);    // 영역 채우기
        ret.setFillColor(backColors[setIndex]);
        ret.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        ret.setDrawValues(false);   // value 표시
        ret.setValueTextSize(10f);
        ret.setValueTextColor(backColors[setIndex]);
        ret.setDrawCircleHole(false);
        ret.setDrawCircles(false);

        ret.setAxisDependency(YAxis.AxisDependency.LEFT);

        return ret;
    }

    public BarDataSet makeBarDataSet(ArrayList<BarEntry> values, String label, int setIndex){

        BarDataSet ret = new BarDataSet(values, label);

        ret.setColor(graphColors[setIndex]);
        ret.setValueTextColor(backColors[setIndex]);
        ret.setValueTextSize(1f);
        ret.setAxisDependency(YAxis.AxisDependency.LEFT);

        return ret;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void makeChart(String jsonData, HashMap<String, String> fieldMap){

        try {

            initLegend(chart);
            initCombinedChart(chart);

            if (!jsonData.equals("")) {

                ArrayList<ArrayList<Entry>> lines = new ArrayList<>();
                ArrayList<ArrayList<BarEntry>> bars = new ArrayList<>();

                for (int i = 0; i < fieldMap.size(); i++) {
                    lines.add(new ArrayList<>());
                    bars.add(new ArrayList<>());
                }

                try {

                    JSONObject jo = new JSONObject(jsonData);

                    if (jo != null && jo.getString("errCode").equals("00")) {

                        String first = jo.getString("first");
                        timeBase = DateUtil.get_inst().get_timestamp(first);

                        JSONObject retData = jo.getJSONObject("retData");
                        Iterator<String> iter = retData.keys();
                        while (iter.hasNext()) {
                            String key = iter.next();

                            //if(!key.substring(11).equals("23:00:00")){
                            if (!key.substring(14).equals("00:00")) {
                                continue;
                            }

                            JSONObject data = retData.getJSONObject(key);

                            long key_stamp = DateUtil.get_inst().get_timestamp(key);

                            float diff = (key_stamp - timeBase) / timeTerm ;

                            int idx = 0;
                            for (String field : fieldMap.keySet()) {
                                Double val = data.getDouble(field);
                                lines.get(idx).add(new Entry(diff, val.floatValue()));
                                bars.get(idx).add(new BarEntry(diff, val.floatValue()));
                                idx++;
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                LineData ld = new LineData();
                BarData bd = new BarData();

                int idx = 0;
                for (String field : fieldMap.keySet()) {
                    ld.addDataSet(makeLineDataSet(lines.get(idx), fieldMap.get(field), idx));
                    bd.addDataSet(makeBarDataSet(bars.get(idx), fieldMap.get(field), idx));
                    idx++;
                }

                bd.setBarWidth(0.5f);

                CombinedData data = new CombinedData();
                //data.setData(bd);
                data.setData(ld);
                chart.setData(data);

                //Log.e("CombinedData", "" + data.getBarData().getDataSets());

                YAxis rightAxis = chart.getAxisRight();
                rightAxis.setDrawGridLines(false);
                rightAxis.setAxisMinimum(data.getYMin()); // this replaces setStartAtZero(true)

                YAxis leftAxis = chart.getAxisLeft();
                leftAxis.setDrawGridLines(false);
                leftAxis.setAxisMinimum(data.getYMin()); // this replaces setStartAtZero(true)

                XAxis xAxis = chart.getXAxis();
                xAxis.setValueFormatter(new DateTimeAxisValueFormat());
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setLabelCount(4, false);
                xAxis.setSpaceMax(1f);
                xAxis.setSpaceMin(1f);
                xAxis.setGranularity(1f);
                xAxis.setGranularityEnabled(true);

                xAxis.setAxisMinimum(data.getXMin() - 1f);
                xAxis.setAxisMaximum(data.getXMax() + 1f);

                chart.zoom(5f, 0f, 0f, 0);
                chart.moveViewToX(data.getXMax());
                //chart.zoomToCenter(5f, 1f);
                //chart.zoom(5f, 1f, 100f, 1f, YAxis.AxisDependency.LEFT);

                chart.setScaleYEnabled(false);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
