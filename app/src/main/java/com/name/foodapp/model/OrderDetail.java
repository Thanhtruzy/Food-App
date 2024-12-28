package com.name.foodapp.model;

public class OrderDetail {
    private int idorder;
    private int idMeal;
    private int amount;
    private double price;

    // Getters and Setters
    public int getIdOrder() {
        return idorder;
    }

    public void setIdOrder(int idorder) {
        this.idorder = idorder;
    }

    public int getIdMeal() {
        return idMeal;
    }

    public void setIdMeal(int idMeal) {
        this.idMeal = idMeal;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
