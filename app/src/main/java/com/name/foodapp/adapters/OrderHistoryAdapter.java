package com.name.foodapp.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.name.foodapp.activity.OrderDetailActivity;
import com.name.foodapp.databinding.ItemOrderHistoryBinding;
import com.name.foodapp.listener.OrderHistoryListener;
import com.name.foodapp.model.OrderHistory;
import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {

    private List<OrderHistory> orderHistoryList;
    private OrderHistoryListener orderHistoryListener;

    public OrderHistoryAdapter(List<OrderHistory> orderHistoryList, OrderHistoryListener orderHistoryListener) {
        this.orderHistoryList = orderHistoryList;
        this.orderHistoryListener = orderHistoryListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemOrderHistoryBinding itemBinding = ItemOrderHistoryBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderHistory orderHistory = orderHistoryList.get(position);
        holder.bind(orderHistory, orderHistoryListener);
    }

    @Override
    public int getItemCount() {
        return orderHistoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemOrderHistoryBinding binding;

        public ViewHolder(@NonNull ItemOrderHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(final OrderHistory orderHistory, final OrderHistoryListener listener) {
            binding.setOrderHistory(orderHistory);
            binding.executePendingBindings();
            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(itemView.getContext(), OrderDetailActivity.class);
                intent.putExtra("orderHistory", orderHistory);
                itemView.getContext().startActivity(intent);
            });
        }
    }
}
