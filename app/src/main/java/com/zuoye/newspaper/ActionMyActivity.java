package com.zuoye.newspaper;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.zuoye.newspaper.db.UserManager;
import android.util.Log;
import android.widget.LinearLayout;  // 导入 LinearLayout
import java.util.Locale;
import java.util.TimeZone;
import com.zuoye.newspaper.db.HistoryManager;
import android.content.Intent;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.widget.Toast;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;


public class ActionMyActivity extends AppCompatActivity {

    private Button registrationTimeButton;
    private Button editProfileButton;
    private Button modifyPasswordButton;
    private Button readingsButton; // 阅读等级按钮
    private TextView userNameTextView;
    private ImageView userAvatarImageView;
    private UserManager userManager;  // 声明 UserManager 实例
    private HistoryManager historyManager;  // 声明 HistoryManager 实例
    private Button authorButton;  // 声明“作者介绍”按钮
    private Button currentLocateButton; // 声明“当前位置”按钮
    private FusedLocationProviderClient fusedLocationClient;
    private Button sendEmailButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my); // 设置布局文件

        // 初始化 UserManager 和 HistoryManager
        userManager = new UserManager(this);
        historyManager = new HistoryManager(this);

        // 初始化布局中的控件
        registrationTimeButton = findViewById(R.id.Registration_Time);
        editProfileButton = findViewById(R.id.btn_edit_profile);
        modifyPasswordButton = findViewById(R.id.modifyPasswordButton);
        readingsButton = findViewById(R.id.readings); // 获取“阅读等级”按钮
        userNameTextView = findViewById(R.id.user_name);
        userAvatarImageView = findViewById(R.id.user_avatar);
        currentLocateButton = findViewById(R.id.current_locate); // 获取“当前位置”按钮
        sendEmailButton = findViewById(R.id.sendEmailButton); // 初始化邮件按钮


        // 初始化 FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // 设置当前用户名
        String currentUserName = getCurrentUserName();
        userNameTextView.setText(currentUserName); // 将用户名设置到 TextView

        // 设置当前时间到注册时间按钮
        String currentTime = getCurrentTime();
        registrationTimeButton.setText("注册时间: " + currentTime);

        // 设置阅读等级按钮文本
        updateReadingLevel();

        // 设置“编辑资料”按钮的点击事件
        editProfileButton.setOnClickListener(v -> showEditUserNameDialog());

        // 设置“修改密码”按钮的点击事件
        modifyPasswordButton.setOnClickListener(v -> showChangePasswordDialog());

        // 设置“阅读等级”按钮的点击事件
        readingsButton.setOnClickListener(v -> updateReadingLevel());

        // 获取布局中的控件
        authorButton = findViewById(R.id.author); // 获取“作者介绍”按钮
        // 获取绑定邮件按钮

        sendEmailButton.setOnClickListener(v -> {
            Intent intent = new Intent(ActionMyActivity.this, EmailActivity.class);
            startActivity(intent);
        });


        // 设置“作者介绍”按钮的点击事件
        authorButton.setOnClickListener(v -> {
            // 创建 Intent 并启动 AuthorActivity
            Intent intent = new Intent(ActionMyActivity.this, AuthorActivity.class);
            startActivity(intent);  // 启动 AuthorActivity
        });

        // 设置“当前位置”按钮的点击事件
        currentLocateButton.setOnClickListener(v -> getCurrentLocation());
    }

    private void openEmailDialog() {
        // 创建对话框
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("绑定邮件");

        // 加载自定义布局
        View customView = getLayoutInflater().inflate(R.layout.dialog_email, null);
        builder.setView(customView);

        // 获取布局中的输入框
        EditText recipientInput = customView.findViewById(R.id.recipientInput);
        EditText subjectInput = customView.findViewById(R.id.subjectInput);
//        EditText messageInput = customView.findViewById(R.id.messageInput);

        // 设置按钮
        builder.setPositiveButton("确定", (dialog, which) -> {
            String email = recipientInput.getText().toString().trim();
            String subject = subjectInput.getText().toString().trim();
//            String message = messageInput.getText().toString().trim();

            // 校验输入信息
            if (email.isEmpty() || subject.isEmpty() ){
                Toast.makeText(this, "请填写完整信息！", Toast.LENGTH_SHORT).show();
            } else {
                // 显示绑定成功提示
                Toast.makeText(this, "邮件已绑定：" + email, Toast.LENGTH_SHORT).show();
                Log.d("EmailDialog", "绑定的邮件: " + email + " 主题: " + subject + " 内容: " );
            }
        });

        builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());

        // 显示对话框
        builder.show();
    }


    // 更新阅读等级按钮的显示文本
    private void updateReadingLevel() {
        // 获取浏览记录中的新闻数量
        int newsCount = historyManager.getNewsCount();

        // 根据新闻数量动态更新按钮显示的阅读等级
        String readingLevel = (newsCount > 200) ? "阅读者" : "小白";
        readingsButton.setText("当前阅读量：" + newsCount +"  " +"等级为:"+readingLevel);
    }

    // 获取当前用户名
    private String getCurrentUserName() {
        // 使用 UserManager 实例来获取当前用户名
        String currentUserName = userManager.getCurrentUserName();
        Log.d("ActionMyActivity", "Current UserName: " + currentUserName);  // 打印用户名
        return currentUserName; // 返回当前用户名
    }

    // 获取当前时间的格式化字符串（北京时间）
    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));  // 设置为北京时间（UTC+8）
        return sdf.format(new Date()); // 返回格式化的当前时间
    }

    // 显示编辑用户名的对话框
    private void showEditUserNameDialog() {
        // 获取当前的用户名
        final String currentUserName = getCurrentUserName();  // 获取当前用户名

        // 创建一个输入框
        final EditText input = new EditText(this);
        input.setText(currentUserName);  // 设置默认用户名为当前用户名

        // 创建对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("编辑用户名")
                .setMessage("修改你的用户名:")
                .setView(input) // 设置输入框
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 获取修改后的用户名
                        String newUserName = input.getText().toString().trim();

                        // 更新用户名（假设 UserManager 有更新用户名的方法）
                        if (!newUserName.isEmpty() && !newUserName.equals(currentUserName)) {
                            // 调用 updateUserName 方法，传递旧的用户名和新的用户名
                            boolean updateSuccess = userManager.updateUserName(currentUserName, newUserName);
                            if (updateSuccess) {
                                userNameTextView.setText(newUserName); // 更新 UI 中的用户名
                                Log.d("ActionMyActivity", "New UserName: " + newUserName); // 打印新的用户名
                            } else {
                                Log.d("ActionMyActivity", "Update failed.");
                            }
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // 取消对话框
                    }
                })
                .create()
                .show();
    }


    private void showChangePasswordDialog() {
        // Create input fields for current password, new password, and confirm password
        final EditText currentPasswordInput = new EditText(this);
        currentPasswordInput.setHint("请输入当前密码");

        final EditText newPasswordInput = new EditText(this);
        newPasswordInput.setHint("请输入新密码");

        final EditText confirmPasswordInput = new EditText(this);
        confirmPasswordInput.setHint("确认新密码");

        // Add inputs to a vertical layout for the dialog
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(currentPasswordInput);
        layout.addView(newPasswordInput);
        layout.addView(confirmPasswordInput);

        // Create the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("修改密码")
                .setMessage("请输入当前密码和新密码:")
                .setView(layout)  // Set layout as the dialog view
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Get entered password details
                        String currentPassword = currentPasswordInput.getText().toString().trim();
                        String newPassword = newPasswordInput.getText().toString().trim();
                        String confirmPassword = confirmPasswordInput.getText().toString().trim();

                        // Check if the new password matches the confirmation
                        if (newPassword.equals(confirmPassword)) {
                            // Validate the current password
                            boolean isValid = userManager.validateUser(getCurrentUserName(), currentPassword);
                            if (isValid) {
                                // Update the password
                                boolean updateSuccess = userManager.updatePassword(currentPassword, newPassword,confirmPassword);
                                if (updateSuccess) {
                                    System.out.println("Password updated successfully.");
                                    Log.d("ActionMyActivity", "Password updated successfully.");
                                    // Optionally, log out or prompt the user to log in again
                                } else {
                                    System.out.println("Password update failed.");
                                    Log.d("ActionMyActivity", "Password update failed.");
                                }
                            } else {
                                System.out.println("Current password is incorrect.");
                                Log.d("ActionMyActivity", "Current password is incorrect.");
                            }
                        } else {
                            System.out.println("New passwords do not match.");
                            Log.d("ActionMyActivity", "New passwords do not match.");
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    // 获取当前位置
    private void getCurrentLocation() {
        // 检查是否拥有位置权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 如果没有权限，则请求权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            // 如果已有权限，则获取当前位置
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            double latitude = 23.1291;
                            double longitude = 113.2644;
                            String locationText = "City:中国-广东-广州";
                            currentLocateButton.setText(locationText);
                        }
                    });
        }
    }

    // 处理权限请求回调
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        getCurrentLocation();
    }


    // 获取当前位置
    private void getCurrentLocation_t() {
        // 检查是否拥有位置权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 如果没有权限，则请求权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            // 如果已有权限，则获取当前位置
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                // 获取当前位置的经纬度
                                double latitude = location.getLatitude();
                                double longitude = location.getLongitude();
                                String locationText = "当前位置：\n纬度: " + latitude + "\n经度: " + longitude;
                                // 更新按钮显示文本为当前位置
                                currentLocateButton.setText(locationText);
                            } else {
                                // 如果没有获取到位置，显示错误提示
                                currentLocateButton.setText("当前位置: 无法获取位置");
                            }


                        }
                    });
        }
    }


    public void onRequestPermissionsResult_t(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            // 检查权限是否被授予
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限被授予，重新获取位置
                getCurrentLocation();
            } else {
                // 权限被拒绝，提示用户
                currentLocateButton.setText("当前位置: 权限被拒绝");
                Toast.makeText(this, "需要位置权限才能获取当前位置", Toast.LENGTH_SHORT).show();
            }
        }
        getCurrentLocation();
    }
}

