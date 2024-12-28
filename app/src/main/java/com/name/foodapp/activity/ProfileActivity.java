package com.name.foodapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.CompoundButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.name.foodapp.R;
import com.name.foodapp.databinding.ActivityProfileBinding;
import com.name.foodapp.model.User;
import com.name.foodapp.model.UserLocalStore;

public class ProfileActivity extends AppCompatActivity {
    private ActivityProfileBinding binding;
    private UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);

        // Initialize UserLocalStore
        userLocalStore = new UserLocalStore(this);

        // Initialize views and listeners
        initView();

        // Display user information
        displayUserInformation();
    }

    private void initView() {
        binding.floatingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cart = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(cart);
            }
        });

        binding.home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(home);
            }
        });

        binding.oderdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order = new Intent(getApplicationContext(), OrderHistoryActivity.class);
                startActivity(order);
            }
        });

        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutDialog(); // Show logout confirmation dialog
            }
        });

        binding.chkShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.txtPassUser.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    binding.txtPassUser.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                // Move cursor to the end of the text
                binding.txtPassUser.setElevation(binding.txtPassUser.getText().length());
            }
        });
    }

    private void displayUserInformation() {
        User user = userLocalStore.getLoggedInUser();
        if (user != null) {
            binding.txtNameUser.setText(user.getUsername());
            binding.txtNameUser2.setText(user.getUsername());
            binding.txtEmailUser.setText(user.getEmail());
            binding.txtPassUser.setText(user.getPassword());
        }
    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Đăng xuất");
        builder.setMessage("Bạn có chắc chắn muốn đăng xuất?");
        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logoutUser();
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // Close dialog when user cancels
            }
        });
        builder.show();
    }

    private void logoutUser() {
        userLocalStore.clearUserData(); // Clear saved user data
        userLocalStore.setUserLoggedIn(false); // Set logged-in status to false
        navigateToLogin(); // Redirect to login screen
    }

    private void navigateToLogin() {
        Intent intent = new Intent(ProfileActivity.this, LogInActivity.class);
        startActivity(intent);
        finish();
    }
}
