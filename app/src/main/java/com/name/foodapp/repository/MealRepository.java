package com.name.foodapp.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.name.foodapp.model.MealModel;
import com.name.foodapp.model.Meals;
import com.name.foodapp.retrofit.FoodAppApi;
import com.name.foodapp.retrofit.RetrofitInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MealRepository {
    private static MealRepository instance;
    private final FoodAppApi foodAppApi;

    public MealRepository() {
        foodAppApi = RetrofitInstance.getRetrofit().create(FoodAppApi.class);
    }

    public static synchronized MealRepository getInstance() {
        if (instance == null) {
            instance = new MealRepository();
        }
        return instance;
    }

    public LiveData<MealModel> getMeals(int idcate) {
        MutableLiveData<MealModel> data = new MutableLiveData<>();
        foodAppApi.getMeals(idcate).enqueue(new Callback<MealModel>() {
            @Override
            public void onResponse(Call<MealModel> call, Response<MealModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<MealModel> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<List<Meals>> searchMeals(String query) {
        MutableLiveData<List<Meals>> data = new MutableLiveData<>();
        foodAppApi.searchMeals(query).enqueue(new Callback<List<Meals>>() {
            @Override
            public void onResponse(Call<List<Meals>> call, Response<List<Meals>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Meals>> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }
}
