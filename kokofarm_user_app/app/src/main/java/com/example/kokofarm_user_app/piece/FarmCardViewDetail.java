package com.example.kokofarm_user_app.piece;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.kokofarm_user_app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class FarmCardViewDetail extends LinearLayout {

    Context context;
    LayoutInflater inflater;
    String id = "";

    public FarmCardViewDetail(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FarmCardViewDetail(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context){
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.include_cardview_farm_detail,this,true);
    }

    public void setDong(String dong){
        TextView tv = this.findViewById(R.id.tv_dong);
        tv.setText(dong);
    }

    public void setDay(String day){
        TextView tv = this.findViewById(R.id.tv_day);
        tv.setText(day);
    }

    public void setNetwork(String network){
        TextView tv = this.findViewById(R.id.tv_network);
        tv.setText(network + "ms");
    }

//    public void setLive(String live){
//        TextView tv = this.findViewById(R.id.tv_live);
//        tv.setText(live + "수");
//    }

    public void setAvgWeight(String avgWeight){
        TextView tv = this.findViewById(R.id.tv_avg_weight);
        tv.setText(avgWeight + "g");
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
    public void initData(String id, JSONObject dongJson){
        this.id = id;   // KF000601

        try {
            setDong(dongJson.getString("fdDongid"));

            String beStatus = dongJson.getString("beStatus");
            if(beStatus.equals("O")){
                setDay("출하");
            }
            else{
                setDay(dongJson.getString("beInterm"));
            }

//            int live = dongJson.getInt("cmInsu") + dongJson.getInt("cmExtraSu")
//                    - dongJson.getInt("cmDeathCount") - dongJson.getInt("cmCullCount")
//                    - dongJson.getInt("cmThinoutCount");
//
//            setLive("" + live);

            setAvgWeight(String.format(Locale.getDefault(), "%.1f", dongJson.getDouble("beAvgWeight")));
            setNetwork(dongJson.getString("beNetwork"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
