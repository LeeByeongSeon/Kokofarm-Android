package com.example.kokofarm_user_app.piece;

import android.app.ActionBar;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;

import androidx.drawerlayout.widget.DrawerLayout;

import com.example.kokofarm_user_app.R;
import com.example.kokofarm_user_app.manager.PageManager;
import com.google.android.material.button.MaterialButton;

public class MainButton extends MaterialButton {
    public MainButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MainButton(Context context) {
        super(context);
    }

    public void setContents(String contents){

        int size = 15;

        DrawerLayout.LayoutParams params = new DrawerLayout.LayoutParams(DrawerLayout.LayoutParams.WRAP_CONTENT, DrawerLayout.LayoutParams.WRAP_CONTENT);
        // 버튼 사이 공백
        params.rightMargin = size;

        String s = contents.equals("") ? "전체" : contents + "동";
        this.setText(s);

        // 버튼 크기 조정하기위해 setMinimumWidth 로 0을 맞추준다음 setWidth 로 원하는 사이즈 기입
        this.setMinimumWidth(0);
        this.setWidth(200);
        this.setLayoutParams(params);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                PageManager.getInstance().eventSelectBar(contents);
            }
        });
    }
}
