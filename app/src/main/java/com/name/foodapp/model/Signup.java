package com.name.foodapp.model;

public class Signup {
    private int success;
    private String message;
    private User user;

    public Signup(int success, String message, User user) {
        this.success = success;
        this.message = message;
        this.user = user;
    }

    public int getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }


}
