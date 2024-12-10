package com.zuoye.newspaper;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.zuoye.newspaper.db.UserManager;

import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private Button viewUsersButton;
    private LinearLayout usersListContainer;
    private UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // 初始化控件和数据库帮助类
        viewUsersButton = findViewById(R.id.viewUsersButton);
        usersListContainer = findViewById(R.id.usersListContainer);
        userManager = new UserManager(this);

        viewUsersButton.setOnClickListener(v -> {
            List<String> usersList = userManager.getAllUsers();
            usersListContainer.removeAllViews(); // 清空之前的内容

            if (usersList.isEmpty()) {
                TextView noUsersText = new TextView(this);
                noUsersText.setText("没有注册用户");
                noUsersText.setTextColor(getResources().getColor(android.R.color.darker_gray));
                usersListContainer.addView(noUsersText);
            } else {
                for (String user : usersList) {
                    // 创建用户信息布局
                    LinearLayout userLayout = new LinearLayout(this);
                    userLayout.setOrientation(LinearLayout.HORIZONTAL);
                    userLayout.setPadding(0, 10, 0, 10); // 设置一些间距

                    // 创建用户名 TextView
                    TextView userNameTextView = new TextView(this);
                    userNameTextView.setText(user); // 显示用户名
                    userNameTextView.setTextSize(16f);
                    userNameTextView.setTextColor(getResources().getColor(android.R.color.black));
                    userNameTextView.setPadding(0, 0, 20, 0); // 给用户名添加右边的间距

                    // 创建“阅读等级: 小白” TextView
                    TextView levelTextView = new TextView(this);
                    levelTextView.setText("阅读等级: 小白"); // 显示等级
                    levelTextView.setTextSize(16f);
                    levelTextView.setTextColor(getResources().getColor(android.R.color.holo_blue_light)); // 设置为蓝色，增加可见度
                    levelTextView.setPadding(0, 0, 0, 0); // 调整“等级”与用户名之间的距离

                    // 将用户名和等级信息添加到布局中
                    userLayout.addView(userNameTextView);
                    userLayout.addView(levelTextView);

                    // 将每个用户的信息添加到容器中
                    usersListContainer.addView(userLayout);
                }
            }
        });
    }
}
