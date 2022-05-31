package com.example.kokofarm_user_app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kokofarm_user_app.databinding.FragmentOutRecordBinding;

import java.util.ArrayList;
import java.util.Random;

import ir.androidexception.datatable.DataTable;
import ir.androidexception.datatable.model.DataTableHeader;
import ir.androidexception.datatable.model.DataTableRow;

public class OutRecordFragment extends Fragment {

    private FragmentOutRecordBinding binding;

    // Fragment manager
    private FragmentManager fragmentManager;

    public OutRecordFragment() {

    }

    public static OutRecordFragment newInstance() {

        return new OutRecordFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOutRecordBinding.inflate(inflater);

        // https://androidexample365.com/material-data-table-android-library/

        DataTable dataTable = binding.outRecordTable;
        DataTableHeader header = new DataTableHeader.Builder()
                .item("번호", 1)
                .item("동명", 1)
                .item("입추일자", 1)
                .item("출하일자", 1)
                .build();

        ArrayList<DataTableRow> rows = new ArrayList<>();
        for(int i=0;i<10;i++){
            Random random = new Random();
            int r = random.nextInt(i+1);
            int rDiscount = random.nextInt(20);
            DataTableRow row = new DataTableRow.Builder()
                    .value(i+"번")
                    .value(String.valueOf(r))
                    .value(String.valueOf(r*1000).concat("$"))
                    .value(String.valueOf(rDiscount).concat("%"))
                    .build();
            rows.add(row);
        }

        dataTable.setHeader(header);
        dataTable.setRows(rows);
        dataTable.inflate(inflater.getContext());

        return binding.getRoot();
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }
}