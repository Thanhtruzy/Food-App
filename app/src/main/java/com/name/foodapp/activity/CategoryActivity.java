package com.name.foodapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.name.foodapp.R;
import com.name.foodapp.adapters.MealAdapter;
import com.name.foodapp.databinding.ActivityCategoryBinding;
import com.name.foodapp.model.MealModel;
import com.name.foodapp.model.Meals;
import com.name.foodapp.viewModel.CategoryViewModel;

import java.util.List;

public class CategoryActivity extends AppCompatActivity {
    ActivityCategoryBinding binding;
    CategoryViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_category);
        initView();

        viewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        String query = getIntent().getStringExtra("query");
        if (query != null && !query.isEmpty()) {
            searchMeals(query);
        } else {
            initData();
        }

        binding.tvback.setOnClickListener(v -> finish());
    }

    private void initView() {
        binding.rcCategory.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.rcCategory.setLayoutManager(layoutManager);
    }

    private void initData() {
        int idcate = getIntent().getIntExtra("idcate", 1);
        String namecate = getIntent().getStringExtra("namecate");

        viewModel.mealModelMutableLiveData(idcate).observe(this, mealModel -> {
            if (mealModel != null && mealModel.isSuccess()) {
                List<Meals> meals = mealModel.getResult();
                updateRecyclerView(meals, namecate + ": " + meals.size());
            } else {
                Log.e("CategoryActivity", "Failed to fetch meals or response is unsuccessful.");
            }
        });
    }

    private void searchMeals(String query) {
        viewModel.searchMeals(query).observe(this, meals -> {
            if (meals != null && !meals.isEmpty()) {
                updateRecyclerView(meals, "Search results: " + meals.size());
            } else {
                Log.e("CategoryActivity", "Failed to fetch meals or response is unsuccessful.");
                binding.tvname.setText("No search results found");
            }
        });
    }

    private void updateRecyclerView(List<Meals> meals, String message) {
        MealAdapter adapter = new MealAdapter(meals, this::onMealClick);
        binding.rcCategory.setAdapter(adapter);
        binding.tvname.setText(message);
    }

    private void onMealClick(Meals meals) {
        Intent intent = new Intent(getApplicationContext(), ShowDetailActivity.class);
        intent.putExtra("id", meals.getIdMeal());
        startActivity(intent);
    }
}
