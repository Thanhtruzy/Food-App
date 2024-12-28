package com.name.foodapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.name.foodapp.R;
import com.name.foodapp.adapters.OrderHistoryAdapter;
import com.name.foodapp.databinding.ActivityOrderHistoryBinding;
import com.name.foodapp.model.OrderHistory;
import com.name.foodapp.model.UserLocalStore;
import com.name.foodapp.retrofit.FoodAppApi;
import com.name.foodapp.retrofit.RetrofitInstance;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryActivity extends AppCompatActivity {

    private ActivityOrderHistoryBinding binding;
    private UserLocalStore userLocalStore;
    private OrderHistoryAdapter adapter;
    private List<OrderHistory> orderHistoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_history);

        userLocalStore = new UserLocalStore(this);
        initView();
        fetchOrderHistory();

    }

    private void initView() {
        binding.recyclerOrderHistory.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerOrderHistory.setLayoutManager(layoutManager);

        binding.floatingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cart = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(cart);
            }
        });
        binding.home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(home);
            }
        });
        binding.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profile = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(profile);
            }
        });
    }

    private void fetchOrderHistory() {
        int iduser = Integer.parseInt(userLocalStore.getLoggedInUser().getId());
        FoodAppApi api = RetrofitInstance.getRetrofit().create(FoodAppApi.class);
        Call<List<OrderHistory>> call = api.getOrderHistory(iduser);

        call.enqueue(new Callback<List<OrderHistory>>() {
            @Override
            public void onResponse(Call<List<OrderHistory>> call, Response<List<OrderHistory>> response) {
                if (response.isSuccessful()) {
                    orderHistoryList = response.body();
                    adapter = new OrderHistoryAdapter(orderHistoryList, orderHistory -> {
                        // Xử lý khi item được click
                    });
                    binding.recyclerOrderHistory.setAdapter(adapter);
                } else {
                    Toast.makeText(OrderHistoryActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<OrderHistory>> call, Throwable t) {
                Log.e("OrderHistoryActivity", t.getMessage());
                Toast.makeText(OrderHistoryActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
