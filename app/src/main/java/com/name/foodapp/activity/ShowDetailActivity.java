package com.name.foodapp.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.name.foodapp.R;
import com.name.foodapp.Utils.Utils;
import com.name.foodapp.databinding.ActivityShowDetailBinding;
import com.name.foodapp.model.Cart;
import com.name.foodapp.model.MealDetail;
import com.name.foodapp.viewModel.ShowDetailViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class ShowDetailActivity extends AppCompatActivity {
    ShowDetailViewModel viewModel;
    ActivityShowDetailBinding binding;
    int amount = 1;
    MealDetail mealDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_show_detail);
        Paper.init(this);
        int id = getIntent().getIntExtra("id", 0);
        getData(id);
        evenClick();

        binding.tvback.setOnClickListener(v -> {
            finish(); // Quay lại
        });
    }

    private void showToData(int id) {
        if (Paper.book().read("Cart") != null) {
            List<Cart> list = Paper.book().read("Cart");
            Utils.cartList = list;
        }

        if (mealDetail != null && Utils.cartList.size() > 0) {
            for (int i = 0; i < Utils.cartList.size(); i++) {
                // Chỉ tiếp tục nếu mealDetail không phải null và ID khớp
                if (Utils.cartList.get(i).getMealDetail() != null &&
                        Utils.cartList.get(i).getMealDetail().getId() == id) {
                    binding.txtamount.setText(Utils.cartList.get(i).getAmount() + "");
                    return; // Dừng vòng lặp khi tìm thấy sản phẩm trùng khớp
                }
            }
        }
        // Thiết lập giá trị mặc định nếu không tìm thấy sản phẩm trùng khớp
        binding.txtamount.setText(amount + "");
    }

    private void evenClick() {
        binding.imageadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentAmount = Integer.parseInt(binding.txtamount.getText().toString());
                // Tăng số lượng
                if (currentAmount < mealDetail.getAmount()) {
                    amount = currentAmount + 1;
                    binding.txtamount.setText(String.valueOf(amount));
                } else {
                    // Hiển thị thông báo nếu số lượng vượt quá số lượng có sẵn
                    Toast.makeText(ShowDetailActivity.this, "Không thể vượt quá số lượng có sẵn", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.imagesub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentAmount = Integer.parseInt(binding.txtamount.getText().toString());
                if (currentAmount > 1) {
                    amount = currentAmount - 1;
                    binding.txtamount.setText(String.valueOf(amount));
                }
            }
        });

        binding.btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart(amount);
            }
        });
    }

    private void addToCart(int amount) {
        if (mealDetail == null) {
            Toast.makeText(this, "Dữ liệu món ăn chưa được tải.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (amount > mealDetail.getAmount()) {
            Toast.makeText(this, "Không thể vượt quá số lượng có sẵn", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean checkExist = false;
        int n = 0;
        if (Utils.cartList != null) {
            for (int i = 0; i < Utils.cartList.size(); i++) {
                if (Utils.cartList.get(i).getMealDetail() != null &&
                        Utils.cartList.get(i).getMealDetail().getId() == mealDetail.getId()) {
                    checkExist = true;
                    n = i;
                    break;
                }
            }
        } else {
            Utils.cartList = new ArrayList<>();
        }

        if (checkExist) {
            Utils.cartList.get(n).setAmount(amount);
        } else {
            Cart cart = new Cart();
            cart.setMealDetail(mealDetail);
            cart.setAmount(amount);
            Utils.cartList.add(cart);
        }
        Toast.makeText(getApplicationContext(), "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
        Paper.book().write("Cart", Utils.cartList);
    }


    private void getData(int id) {
        viewModel = new ViewModelProvider(this).get(ShowDetailViewModel.class);
        viewModel.mealDetailModelMutableLiveData(id).observe(this, mealDetailModel -> {
            if (mealDetailModel.isSuccess() && mealDetailModel.getResult() != null
                    && !mealDetailModel.getResult().isEmpty()) {
                mealDetail = mealDetailModel.getResult().get(0);
                Log.d("log", mealDetail.getMeal());

                binding.txtnamefood.setText(mealDetail.getMeal());
                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                binding.txtprice.setText(decimalFormat.format(mealDetail.getPrice()) + "đ");
                binding.txtdesc.setText(mealDetail.getInstructions());
                binding.amount.setText(String.valueOf(mealDetail.getAmount()));
                Glide.with(this).load(mealDetail.getStrmealthumb()).into(binding.image);

                // Gọi showToData sau khi mealDetail đã được khởi tạo thành công
                showToData(id);
            } else {
                Toast.makeText(this, "Failed to load meal details.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}