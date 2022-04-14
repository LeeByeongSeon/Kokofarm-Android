package com.example.kkf_user_2_0.ui.iot_scale;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.text.AllCapsTransformationMethod;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.kkf_user_2_0.R;
import com.example.kkf_user_2_0.databinding.FragmentIotscaleBinding;

import java.util.ArrayList;
import java.util.Map;

import ir.androidexception.datatable.DataTable;
import ir.androidexception.datatable.enums.Direction;
import ir.androidexception.datatable.model.DataTableHeader;
import ir.androidexception.datatable.model.DataTableRow;

public class IoTScaleFragment extends Fragment {
    private FragmentIotscaleBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        IoTScaleViewModel ioTScaleViewModel =
                new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(IoTScaleViewModel.class);

        binding = FragmentIotscaleBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        DataTable dataTable = binding.iotTable;

        // Table Header 생성
        DataTableHeader tableHeader = new DataTableHeader.Builder()
                .item("저울",10)
                .item("중량",10)
                .item("온도",10)
                .item("습도",10)
                .item("Co2",10)
                .item("Nh3",10)
                .build();

        // Table Row 생성과 안에 들어갈 Data 생성
        ArrayList<DataTableRow> tableRows = new ArrayList<>();
        DataTableRow row = new DataTableRow.Builder()
                .value("IoT저울-01")
                .value("1411.0")
                .value("22.5")
                .value("44.3")
                .value("1379.0")
                .value("0.0")
                .build();
        tableRows.add(row);
        DataTableRow row2 = new DataTableRow.Builder()
                .value("IoT저울-02")
                .value("1411.0")
                .value("22.5")
                .value("44.3")
                .value("1379.0")
                .value("0.0")
                .build();
        tableRows.add(row2);
        DataTableRow row3 = new DataTableRow.Builder()
                .value("IoT저울-03")
                .value("1411.0")
                .value("22.5")
                .value("44.3")
                .value("1379.0")
                .value("0.0")
                .build();
        tableRows.add(row3);

        // Table settings
        dataTable.setShadow(5);
        dataTable.setHeaderBackgroundColor(Color.TRANSPARENT);
        dataTable.setDividerColor(Color.LTGRAY);
        dataTable.setDividerThickness(1);
        dataTable.setCornerRadius(5);

        // 각(Header, Row) data 주입
        dataTable.setHeader(tableHeader);
        dataTable.setRows(tableRows);
        dataTable.inflate(view.getContext());

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}