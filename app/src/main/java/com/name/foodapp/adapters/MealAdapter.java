package com.name.foodapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.name.foodapp.databinding.ItemMealBinding;
import com.name.foodapp.listener.CategoryItemListener;
import com.name.foodapp.model.Meals;
import java.util.List;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MyViewHome> {
    private List<Meals> mealsList;
    private final CategoryItemListener listener;

    public MealAdapter(List<Meals> mealsList, CategoryItemListener listener) {
        this.mealsList = mealsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHome onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMealBinding binding = ItemMealBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHome(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHome holder, int position) {
        holder.setBinding(mealsList.get(position));
        Glide.with(holder.itemView.getContext())
                .load(mealsList.get(position).getStrMealThumb())
                .into(holder.binding.image);
    }

    @Override
    public int getItemCount() {
        return mealsList != null ? mealsList.size() : 0;
    }

    public void setMeals(List<Meals> mealsList) {
        this.mealsList = mealsList;
        notifyDataSetChanged();
    }

    public class MyViewHome extends RecyclerView.ViewHolder {
        private final ItemMealBinding binding;

        public MyViewHome(ItemMealBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void setBinding(final Meals meals) {
            binding.setMealitem(meals);
            binding.executePendingBindings();
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(meals);
                }
            });
        }
    }
}
