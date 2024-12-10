package com.zuoye.newspaper;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EmailActivity extends AppCompatActivity {
    private EditText recipientInput;
    private EditText subjectInput;
    private EditText messageInput;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_email); // 使用你的 email 布局文件

        // 初始化布局中的控件
        recipientInput = findViewById(R.id.recipientInput);
        subjectInput = findViewById(R.id.subjectInput);
//        messageInput = findViewById(R.id.messageInput);
        submitButton = findViewById(R.id.submitButton);

        // 设置提交按钮点击事件
        submitButton.setOnClickListener(v -> {
            String email = recipientInput.getText().toString().trim();
            String subject = subjectInput.getText().toString().trim();
            String message = messageInput.getText().toString().trim();

            if (email.isEmpty() || subject.isEmpty() || message.isEmpty()) {
                // 显示错误提示
                Toast.makeText(EmailActivity.this, "请填写完整信息！", Toast.LENGTH_SHORT).show();
            } else {
                // 可以保存或处理邮箱信息
                Toast.makeText(EmailActivity.this, "邮箱绑定成功！", Toast.LENGTH_SHORT).show();
                finish(); // 关闭当前页面
            }
        });
    }
}
