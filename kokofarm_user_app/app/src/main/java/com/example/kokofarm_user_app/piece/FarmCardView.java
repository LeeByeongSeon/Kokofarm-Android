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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class FarmCardView extends LinearLayout {

    Context context;
    LayoutInflater inflater;
    String id = "";     // ex) KF0006

    public FarmCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FarmCardView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context){
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.include_cardview_farm,this,true);
    }

    public void setFarmName(String farmName){
        TextView tv = this.findViewById(R.id.tv_farm_name);
        tv.setText(farmName);
    }

    public void setDongCount(String dongCount){
        TextView tv = this.findViewById(R.id.tv_dong_count);
        tv.setText(dongCount + "개 동");
    }

    public void setLive(String live){
        TextView tv = this.findViewById(R.id.tv_live);
        tv.setText(live + "수");
    }

    public void setAvgWeight(String avg){
        TextView tv = this.findViewById(R.id.tv_avg_weight);
        tv.setText(avg + "g");
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

//    public void addErrorCard(){
//
//        this.findViewById(R.id.img_dot_line).setVisibility(VISIBLE);
//
//        //inflater.inflate(R.layout.badge_card_view, this.findViewById(R.id.badge_error_zone), true);
//        BadgeCardView cdv = new BadgeCardView(context);
//        cdv.setContents("ssibal");
//
//        errorBadgeList.add(cdv);
//
//        LinearLayout lay = this.findViewById(R.id.badge_error_zone);
//        lay.addView(cdv);
//    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void initData(String id, JSONObject info, JSONObject buffer){
        this.id = id;

        LinearLayout dongListZone = this.findViewById(R.id.dong_list_zone);

        try {

            setDongCount(info.getString("count"));
            setLive(info.getString("live"));
            setAvgWeight(info.getString("beAvgWeight"));

            setImgError(1);
//            addErrorCard();

            for (Iterator<String> it = buffer.keys(); it.hasNext(); ){
                String key = it.next();     // ex) KF000601

                JSONObject dongJson = buffer.getJSONObject(key);

                setFarmName(dongJson.getString("fName"));

                FarmCardViewDetail fcvd = new FarmCardViewDetail(context);
                fcvd.initData(key, dongJson);
                dongListZone.addView(fcvd);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
