package com.example.kokofarm_user_app;

import android.content.Context;
import android.content.Intent;
import android.util.SparseBooleanArray;
import android.view.ContentInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {

    private ArrayList<String> listData = new ArrayList<>();
    private Context context;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();    // Item 의 클릭 상태를 저장할 array 객체
    private int prePosition = -1;                                           // 직전에 클릭됐던 Item 의 position

    public RecyclerAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_out_record_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position){
        holder.onBind(listData.get(position), position);
    }

    @Override
    public int getItemCount(){
        return listData.size();
    }

    void addItem(String data){
        listData.add(data);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{

        private String data;
        private int position;
        public TextView textViewTitle;

        ItemViewHolder(View itemView){
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.out_tv_number);  // 리사이클러뷰 item 의 제목

            textViewTitle.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    Intent intent = new Intent(view.getContext(), OutRecordChartActivity.class);
                    context.startActivity(intent);
                }
            });
        }

        void onBind(String data, int position){
            this.data = data;
            this.position = position;

            textViewTitle.setText(data);
        }
    }
}
