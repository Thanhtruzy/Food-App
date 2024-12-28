package com.name.foodapp.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.name.foodapp.databinding.ActivityLogInBinding;
import com.name.foodapp.model.Login;
import com.name.foodapp.model.User;
import com.name.foodapp.model.UserLocalStore;
import com.name.foodapp.retrofit.FoodAppApi;
import com.name.foodapp.retrofit.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogInActivity extends AppCompatActivity {
    private ActivityLogInBinding binding;
    private FoodAppApi api;
    private UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLogInBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Khởi tạo Retrofit Service
        api = RetrofitInstance.getRetrofit().create(FoodAppApi.class);

        // Khởi tạo UserLocalStore
        userLocalStore = new UserLocalStore(this);

        // Kiểm tra nếu người dùng đã đăng nhập
        if (userLocalStore.getLoggedInUser() != null) {
            navigateToHome();
        }

        binding.tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(home);
            }
        });

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.txtEmail.getText().toString().trim();
                String password = binding.txtPass.getText().toString().trim();

                if (validateInputs(email, password)) {
                    if (isNetworkAvailable()) {
                        performLogin(email, password);
                    } else {
                        Toast.makeText(LogInActivity.this, "No network connection. Please check your connection.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private boolean validateInputs(String email, String password) {
        if (!isValidEmail(email)) {
            binding.txtEmail.setError("Email sai định dạng!");
            return false;
        }
        if (password.isEmpty()) {
            binding.txtPass.setError("Vui lòng nhập mật khẩu!");
            return false;
        }
        return true;
    }

    private boolean isValidEmail(String target) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void performLogin(String email, String password) {
        // Gọi API để đăng nhập
        Call<Login> call = api.login(email, password);
        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                if (response.isSuccessful()) {
                    Login loginResponse = response.body();
                    if (loginResponse != null && "success".equals(loginResponse.getStatus())) {
                        Login.User user = loginResponse.getUser();
                        if (user != null) {
                            String id = user.getId();
                            String username = user.getUsername();
                            String userEmail = user.getEmail();
                            // Sử dụng mật khẩu do người dùng nhập
                            String userPassword = password;


                            User userLocal = new User(id, username, userEmail, userPassword);
                            userLocalStore.storeUserData(userLocal);
                            userLocalStore.setUserLoggedIn(true);
                            navigateToHome();
                        }
                    } else {
                        Toast.makeText(LogInActivity.this, "Login failed. Please check your credentials.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LogInActivity.this, "Login failed. Please check your credentials.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Toast.makeText(LogInActivity.this, "Network error. Please check your connection.", Toast.LENGTH_SHORT).show();
                Log.e("LoginActivity", "onFailure: " + t.getMessage(), t);
            }
        });
    }

    private void navigateToHome() {
        Intent intent = new Intent(LogInActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
