package com.name.foodapp.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.name.foodapp.R;
import com.name.foodapp.databinding.ItemOrderDetailBinding;
import com.name.foodapp.model.MealDetail;
import com.name.foodapp.model.OrderDetail;
import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder> {

    private List<OrderDetail> orderDetails;
    private List<MealDetail> mealDetails;

    public OrderDetailAdapter(List<OrderDetail> orderDetails, List<MealDetail> mealDetails) {
        this.orderDetails = orderDetails;
        this.mealDetails = mealDetails;
    }

    @NonNull
    @Override
    public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderDetailBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_order_detail,
                parent,
                false
        );
        return new OrderDetailViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailViewHolder holder, int position) {
        OrderDetail orderDetail = orderDetails.get(position);
        holder.bind(orderDetail);

        // Bind the MealDetail corresponding to the OrderDetail
        MealDetail mealDetail = null;
        for (MealDetail detail : mealDetails) {
            if (detail.getId() == orderDetail.getIdMeal()) {
                mealDetail = detail;
                break;
            }
        }
        if (mealDetail == null) {
            Log.e("OrderDetailAdapter", "Meal not found for OrderDetail: " + orderDetail.getIdMeal());
        } else {
            Log.d("OrderDetailAdapter", "Meal found: ");
        }

        holder.binding.setMealDetail(mealDetail);
    }

    @Override
    public int getItemCount() {
        return orderDetails.size();
    }

    static class OrderDetailViewHolder extends RecyclerView.ViewHolder {
        final ItemOrderDetailBinding binding;

        OrderDetailViewHolder(ItemOrderDetailBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(OrderDetail orderDetail) {
            binding.setOrderDetail(orderDetail);
            binding.executePendingBindings();
        }
    }
}
