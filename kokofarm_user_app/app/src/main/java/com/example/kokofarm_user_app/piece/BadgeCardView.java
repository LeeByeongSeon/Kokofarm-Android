package com.example.kokofarm_user_app.piece;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kokofarm_user_app.R;
import com.google.android.material.card.MaterialCardView;

public class BadgeCardView extends MaterialCardView {
    public BadgeCardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public BadgeCardView(Context context) {
        super(context);

        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.badge_card_view,this,true);
    }

    public void setContents(String contents){
        TextView tv = this.findViewById(R.id.tv_contents);
        tv.setText(contents);

    }
}
