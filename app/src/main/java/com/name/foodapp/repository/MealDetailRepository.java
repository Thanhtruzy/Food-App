package com.name.foodapp.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import com.name.foodapp.model.MealDetailModel;
import com.name.foodapp.retrofit.FoodAppApi;
import com.name.foodapp.retrofit.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MealDetailRepository {
   private FoodAppApi appApi;

    public MealDetailRepository() {
        appApi = RetrofitInstance.getRetrofit().create(FoodAppApi.class);
    }
    public MutableLiveData<MealDetailModel> getMealDetail(int id){
        MutableLiveData<MealDetailModel> data = new MutableLiveData<>();
        appApi.getMealsDetail(id).enqueue(new Callback<MealDetailModel>() {
            @Override
            public void onResponse(Call<MealDetailModel> call, Response<MealDetailModel> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<MealDetailModel> call, Throwable t) {
                data.setValue(null);
                Log.d("log", t.getMessage());
            }
        });
        return data;
    }
}
