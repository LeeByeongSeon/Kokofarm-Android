package com.kokofarm.kokofarm_user_app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.kokofarm.kokofarm_user_app.databinding.ActivityOutRecordChartBinding;

public class OutRecordChartActivity extends AppCompatActivity {

    private ActivityOutRecordChartBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivityOutRecordChartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Toolbar set
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.release_history);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}