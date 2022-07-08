package com.example.kokofarm_user_app;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.kokofarm_user_app.databinding.ActivityLoginBinding;
import com.example.kokofarm_user_app.databinding.ActivityMainBinding;
import com.example.kokofarm_user_app.manager.DataCacheManager;
import com.example.kokofarm_user_app.manager.PageManager;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String id = binding.etId.getText().toString();
                String pw = binding.etPw.getText().toString();

                int result = DataCacheManager.getInstance().login(id, pw);

                if(result == 0){
                    PageManager.getInstance().moveActivity(getApplicationContext(), MainActivity.class);
                }
                else{
                    Log.e("result", result + "");
                }
            }
        });
    }

}
