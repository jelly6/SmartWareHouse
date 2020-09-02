package com.example.smartwarehouse;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartwarehouse.model.Item;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private static final String TAG = ItemAdapter.class.getSimpleName();
    List<Item> items;
    public ItemAdapter(List<Item> items) {
        this.items=items;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_raw,parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(position);
            holder.item_model.setText(item.getItem_model());
            holder.item_position.setText(item.getPosition());
            holder.item_check.setChecked(item.isChecked());
            Log.d(TAG, "onBindViewHolder: "+item.getItem_model());
        }


    }

    @Override
    public int getItemCount() {
        if(items!=null){
            return items.size();

        }else{
            return 0;
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView item_model;
        TextView item_position;
        CheckBox item_check;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            item_model=itemView.findViewById(R.id.item_model);
            item_position=itemView.findViewById(R.id.item_position);
            item_check=itemView.findViewById(R.id.item_checkBox);
        }
    }
}
