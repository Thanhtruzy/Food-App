package com.name.foodapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.name.foodapp.R;
import com.name.foodapp.adapters.CategoryAdapter;
import com.name.foodapp.adapters.ImageAdapter;
import com.name.foodapp.adapters.PopularAdapter;
import com.name.foodapp.databinding.ActivityHomeBinding;
import com.name.foodapp.listener.CategoryListener;
import com.name.foodapp.listener.EventClickListener;
import com.name.foodapp.model.Category;
import com.name.foodapp.model.Meals;
import com.name.foodapp.model.User;
import com.name.foodapp.model.UserLocalStore;
import com.name.foodapp.viewModel.HomeViewModel;

public class HomeActivity extends AppCompatActivity implements CategoryListener, EventClickListener {
    private ActivityHomeBinding binding;
    private HomeViewModel homeViewModel;
    private ImageAdapter mAdapter;
    private UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userLocalStore = new UserLocalStore(this);

        initData();
        initView();

        int[] images = {R.drawable.img_3, R.drawable.img_4, R.drawable.img};
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mAdapter = new ImageAdapter(this, images);
        binding.recyclerView.setAdapter(mAdapter);

        // Display username
        displayUsername();
        // Initialize SearchView
        initSearchView();
    }

    private void initSearchView() {
        binding.editsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(HomeActivity.this, CategoryActivity.class);
                intent.putExtra("query", query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }


    private void initView() {
        binding.rcCategories.setHasFixedSize(true);
        binding.rcCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        binding.rcPopular.setHasFixedSize(true);
        binding.rcPopular.setLayoutManager(new GridLayoutManager(this, 2));

        binding.floatingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cart = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(cart);
            }
        });
        binding.oderhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order = new Intent(getApplicationContext(), OrderHistoryActivity.class);
                startActivity(order);
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

    private void initData() {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.categoryModelMutableLiveData().observe(this, categoryModel -> {
            if (categoryModel.isSuccess()) {
                CategoryAdapter adapter = new CategoryAdapter(categoryModel.getResult(), this);
                binding.rcCategories.setAdapter(adapter);
            }
        });

        homeViewModel.mealsModelMutableLiveData(1).observe(this, mealsModel -> {
            if (mealsModel.isSuccess()) {
                PopularAdapter adapter = new PopularAdapter(mealsModel.getResult(), this);
                binding.rcPopular.setAdapter(adapter);
            }
        });
    }
    private void displayUsername() {
        User user = userLocalStore.getLoggedInUser();
        if (user != null) {
            binding.username.setText(user.getUsername());
        }
    }

    @Override
    public void onCategoryClick(Category category) {
        Intent intent = new Intent(getApplicationContext(), CategoryActivity.class);
        intent.putExtra("idcate", category.getId());
        intent.putExtra("namecate", category.getCategory());
        startActivity(intent);
    }

    @Override
    public void onPopularClick(Meals meals) {
        Intent intent = new Intent(getApplicationContext(), ShowDetailActivity.class);
        intent.putExtra("id", meals.getIdMeal());
        intent.putExtra("strMeal", meals.getIdMeal());
        startActivity(intent);
    }
}
