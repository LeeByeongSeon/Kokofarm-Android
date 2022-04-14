package com.example.kkf_user_2_0.ui.breed_info;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.kkf_user_2_0.R;
import com.example.kkf_user_2_0.databinding.FragmentBreedInfoBinding;

import java.util.ArrayList;
import java.util.List;

public class BreedInfoFragment extends Fragment {
    private FragmentBreedInfoBinding binding;

    private AutoCompleteTextView autoCompleteTextView;
    private TextView textView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentBreedInfoBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        autoCompleteTextView = binding.selectItemInterm;

        List<String> items = new ArrayList<>();
        items.add("2022-03-08 00:00:00");
        items.add("2022-03-08 00:00:00");
        items.add("2022-03-08 00:00:00");

        ArrayAdapter<String> itemAdapter = new ArrayAdapter<>(container.getContext(), R.layout.select_box_item, items);
        autoCompleteTextView.setAdapter(itemAdapter);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                textView.setText((String)adapterView.getItemAtPosition(i));
            }
        });

        return view;
    }
}
