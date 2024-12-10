package com.zuoye.newspaper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.zuoye.newspaper.db.UserManager;

import java.util.Random;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText, captchaEditText;
    private UserManager userManager;
    private ImageView captchaImageView;
    private Button getCaptchaButton, loginButton, adminLoginButton;
    private String generatedCaptcha;  // 用于存储生成的验证码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        captchaEditText = findViewById(R.id.captchaEditText);
        captchaImageView = findViewById(R.id.captchaImageView);
        getCaptchaButton = findViewById(R.id.getCaptchaButton);
        loginButton = findViewById(R.id.loginButton);
        adminLoginButton = findViewById(R.id.adminLoginButton);
        TextView registerButton = findViewById(R.id.registerButton);

        userManager = new UserManager(this);

        // 普通用户登录按钮点击事件
        loginButton.setOnClickListener(v -> login());

        // 注册按钮点击事件
        registerButton.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));

        // 超级管理员登录按钮点击事件
        adminLoginButton.setOnClickListener(v -> handleAdminLogin());

        // 获取验证码按钮点击事件
        getCaptchaButton.setOnClickListener(v -> generateCaptcha());
    }

    // 普通用户登录逻辑
    private void login() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String captcha = captchaEditText.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty() || captcha.isEmpty()) {
            Toast.makeText(this, "请输入用户名、密码和验证码", Toast.LENGTH_SHORT).show();
        } else if (captcha.equals(generatedCaptcha)) {  // 验证验证码
            if (userManager.validateUser(username, password)) {
                Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "验证码错误", Toast.LENGTH_SHORT).show();
        }
    }

    // 超级管理员登录逻辑
    private void handleAdminLogin() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // 假设超级管理员的用户名和密码是固定的
        String adminUsername = "admin";
        String adminPassword = "123";

        if (username.equals(adminUsername) && password.equals(adminPassword)) {
            // 登录成功，跳转到管理员界面
            Toast.makeText(this, "超级管理员登录成功", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, AdminActivity.class));
            finish();
        } else {
            // 登录失败，提示错误信息
            Toast.makeText(this, "超级管理员用户名或密码错误", Toast.LENGTH_SHORT).show();
        }
    }

    // 生成验证码并显示在ImageView中
    private void generateCaptcha() {
        Random random = new Random();
        generatedCaptcha = String.format("%04d", random.nextInt(10000));  // 生成一个4位数字验证码

        // 创建一个 Bitmap，用于绘制验证码
        Bitmap captchaBitmap = Bitmap.createBitmap(200, 80, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(captchaBitmap);

        // 设置画笔
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);  // 设置文字颜色为黑色
        paint.setTextSize(40);        // 设置文字大小
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));  // 设置文字字体
        paint.setAntiAlias(true);     // 开启抗锯齿，提升文字显示质量

        // 在画布上绘制验证码文本
        canvas.drawText(generatedCaptcha, 50, 50, paint);

        // 将生成的验证码图片设置为 ImageView 的资源
        captchaImageView.setImageBitmap(captchaBitmap);

        // 提示用户验证码已生成
        Toast.makeText(this, "验证码已生成", Toast.LENGTH_SHORT).show();
    }
}
