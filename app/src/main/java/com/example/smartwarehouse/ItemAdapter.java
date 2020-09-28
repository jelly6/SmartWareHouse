package com.example.smartwarehouse;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartwarehouse.model.Item;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private static final String TAG = ItemAdapter.class.getSimpleName();
    private List<Item> items;

    public List<Item> getItems() {
        return items;
    }

    public ItemAdapter(List<Item> items) {
        this.items=items;
    }
    public ItemAdapter(){}

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_raw,parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.item_model.setBackgroundResource(R.drawable.textview_circle_black);
        holder.item_position.setBackgroundResource(R.drawable.textview_circle_black);
        holder.item_check.setBackgroundResource(R.drawable.textview_circle_black);
        if(position==0){
            holder.textView.setVisibility(View.VISIBLE);
            holder.item_check.setVisibility(View.INVISIBLE);
            holder.item_model.setBackgroundResource(R.drawable.textview_circle_deepblue);
            holder.item_position.setBackgroundResource(R.drawable.textview_circle_deepblue);
            holder.textView.setBackgroundResource(R.drawable.textview_circle_deepblue);
            holder.textView.setTextColor(Color.WHITE);
            holder.item_model.setTextColor(Color.WHITE);
            holder.item_position.setTextColor(Color.WHITE);

        }else{
            holder.textView.setVisibility(View.INVISIBLE);
            holder.item_check.setVisibility(View.VISIBLE);
            holder.item_model.setBackgroundResource(R.drawable.textview_circle_light);

            Item item = items.get(position-1);
            holder.item_model.setText(item.getItem_model());
            holder.item_position.setText(item.getPosition());
            if(item.isChecked()){
                holder.item_check.setImageResource(R.drawable.check_mark);
            }else{
                holder.item_check.setImageResource(android.R.drawable.menuitem_background);
            }
            if(item.getItem_model().contains("LED")){
                holder.item_model.setBackgroundResource(R.drawable.textview_circle_yellow);
            }
            Log.d(TAG, "onBindViewHolder: "+item.getItem_model());
        }



    }

    @Override
    public int getItemCount() {
        if(items!=null){
            return items.size()+1;

        }else{
            return 0;
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView item_model;
        TextView item_position;
        ImageView item_check;
        TextView textView;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            item_model=itemView.findViewById(R.id.item_model);
            item_position=itemView.findViewById(R.id.item_position);
            item_check=itemView.findViewById(R.id.item_imageView);
            textView = itemView.findViewById(R.id.textView);
        }
    }
}
