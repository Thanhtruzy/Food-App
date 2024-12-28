package com.name.foodapp.listener;

import com.name.foodapp.model.OrderHistory;

public interface OrderHistoryListener {
    void onItemClick(OrderHistory orderHistory);
}
