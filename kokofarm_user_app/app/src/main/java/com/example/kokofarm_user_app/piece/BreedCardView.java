package com.example.kokofarm_user_app.piece;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kokofarm_user_app.R;

import org.json.JSONObject;

public class BreedCardView extends LinearLayout {

    Context context;
    LayoutInflater inflater;
    String id = "";

    public BreedCardView(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context);
    }

    public BreedCardView(Context context){
        super(context);
        init(context);
    }

    private void init(Context context){
        this.context = context;
        inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.include_cardview_breed,this,true);
    }

    public void setDate(String date){
        TextView tv = this.findViewById(R.id.tv_date);
        tv.setText(date);
    }

    public void setDay(String day){
        TextView tv = this.findViewById(R.id.tv_day);
        tv.setText(day);
    }

    public void setLive(String live){
        TextView tv = this.findViewById(R.id.tv_live);
        tv.setText(live + "수");
    }

    public void setDeath(String death){
        TextView tv = this.findViewById(R.id.tv_death);
        tv.setText(death);
    }

    public void setCull(String cull){
        TextView tv = this.findViewById(R.id.tv_cull);
        tv.setText(cull);
    }

    public void setThinout(String thinout){
        TextView tv = this.findViewById(R.id.tv_thinout);
        tv.setText(thinout);
    }

    public void initData(String date, int live, int day, JSONObject jo){
        setDate(date);

        setLive(Integer.toString(live));
        setDeath(jo.optString("cdDeath"));
        setCull(jo.optString("cdCull"));
        setThinout(jo.optString("cdThinout"));

        setDay(Integer.toString(day));

    }
}
