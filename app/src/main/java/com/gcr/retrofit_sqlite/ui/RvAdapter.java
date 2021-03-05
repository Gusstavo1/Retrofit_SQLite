package com.gcr.retrofit_sqlite.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gcr.retrofit_sqlite.R;
import com.gcr.retrofit_sqlite.models.FlowerResponse;

import java.util.List;

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.Holder> {

    List<FlowerResponse> flowerList;
    ItemClickListener clickListener;

    public RvAdapter(List<FlowerResponse> flowerList, ItemClickListener clickListener) {
        this.flowerList = flowerList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public RvAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_flower, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RvAdapter.Holder holder, int position) {
        holder.tvTitle.setText(flowerList.get(position).getName());
        holder.tvDescription.setText(flowerList.get(position).getInstructions());
        Glide.with(holder.itemView)
                .load("https://services.hanselandpetal.com/photos/"+ flowerList
                        .get(position)
                        .getPhoto())
                .error(R.drawable.ic_launcher_background)
                .centerCrop()
                .into(holder.ivFlower);
        holder.bind(flowerList.get(position),clickListener);
    }

    @Override
    public int getItemCount() {
        return flowerList.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvDescription;
        ImageView ivFlower;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivFlower = itemView.findViewById(R.id.ivFlower);
        }

        public void bind(FlowerResponse flower, ItemClickListener listener){
            itemView.setOnClickListener(v -> listener.itemClickListener(flower));
        }
    }

    interface ItemClickListener{
        void itemClickListener(FlowerResponse flower);
    }

}
