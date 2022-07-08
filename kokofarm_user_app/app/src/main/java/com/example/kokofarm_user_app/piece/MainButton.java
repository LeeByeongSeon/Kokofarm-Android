package com.example.kokofarm_user_app.piece;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.kokofarm_user_app.R;
import com.example.kokofarm_user_app.manager.PageManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class MainButton extends MaterialButton {
    public MainButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MainButton(Context context) {
        super(context);
    }

    public void setContents(String contents){

        String s = contents.equals("") ? "전체" : contents + "동";
        this.setText(s);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                PageManager.getInstance().eventSelectBar(contents);
            }
        });
    }
}
