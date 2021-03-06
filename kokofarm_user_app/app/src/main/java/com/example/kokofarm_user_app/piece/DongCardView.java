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
import com.example.kokofarm_user_app.manager.DataCacheManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DongCardView extends LinearLayout {

    Context context;
    LayoutInflater inflater;
    String id = "";

    List<BadgeCardView> errorBadgeList = new ArrayList<>();

    public DongCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DongCardView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context){
        this.context = context;
        inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.include_cardview_dong,this,true);
    }

    public void setDong(String dong){
        TextView tv = this.findViewById(R.id.tv_dong);
        tv.setText(dong + "동");
    }

    public void setDay(String day){
        TextView tv = this.findViewById(R.id.tv_day);
        tv.setText(day + "일령");
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

    public void setImgHen(int idx){
        ImageView img = this.findViewById(R.id.img_hen);
        //img.setImageResource();
    }

    public void setImgFeed(int idx){
        ImageView img = this.findViewById(R.id.img_feed);
        //img.setImageResource();
    }

    public void addErrorCard(){

        this.findViewById(R.id.img_dot_line).setVisibility(VISIBLE);

        //inflater.inflate(R.layout.badge_card_view, this.findViewById(R.id.badge_error_zone), true);
        BadgeCardView cdv = new BadgeCardView(context);
        cdv.setContents("ssibal");

        errorBadgeList.add(cdv);

        LinearLayout lay = this.findViewById(R.id.badge_error_zone);
        lay.addView(cdv);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void initData(String id){
        this.id = id;

        JSONObject buffer = DataCacheManager.getInstance().getCacheData("buffer");

        try {
            JSONObject dongJson = buffer.getJSONObject(id);

            //Log.e("dongJson", dongJson.toString());

            setDong(dongJson.getString("beDongid"));
            setDay(dongJson.getString("interm"));

            int live = dongJson.getInt("cmInsu") + dongJson.getInt("cmExtraSu")
                    - dongJson.getInt("cmDeathCount") - dongJson.getInt("cmCullCount")
                    - dongJson.getInt("cmThinoutCount");

            setLive("" + live);
            setAvgWeight(String.format(Locale.getDefault(), "%.1f", dongJson.getDouble("beAvgWeight")));

            setImgError(1);
//            addErrorCard();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
