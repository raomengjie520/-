package com.zuoye.newspaper;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.zuoye.newspaper.db.UserManager;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText, repasswordEditText;

    private UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userManager = new UserManager(this);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        repasswordEditText = findViewById(R.id.repasswordEditText);
        Button registerButton = findViewById(R.id.registerButton);


        registerButton.setOnClickListener(v -> register());
        TextView loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(v -> finish());
    }

    private void register() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String repassword = repasswordEditText.getText().toString().trim();
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
        } else if(!password.equals(repassword)){
            Toast.makeText(this, "两次输入的密码不一致！", Toast.LENGTH_SHORT).show();
        } else if(userManager.isUserExists(username)){
            Toast.makeText(this, "用户已存在", Toast.LENGTH_SHORT).show();
        } else {
            userManager.insertUser(username,password);
            Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }
}