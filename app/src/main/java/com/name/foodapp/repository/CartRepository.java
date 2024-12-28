package com.name.foodapp.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.name.foodapp.model.MessModel;
import com.name.foodapp.retrofit.FoodAppApi;
import com.name.foodapp.retrofit.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartRepository {
    private FoodAppApi api;
    MutableLiveData<MessModel> data;

    public CartRepository() {
        api = RetrofitInstance.getRetrofit().create(FoodAppApi.class);
        data = new MutableLiveData<>();
    }

    public void checkOut(int iduser, int amount, double total, String detail) {
        api.postCart(iduser, amount, total, detail).enqueue(new Callback<MessModel>() {
            @Override
            public void onResponse(Call<MessModel> call, Response<MessModel> response) {
                if (response.isSuccessful()) {
                    data.postValue(response.body());
                } else {
                    // Handle API failure
                    data.postValue(new MessModel(false, "Error occurred while processing the order"));
                }
            }

            @Override
            public void onFailure(Call<MessModel> call, Throwable t) {
                data.postValue(new MessModel(false, t.getMessage()));
                Log.d("logg", t.getMessage());
            }
        });
    }

    public MutableLiveData<MessModel> messModelMutableLiveData() {
        return data;
    }
}
