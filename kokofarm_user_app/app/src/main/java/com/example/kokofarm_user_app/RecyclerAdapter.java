package com.example.kokofarm_user_app;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.SparseBooleanArray;
import android.view.ContentInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kokofarm_user_app.databinding.FragmentBreedListBinding;
import com.example.kokofarm_user_app.databinding.FragmentBreedListItemBinding;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

// 뷰 어댑터
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_breed_list_item, parent, false);
//        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        view.setLayoutParams(lp);
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

    // 뷰홀더
    class ItemViewHolder extends RecyclerView.ViewHolder{

        private String data;
        private int position;
        public MaterialCardView mCardView;

        TextView textView;

        ItemViewHolder(View itemView){
            super(itemView);
            // 테스트용...
            textView = itemView.findViewById(R.id.tv_date);

            mCardView = itemView.findViewById(R.id.breed_list);

            mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog dialog = new Dialog(view.getContext());
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    WindowManager.LayoutParams winLayout = new WindowManager.LayoutParams();
                    winLayout.copyFrom(dialog.getWindow().getAttributes());
                    winLayout.width = WindowManager.LayoutParams.MATCH_PARENT;
                    winLayout.height = WindowManager.LayoutParams.WRAP_CONTENT;

                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_layout);
                    dialog.show();

    //               dialog.findViewById(R.id.dialog_cancel);

                }
            });
        }

        void onBind(String data, int position){
            this.data = data;
            this.position = position;

            textView.setText(data);
        }
    }
}
