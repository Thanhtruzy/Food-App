package com.name.foodapp.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.name.foodapp.R;
import com.name.foodapp.adapters.OrderDetailAdapter;
import com.name.foodapp.databinding.ActivityOrderDetailBinding;
import com.name.foodapp.model.MealDetail;
import com.name.foodapp.model.OrderDetail;
import com.name.foodapp.model.OrderDetailModel;
import com.name.foodapp.model.OrderHistory;
import com.name.foodapp.retrofit.FoodAppApi;
import com.name.foodapp.retrofit.RetrofitInstance;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailActivity extends AppCompatActivity {

    private ActivityOrderDetailBinding binding;
    private OrderHistory orderHistory;
    private OrderDetailAdapter adapter;
    private List<OrderDetail> orderDetails = new ArrayList<>();
    private List<MealDetail> mealDetails = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_detail);

        // Get the orderHistory object from the intent
        orderHistory = (OrderHistory) getIntent().getSerializableExtra("orderHistory");

        // Initialize RecyclerView
        initRecyclerView();

        // Fetch order details
        fetchOrderDetails();
    }

    private void initRecyclerView() {
        binding.recyclerOrderDetail.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerOrderDetail.setLayoutManager(layoutManager);
        adapter = new OrderDetailAdapter(orderDetails, mealDetails);
        binding.recyclerOrderDetail.setAdapter(adapter);

        binding.tvback.setOnClickListener(v -> finish());
    }

    private void fetchOrderDetails() {
        FoodAppApi api = RetrofitInstance.getRetrofit().create(FoodAppApi.class);
        Call<OrderDetailModel> call = api.getOrderDetail(orderHistory.getId());

        call.enqueue(new Callback<OrderDetailModel>() {
            @Override
            public void onResponse(Call<OrderDetailModel> call, Response<OrderDetailModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    OrderDetailModel orderDetailModel = response.body();
                    if (orderDetailModel.isSuccess() == 1) {
                        orderDetails.clear();
                        mealDetails.clear();

                        // Ensure the lists are not null before adding
                        if (orderDetailModel.getData() != null) {
                            orderDetails.addAll(orderDetailModel.getData());
                        }

                        if (orderDetailModel.getMealDetails() != null) {
                            mealDetails.addAll(orderDetailModel.getMealDetails());
                        }

                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(OrderDetailActivity.this, orderDetailModel.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("OrderDetailActivity", "Response code: " + response.code() + ", Message: " + response.message());
                    Toast.makeText(OrderDetailActivity.this, "Failed to fetch data: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OrderDetailModel> call, Throwable t) {
                Log.e("OrderDetailActivity", "Error fetching order details", t);
                Toast.makeText(OrderDetailActivity.this, "An error occurred: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
