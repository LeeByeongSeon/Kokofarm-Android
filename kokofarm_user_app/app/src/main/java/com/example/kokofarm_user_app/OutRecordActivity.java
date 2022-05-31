package com.example.kokofarm_user_app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kokofarm_user_app.databinding.ActivityOutRecordBinding;

public class OutRecordActivity extends AppCompatActivity {

    private ActivityOutRecordBinding binding;
    private RecyclerView recyclerView;

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

        recyclerView = binding.scaleListView;

        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(getApplicationContext());
        for(int i=1; i<=20; i++){
            String str;
            if(i<10){
                str = "0"+i;
            } else {
                str = String.valueOf(i);
            }
            recyclerAdapter.addItem(str);
        }
        recyclerView.setAdapter(recyclerAdapter);

//        Spinner spinnerMonth = binding.outMonth;
//        ArrayList<String> monthArr = new ArrayList<>();
//        for(int i=1; i<13; i++){
//            monthArr.add(i+"월");
//        }
//
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, monthArr);
//
//        spinnerMonth.setAdapter(arrayAdapter);
    }
}
