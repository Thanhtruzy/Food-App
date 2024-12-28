package com.name.foodapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.name.foodapp.databinding.ActivitySignUpBinding;
import com.name.foodapp.model.Signup;
import com.name.foodapp.retrofit.FoodAppApi;
import com.name.foodapp.retrofit.RetrofitInstance;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = SignUpActivity.class.getSimpleName();
    private ActivitySignUpBinding binding;
    private ProgressDialog pDialog;

    private FoodAppApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initView();

        // Khởi tạo Retrofit Service
        api = RetrofitInstance.getRetrofit().create(FoodAppApi.class);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Đang đăng ký...");
        pDialog.setCanceledOnTouchOutside(false);

        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = binding.edName.getText().toString().trim();
                String password = binding.edPasswd.getText().toString().trim();
                String email = binding.edEmail.getText().toString().trim();
                String confirmPassword = binding.edPasswd2.getText().toString().trim();

                if (validateInputs(username, password, email, confirmPassword)) {
                    registerUser(username, email, password);
                }
            }
        });
    }

    private void initView() {
        binding.tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(getApplicationContext(), LogInActivity.class);
                startActivity(login);
            }
        });
    }

    private boolean validateInputs(String username, String password, String email, String confirmPassword) {
        if (username.isEmpty()) {
            binding.edName.setError("Vui lòng nhập tên đăng nhập!");
            return false;
        }
        if (password.isEmpty()) {
            binding.edPasswd.setError("Vui lòng nhập mật khẩu!");
            return false;
        }
        if (!password.equals(confirmPassword)) {
            binding.edPasswd2.setError("Mật khẩu xác nhận không khớp!");
            return false;
        }
        if (!isValidEmail(email)) {
            binding.edEmail.setError("Email sai định dạng!");
            return false;
        }
        return true;
    }

    private boolean isValidEmail(String target) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void registerUser(String username, String email, String password) {
        pDialog.show();
        Call<Signup> call = api.signUp(username, email, password);
        call.enqueue(new Callback<Signup>() {
            @Override
            public void onResponse(Call<Signup> call, Response<Signup> response) {
                pDialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    Signup signupResponse = response.body();
                    Toast.makeText(SignUpActivity.this, signupResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
                    startActivity(intent);
                } else {
                    // Xử lý trường hợp phản hồi không thành công
                    try {
                        String errorResponse = response.errorBody().string();
                        Log.e(TAG, "Registration failed: " + errorResponse);
                        Toast.makeText(SignUpActivity.this, "Registration failed: " + errorResponse, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(SignUpActivity.this, "Registration failed: Unable to parse error response", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Signup> call, Throwable t) {
                pDialog.dismiss();
                Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
                startActivity(intent);
            }
        });
    }
}
