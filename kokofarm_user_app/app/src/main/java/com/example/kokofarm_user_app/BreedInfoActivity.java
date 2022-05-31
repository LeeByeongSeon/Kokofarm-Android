package com.example.kokofarm_user_app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.kokofarm_user_app.databinding.ActivityBreedInfoBinding;

public class BreedInfoActivity extends AppCompatActivity {

    private ActivityBreedInfoBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivityBreedInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Toolbar set
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("사육정보 변경");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
