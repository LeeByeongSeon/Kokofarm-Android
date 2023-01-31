package com.kokofarm.kokofarm_user_app;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.kokofarm.kokofarm_user_app.kkf_utils.DateUtil;
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
import com.kokofarm.kokofarm_user_app.manager.PageManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class CombinedChartMaker {

    public static final int[] graphColors = new int[]{
            Color.parseColor("#F79F81"),
            Color.parseColor("#F3E2A9"),
            Color.parseColor("#2ECCFA"),
            Color.parseColor("#FF00FF")
    };

    public static final int[] backColors = new int[]{
            Color.parseColor("#F5BCA9"),
            Color.parseColor("#F3F781"),
            Color.parseColor("#848484"),
            Color.parseColor("#2E2E2E")
    };

    private CombinedChart chart;
    private long timeBase;
    private float timeTerm;
    private String timeFormat;

    private boolean isSetY = false;
    private float yMax = 0;
    private float yMin = 0;

    private boolean isDateFormat = false;

    private String graphStyle = "line";
    private float zoom = 5f;
    private float barWidth = 0.5f;
    private int startIdx = 0;       // 그래프 첫 데이터의 색상을 뭘로 표기할지

    public CombinedChartMaker(CombinedChart chart){
        this.chart = chart;
    }

    public void setDateStyle(float timeTerm, String timeFormat){
        isDateFormat = true;
        this.timeTerm = timeTerm;
        this.timeFormat = timeFormat;
    }

    public void setGraphStyle(String graphStyle){
        this.graphStyle = graphStyle;
    }

    public void setZoom(float zoom){
        this.zoom = zoom;
    }

    public void setBarWidth(float barWidth){
        this.barWidth = barWidth;
    }

    public void setStartIdx(int startIdx){
        this.startIdx = startIdx;
    }

    public void setYAxisRange(float yMin, float yMax){
        isSetY = true;
        this.yMin = yMin;
        this.yMax = yMax;
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

    // x 축을 날짜 형식으로 변환
    class DongAxisValueFormat extends IndexAxisValueFormatter {

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public String getFormattedValue(float val){

            return ((int)val + 1) + "동";
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getDateFromValue(float val){
        long timestamp = (long)(val * timeTerm) + timeBase;

        String ret = DateUtil.get_inst().timestamp_to_date(timestamp, timeFormat);

//        Log.e("val", val + "");
        if(ret.length() < 6 || ret.substring(6).equals("00:00")){
            return ret.substring(0, 2) + PageManager.getInstance().getRString(R.string.month)+" " + ret.substring(3, 5) + PageManager.getInstance().getRString(R.string.day);
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
        chart.setBackgroundColor(Color.TRANSPARENT);
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
    public void makeTimeLineChart(JSONObject jsonData, HashMap<String, String> fieldMap){

        try {
            // 초기화
            initLegend(chart);
            initCombinedChart(chart);

            if (jsonData != null) {
                // line 과 bar 기본 생성
                ArrayList<ArrayList<Entry>> lines = new ArrayList<>();
                ArrayList<ArrayList<BarEntry>> bars = new ArrayList<>();

                // 초기화
                for (int i = 0; i < fieldMap.size(); i++) {
                    lines.add(new ArrayList<>());
                    bars.add(new ArrayList<>());
                }

                try {
//                    Log.e("jsonData", jsonData.toString());

                    // 데이터를 float으로 받아서 날짜로 변경해줘야 함
                    // 첫 시간 데이터를 0으로 받음
                    // 시간 주기가 1시간인 경우 1이 증가할때 마다 1시간으로 변환
                    String first = "";

                    //JSONObject retData = jsonData.getJSONObject("retData");
                    Iterator<String> iter = jsonData.keys();
                    while (iter.hasNext()) {
                        String key = iter.next();
                        JSONObject data = jsonData.getJSONObject(key);

                        if(isDateFormat){
                            if(first.equals("")){
                                first = key;
                                timeBase = DateUtil.get_inst().get_timestamp(first);
                            }

                            //if(!key.substring(11).equals("23:00:00")){
                            if (!key.substring(14).equals("00:00")) {
                                continue;
                            }

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
                        else{
                            int idx = 0;
                            for (String field : fieldMap.keySet()) {
                                Double val = data.getDouble(field);
                                lines.get(idx).add(new Entry(Float.parseFloat(key), val.floatValue()));
                                bars.get(idx).add(new BarEntry(Float.parseFloat(key), val.floatValue()));
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
                    ld.addDataSet(makeLineDataSet(lines.get(idx), fieldMap.get(field), startIdx + idx));
                    bd.addDataSet(makeBarDataSet(bars.get(idx), fieldMap.get(field), startIdx + idx));
                    idx++;
                }

                bd.setBarWidth(barWidth);

                CombinedData data = new CombinedData();
                switch (graphStyle){
                    case "bar":
                        data.setData(bd);
                        break;
                    case "combine":
                        data.setData(bd);
                        data.setData(ld);
                        break;
                    default:
                        data.setData(ld);
                        break;
                }

                chart.setData(data);
                //Log.e("CombinedData", "" + data.getBarData().getDataSets());

                yMin = isSetY ? yMin : data.getYMin() - 1f;
                yMax = isSetY ? yMax : data.getYMax() + 30f;

                YAxis rightAxis = chart.getAxisRight();
                rightAxis.setDrawGridLines(false);
                rightAxis.setAxisMinimum(yMin);
                rightAxis.setAxisMaximum(yMax);

                YAxis leftAxis = chart.getAxisLeft();
                leftAxis.setDrawGridLines(false);
                leftAxis.setAxisMinimum(yMin);
                leftAxis.setAxisMaximum(yMax);

                XAxis xAxis = chart.getXAxis();
                if(isDateFormat) xAxis.setValueFormatter(new DateTimeAxisValueFormat());
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setLabelCount(4, false);
                xAxis.setSpaceMax(1f);
                xAxis.setSpaceMin(1f);
                xAxis.setGranularity(1f);
                xAxis.setGranularityEnabled(true);

                xAxis.setAxisMinimum(data.getXMin() - 1f);
                xAxis.setAxisMaximum(data.getXMax() + 1f);

                chart.zoom(zoom, 0f, 0f, 0);
                chart.moveViewToX(data.getXMax());
                //chart.zoomToCenter(5f, 1f);
                //chart.zoom(5f, 1f, 100f, 1f, YAxis.AxisDependency.LEFT);

                chart.setScaleYEnabled(false);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void makeSimpleChart(String label, List<Float> data, int colorIdx){
        initLegend(chart);
        initCombinedChart(chart);

        ArrayList<Entry> lines = new ArrayList<>();
        ArrayList<BarEntry> bars = new ArrayList<>();

        for(int i=0; i<data.size(); i++){
            lines.add(new Entry(i, data.get(i)));
            bars.add(new BarEntry(i, data.get(i)));
        }

        LineData ld = new LineData();
        BarData bd = new BarData();

        ld.addDataSet(makeLineDataSet(lines, label, colorIdx));
        bd.addDataSet(makeBarDataSet(bars, label, colorIdx));

        bd.setBarWidth(0.5f);

        CombinedData combinedData = new CombinedData();
        //data.setData(bd);
        combinedData.setData(bd);
        chart.setData(combinedData);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(combinedData.getYMin() / 2);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(combinedData.getYMin() / 2);

        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new DongAxisValueFormat());
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setLabelCount(4, false);
        xAxis.setSpaceMax(1f);
        xAxis.setSpaceMin(1f);
        xAxis.setGranularity(1f);
        //xAxis.setGranularityEnabled(true);

    }
}
