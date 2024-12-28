package com.name.foodapp.model;

public class Login {
    private String status;
    private User user;

    // Constructor, getters và setters

    public Login(String status, User user) {
        this.status = status;
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public User getUser() {
        return user;
    }

    public class User {
        private String id;
        private String username;
        private String email;

        public User(String id ,String username, String email) {
            this.id = id;
            this.username = username;
            this.email = email;
        }

        public String getId() {
            return id;
        }

        public String getUsername() {
            return username;
        }
        public String getEmail() {
            return email;
        }


    }
}


