package com.example.kokofarm_user_app.piece;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kokofarm_user_app.R;

public class ScaleCardView extends LinearLayout {

    Context context;
    LayoutInflater inflater;
    String id = "";

    public ScaleCardView(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context);
    }

    public ScaleCardView(Context context){
        super(context);
        init(context);
    }

    private void init(Context context){
        this.context = context;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.include_cardview_scale,this,true);
    }

    public void setScaleNum(String scaleNum){
        TextView tv = this.findViewById(R.id.tv_scale_num);
        tv.setText(scaleNum);
    }

    public void setWeight(String weight){
        TextView tv = this.findViewById(R.id.tv_avg_weight);
        tv.setText(weight + "g");
    }

    public void setTemp(String temp){
        TextView tv = this.findViewById(R.id.tv_temp);
        tv.setText(temp + "°C");
    }

    public void setHumi(String humi){
        TextView tv = this.findViewById(R.id.tv_humi);
        tv.setText(humi + "%");
    }

    public void setCO2(String co2){
        TextView tv = this.findViewById(R.id.tv_co2);
        tv.setText(co2 + "ppm");
    }

    public void setH2S(String h2s){
        TextView tv = this.findViewById(R.id.tv_h2s);
        tv.setText(h2s + "ppm");
    }
}
