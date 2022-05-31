package com.example.kokofarm_user_app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.kokofarm_user_app.databinding.ActivityOutEnvironmentBinding;

public class OutEnvironmentActivity extends AppCompatActivity {

    private ActivityOutEnvironmentBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivityOutEnvironmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Toolbar set
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("외기환경");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
