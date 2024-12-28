package com.name.foodapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.name.foodapp.R;
import com.name.foodapp.Utils.Utils;
import com.name.foodapp.adapters.CartAdapter;
import com.name.foodapp.databinding.ActivityCartBinding;
import com.name.foodapp.listener.ChangeNumListener;
import com.name.foodapp.model.Cart;
import com.name.foodapp.model.MessModel;
import com.name.foodapp.model.User;
import com.name.foodapp.model.UserLocalStore;
import com.name.foodapp.viewModel.CartViewModel;

import java.text.DecimalFormat;

import java.util.ArrayList;
import java.util.List;
import io.paperdb.Paper;

public class CartActivity extends AppCompatActivity {
    ActivityCartBinding binding;
    CartViewModel viewModel;
    private UserLocalStore userLocalStore;
    int item;
    double price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cart);

        userLocalStore = new UserLocalStore(this);

        Paper.init(this);
        initView();
        initData();
        totalPrice();
        initControl();
    }

    private void initControl() {
        binding.tvback.setOnClickListener(v -> finish());

        viewModel.init();
        viewModel.messModelMutableLiveData().observe(this, new Observer<MessModel>() {
            @Override
            public void onChanged(MessModel messModel) {
                if (messModel != null) {
                    if (messModel.isSuccess()) {
                        Toast.makeText(getApplicationContext(), messModel.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent home = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(home);
                        Utils.cartList.clear(); // Clear local cart list
                        Paper.book().delete("Cart"); // Clear cart data stored in Paper
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error: " + messModel.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        User user = userLocalStore.getLoggedInUser();
        if (user != null) {
            String userIdStr = user.getId();
            try {
                int userId = Integer.parseInt(userIdStr);
                binding.btncheckout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String cart = new Gson().toJson(Utils.cartList);
                        Log.d("logg", cart);
                        viewModel.checkOut(userId, item, price, cart);
                    }
                });

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid user ID", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    private void initData() {
        List<Cart> carts = Paper.book().read("Cart");
        Utils.cartList = carts;
        if (Utils.cartList != null) {
            // Kiểm tra MealDetail có phải là null không
            for (Cart cart : Utils.cartList) {
                if (cart.getMealDetail() == null) {
                    Log.e("CartActivity", "MealDetail là null cho mục giỏ hàng: " + cart.toString());
                    // Xử lý trường hợp MealDetail là null
                }
            }
            // Tiến hành thiết lập adapter
            CartAdapter adapter = new CartAdapter(this, Utils.cartList, new ChangeNumListener() {
                @Override
                public void change() {
                    totalPrice();
                }
            });
            binding.recyclercart.setAdapter(adapter);
        } else {
            binding.txtmess.setVisibility(View.VISIBLE);
            binding.scrollView.setVisibility(View.INVISIBLE);
        }
    }


    private void totalPrice() {
        item = 0;
        price = 0;
        if (Utils.cartList != null) {
            for (Cart cart : Utils.cartList) {
                item += cart.getAmount();
                if (cart.getMealDetail() != null) {
                    price += (cart.getAmount() * cart.getMealDetail().getPrice());
                } else {
                    Log.e("CartActivity", "MealDetail is null for cart item: " + cart);
                }
            }
        }
        binding.txtitem.setText(String.valueOf(item));
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        binding.txtprice.setText(decimalFormat.format(price) + "đ");
    }

    private void initView() {
        viewModel = new ViewModelProvider(this).get(CartViewModel.class);
        binding.recyclercart.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclercart.setLayoutManager(layoutManager);
    }
}
