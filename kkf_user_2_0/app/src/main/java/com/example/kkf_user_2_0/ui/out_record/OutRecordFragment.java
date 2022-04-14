package com.example.kkf_user_2_0.ui.out_record;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.kkf_user_2_0.databinding.FragmentOutRecordBinding;

import java.util.ArrayList;

import ir.androidexception.datatable.DataTable;
import ir.androidexception.datatable.model.DataTableHeader;
import ir.androidexception.datatable.model.DataTableRow;

public class OutRecordFragment extends Fragment {

    FragmentOutRecordBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentOutRecordBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        DataTable dataTable = binding.outTable;

        // Table Header 생성
        DataTableHeader tableHeader = new DataTableHeader.Builder()
                .item("번호",5)
                .item("동 이름",10)
                .item("입추일자",10)
                .item("출하일자",10)
                .build();

        // Table Row 생성과 안에 들어갈 Data 생성
        ArrayList<DataTableRow> tableRows = new ArrayList<>();
        for(int i=1; i<11; i++){
            DataTableRow row = new DataTableRow.Builder()
                    .value(String.valueOf(i))
                    .value("청운농장")
                    .value("2022-01-01")
                    .value("2022-02-01")
                    .build();
            tableRows.add(row);
        }

        // Table settings
        dataTable.setShadow(5);
        dataTable.setHeaderBackgroundColor(Color.TRANSPARENT);
        dataTable.setDividerColor(Color.LTGRAY);
        dataTable.setDividerThickness(1);

        // 각(Header, Row) data 주입
        dataTable.setHeader(tableHeader);
        dataTable.setRows(tableRows);
        dataTable.inflate(view.getContext());

        return view;
    }
}
