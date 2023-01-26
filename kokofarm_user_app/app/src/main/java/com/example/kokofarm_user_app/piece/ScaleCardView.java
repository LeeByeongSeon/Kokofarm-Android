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

import java.util.Locale;

public class ScaleCardView extends LinearLayout {

    Context context;
    LayoutInflater inflater;
    String id = "";
    String cellID = "";

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

    public void setCellNum(String cellNum){
        TextView tv = this.findViewById(R.id.tv_scale_num);
        tv.setText(cellNum + PageManager.getInstance().getRString(R.string.scale_num));
    }

    public void setWeight(String weight){
        TextView tv = this.findViewById(R.id.tv_weight);
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

//    public void setImgError(int idx){
//        ImageView img = this.findViewById(R.id.img_error);
//
//        switch (idx){
//
//            case 1:
//                img.setImageResource(R.drawable.icon_oval_green);
//                break;
//            case 2:
//                img.setImageResource(R.drawable.icon_oval_red);
//                break;
//
//        }
//    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void initData(String id, JSONObject cell){
        this.id = id;

        try {
//            Log.e("cell", cell.toString());

            this.cellID = cell.getString("siCellid");
            setCellNum(cellID);

            setWeight(String.format(Locale.getDefault(), "%.0f", cell.getDouble("siWeight")));

            setTemp(String.format(Locale.getDefault(), "%.1f", cell.getDouble("siTemp")));
            setHumi(String.format(Locale.getDefault(), "%.1f", cell.getDouble("siHumi")));
            setCO2(String.format(Locale.getDefault(), "%.0f", cell.getDouble("siCo2")));
            setNH3(String.format(Locale.getDefault(), "%.0f", cell.getDouble("siNh3")));

//            setImgError(1);
//            addErrorCard();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
