package com.example.kokofarm_user_app.piece;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.kokofarm_user_app.R;
import com.example.kokofarm_user_app.manager.DataCacheManager;
import com.example.kokofarm_user_app.manager.PageManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Locale;

public class ScaleDongCardView extends LinearLayout {

    Context context;
    LayoutInflater inflater;
    String id = "";     // ex) KF000601

    public ScaleDongCardView(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context);
    }

    public ScaleDongCardView(Context context){
        super(context);
        init(context);
    }

    private void init(Context context){
        this.context = context;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.include_cardview_scale_dong,this,true);
    }

    public void setDong(String dong){
        TextView tv = this.findViewById(R.id.tv_dong);
        tv.setText(dong + PageManager.getInstance().getRString(R.string.dong_txt));
    }

    public void setDay(String day){
        TextView tv = this.findViewById(R.id.tv_day);
        tv.setText(day + PageManager.getInstance().getRString(R.string.day_txt));
    }

    public void setAvgWeight(String weight){
        TextView tv = this.findViewById(R.id.tv_avg_weight);
        tv.setText(weight + "g");
    }

    public void setTemp(String temp){
        TextView tv = this.findViewById(R.id.tv_temp);
        tv.setText(temp + " (°C)");
    }

    public void setHumi(String humi){
        TextView tv = this.findViewById(R.id.tv_humi);
        tv.setText(humi + " (%)");
    }

    public void setCO2(String co2){
        TextView tv = this.findViewById(R.id.tv_co2);
        tv.setText(co2 + " (ppm)");
    }

    public void setNH3(String nh3){
        TextView tv = this.findViewById(R.id.tv_nh3);
        tv.setText(nh3 + " (ppm)");
    }

    public void setImgError(int idx){
        ImageView img = this.findViewById(R.id.img_error);

        switch (idx){

            case 1:
                img.setImageResource(R.drawable.icon_oval_green);
                break;
            case 2:
                img.setImageResource(R.drawable.icon_oval_red);
                break;

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addScaleCard(JSONObject cellJson){
        LinearLayout cellZone = this.findViewById(R.id.cell_zone);

        ScaleCardView scv = new ScaleCardView(context);
        scv.initData(id, cellJson);
        cellZone.addView(scv);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void initData(String id){
        this.id = id;

        JSONObject buffer = DataCacheManager.getInstance().getCacheData("buffer");
        JSONObject cellJson = DataCacheManager.getInstance().getCacheData("cell");

        try {
            JSONObject dongJson = buffer.getJSONObject(id);

            setDong(dongJson.getString("beDongid"));
            setDay(dongJson.getString("beInterm"));

            setAvgWeight(String.format(Locale.getDefault(), "%.1f", dongJson.getDouble("beAvgWeight")));

            setTemp(String.format(Locale.getDefault(), "%.1f", dongJson.getDouble("beAvgTemp")));
            setHumi(String.format(Locale.getDefault(), "%.1f", dongJson.getDouble("beAvgHumi")));
            setCO2(String.format(Locale.getDefault(), "%.0f", dongJson.getDouble("beAvgCo2")));
            setNH3(String.format(Locale.getDefault(), "%.0f", dongJson.getDouble("beAvgNh3")));

            setImgError(1);
//            addErrorCard();

            // 카드뷰 안에 저울정보까지 넣기 - 일단 중단
//            JSONObject cellDongJson = cellJson.getJSONObject(id);
//            for (Iterator<String> it = cellDongJson.keys(); it.hasNext(); ) {
//
//                String cellID = it.next();
//                JSONObject cell = cellDongJson.getJSONObject(cellID);
//
////                Log.e("cell", cell.toString());
//
//                addScaleCard(cell);
//            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
