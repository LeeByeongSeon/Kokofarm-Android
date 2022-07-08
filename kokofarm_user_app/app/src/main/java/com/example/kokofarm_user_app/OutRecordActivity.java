package com.example.kokofarm_user_app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kokofarm_user_app.databinding.ActivityOutRecordBinding;

public class OutRecordActivity extends AppCompatActivity {

    private ActivityOutRecordBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOutRecordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Toolbar set
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.out_txt);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}