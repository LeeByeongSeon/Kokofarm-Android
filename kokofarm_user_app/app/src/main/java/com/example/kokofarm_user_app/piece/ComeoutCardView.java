package com.example.kokofarm_user_app.piece;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kokofarm_user_app.R;

import org.json.JSONObject;

import java.util.Locale;

public class ComeoutCardView extends LinearLayout {

    Context context;
    LayoutInflater inflater;

    public ComeoutCardView(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context);
    }

    public ComeoutCardView(Context context){
        super(context);
        init(context);
    }

    private void init(Context context){
        this.context = context;
        inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.include_cardview_comeout,this,true);
    }

    public void setName(String name){
        TextView tv = this.findViewById(R.id.tv_name);
        tv.setText(name);
    }

    public void setDay(String day){
        TextView tv = this.findViewById(R.id.tv_day);
        tv.setText(day + "일령");
    }

    public void setIndate(String indate){
        indate = indate.substring(0, 10);
        TextView tv = this.findViewById(R.id.tv_in_date);
        tv.setText(indate);
    }

    public void setOutdate(String outdate){
        outdate = outdate.substring(0, 10);
        TextView tv = this.findViewById(R.id.tv_out_date);
        tv.setText(outdate);
    }

    public void setAvgWeight(String avgWeight){
        TextView tv = this.findViewById(R.id.tv_avg_weight);
        tv.setText(avgWeight + "g");
    }

    public void initData(JSONObject jo){

        setName(jo.optString("fdName"));
        setDay(jo.optString("interm"));
        setIndate(jo.optString("cmIndate"));
        setOutdate(jo.optString("cmOutdate"));
        setAvgWeight(String.format(Locale.getDefault(), "%.1f", jo.optDouble("awWeight", 0f)));

    }
}
