package com.name.foodapp.model;

import java.util.List;

public class OrderDetailModel {
    private int success;
    private String message;
    private List<OrderDetail> data;
    private List<MealDetail> mealDetails;

    // Getters and Setters
    public int isSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<OrderDetail> getData() {
        return data;
    }

    public void setData(List<OrderDetail> data) {
        this.data = data;
    }

    public List<MealDetail> getMealDetails() {
        return mealDetails;
    }

    public void setMealDetails(List<MealDetail> mealDetails) {
        this.mealDetails = mealDetails;
    }
}
