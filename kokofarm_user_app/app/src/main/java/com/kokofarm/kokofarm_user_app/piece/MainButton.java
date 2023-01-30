package com.kokofarm.kokofarm_user_app.piece;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.RequiresApi;
import androidx.drawerlayout.widget.DrawerLayout;

import com.kokofarm.kokofarm_user_app.R;
import com.kokofarm.kokofarm_user_app.manager.DataCacheManager;
import com.kokofarm.kokofarm_user_app.manager.PageManager;
import com.google.android.material.button.MaterialButton;

public class MainButton extends MaterialButton {
    public MainButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MainButton(Context context) {
        super(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setContents(String contents){

        int size = 20;

        DrawerLayout.LayoutParams params = new DrawerLayout.LayoutParams(DrawerLayout.LayoutParams.WRAP_CONTENT, DrawerLayout.LayoutParams.WRAP_CONTENT);
        // 버튼 사이 공백
        params.rightMargin = size;

        String s = contents.equals("") ? PageManager.getInstance().getRString(R.string.all_txt) : contents + PageManager.getInstance().getRString(R.string.dong_txt_empty);
        this.setText(s);
        this.setTextSize(15.0f);

        // 버튼 크기 조정하기위해 setMinimumWidth 로 0을 맞추준다음 setWidth 로 원하는 사이즈 기입
        this.setMinimumWidth(0);
        this.setWidth(250);
        this.setLayoutParams(params);

        this.setOnClickListener(view -> {
            DataCacheManager.getInstance().setSelectDong(contents);
            PageManager.getInstance().eventSelectBar(contents);
        });
    }
}
