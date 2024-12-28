package com.name.foodapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.name.foodapp.databinding.ItemPopularBinding;
import com.name.foodapp.listener.EventClickListener;
import com.name.foodapp.model.Meals;

import java.util.List;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.MyViewHome> {
    private List<Meals> list;
    private EventClickListener listener;



    public PopularAdapter(List<Meals> list, EventClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHome onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPopularBinding itemPopularBinding = ItemPopularBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new MyViewHome(itemPopularBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHome holder, int position) {
        holder.setBinding(list.get(position));
        Glide.with(holder.itemView).load(list.get(position).getStrMealThumb()).into(holder.binding.imagePopular);
    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public class MyViewHome extends RecyclerView.ViewHolder {
        private ItemPopularBinding binding;
        public MyViewHome(ItemPopularBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void setBinding(Meals meals){
            binding.setPopular(meals);
            binding.executePendingBindings();
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onPopularClick(meals);
                }
            });
        }
    }
}
