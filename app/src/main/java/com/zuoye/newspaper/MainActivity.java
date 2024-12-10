package com.zuoye.newspaper;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.zuoye.newspaper.db.HistoryManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView newsListView; // 新闻列表视图
    private NewsAdapter newsAdapter; // 新闻适配器

    private MusicPlayer musicPlayer; // 音乐播放器实例

    private HistoryManager manager; // 历史记录管理器

    // 创建主活动
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化新闻列表和适配器
        newsListView = findViewById(R.id.newsListView);
        newsAdapter = new NewsAdapter(this, null);
        newsListView.setAdapter(newsAdapter);

        // 初始化历史记录管理器
        manager = new HistoryManager(this);

        // 注册广播接收器，用于接收新闻更新广播
        IntentFilter filter = new IntentFilter("com.zuoye.newspaper.ACTION_UPDATE_NEWS");
        registerReceiver(newsUpdateReceiver, filter, Context.RECEIVER_EXPORTED);

        // 启动服务去刷新新闻数据
        startService(new Intent(this, NewsRefreshService.class));

        // 初始化音乐播放器并播放音乐
        musicPlayer = new MusicPlayer();
        musicPlayer.playMusic();

//        // 发送邮件按钮
//        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button sendEmailButton = findViewById(R.id.sendEmailButton);
//        sendEmailButton.setOnClickListener(v -> sendEmail_1());

        // 设置新闻列表的点击事件，点击某条新闻后跳转到新闻详情页面
        newsListView.setOnItemClickListener((parentView, view, position, id) -> {
            List<News> newsList = newsAdapter.getNewsList(); // 获取当前新闻列表
            News selectedNews = newsList.get(position); // 获取点击的新闻
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.CHINA);

            // 保存历史记录
            manager.insertNewsHistory(selectedNews.getNewsId(),
                    selectedNews.getTitle(),
                    sf.format(new Date()),
                    selectedNews.getThumbnailUrl(),
                    selectedNews.getNewsUrl());

            String newsUrl = selectedNews.getNewsUrl();

//            // 获取绑定邮件按钮
//            Button sendEmailButton = findViewById(R.id.sendEmailButton);

            // 启动新闻详情页
            Intent intent = new Intent(MainActivity.this, NewsDetailActivity.class);
            intent.putExtra("newsUrl", newsUrl);
            startActivity(intent);
        });
    }

    private void openEmailDialog() {
        // 创建对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
            String email = recipientInput.getText().toString();
            String subject = subjectInput.getText().toString();
//            String message = messageInput.getText().toString();

            // 校验输入信息
            if (email.isEmpty() || subject.isEmpty() ) {
                Toast.makeText(this, "请填写完整信息！", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "邮件已绑定：" + email, Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());

        // 显示对话框
        builder.show();
    }


    // 广播接收器，用于接收新闻更新广播
    private final BroadcastReceiver newsUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String newsData = intent.getStringExtra("news_data"); // 获取传递过来的新闻数据
            try {
                JSONArray newsArray = new JSONArray(newsData); // 将新闻数据解析成JSONArray
                newsAdapter.updateData(newsArray); // 更新适配器中的数据
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "更新新闻失败", Toast.LENGTH_SHORT).show(); // 异常处理
            }
        }
    };

    // 创建菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu); // 加载菜单布局
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            // 点击刷新按钮时重新启动服务刷新新闻数据
            startService(new Intent(this, NewsRefreshService.class));
            return true;
        } else if(item.getItemId() == R.id.action_play) {
            // 点击播放按钮时播放或停止音乐
            if (musicPlayer.isPlaying()) {
                musicPlayer.stopMusic(); // 停止音乐
            } else {
                musicPlayer.playMusic(); // 播放音乐
            }
        } else if(item.getItemId() == R.id.action_history) {
            // 点击历史记录按钮时跳转到历史记录页面
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.action_profile) {
            // 点击个人资料按钮时跳转到个人资料页面
            Intent intent = new Intent(MainActivity.this, ActionMyActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.action_weather) {
            // 点击天气预报按钮，跳转到天气预报页面
            Intent weatherIntent = new Intent(MainActivity.this, WeatherActivity.class);
            startActivity(weatherIntent);
            return true;
        }

        return super.onOptionsItemSelected(item); // 处理其他菜单项
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        musicPlayer.release(); // 释放音乐播放器资源
        unregisterReceiver(newsUpdateReceiver); // 取消注册广播接收器
    }

    // 音乐播放器类
    private class MusicPlayer {
        private MediaPlayer mediaPlayer; // 媒体播放器实例
        private boolean isPlaying = false; // 标记当前音乐是否正在播放

        public MusicPlayer() {
            mediaPlayer = new MediaPlayer(); // 初始化媒体播放器
        }

        public void playMusic() {
            try {
                // 从资产文件中读取音乐文件
                AssetFileDescriptor afd = getAssets().openFd("music.mp3");
                mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                mediaPlayer.prepare(); // 准备播放
                mediaPlayer.start(); // 播放音乐
                isPlaying = true; // 更新播放状态
            } catch (IOException e) {
                e.printStackTrace(); // 错误处理
            }
        }

        public void stopMusic() {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop(); // 停止播放
                mediaPlayer.reset(); // 重置播放器
            }
            isPlaying = false; // 更新播放状态
        }

        public boolean isPlaying() {
            return isPlaying; // 返回当前播放状态
        }

        public void release() {
            if (mediaPlayer != null) {
                mediaPlayer.release(); // 释放播放器资源
            }
        }
    }

    private void sendEmail() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("发送邮件");
        builder.setMessage("请输入邮件内容：");

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("发送", (dialog, which) -> {
            String emailContent = input.getText().toString();

            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("message/rfc822");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"recipient@example.com"});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "动态设置的邮件标题");
            emailIntent.putExtra(Intent.EXTRA_TEXT, emailContent);

            try {
                startActivity(Intent.createChooser(emailIntent, "选择邮件客户端"));
            } catch (Exception e) {
                Toast.makeText(this, "无法启动邮件客户端，请检查是否安装了邮件应用", Toast.LENGTH_LONG).show();
            }
        });

        builder.setNegativeButton("取消", (dialog, which) -> dialog.cancel());

        builder.show();
    }


    private void sendEmail_1() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("发送邮件");

        // 自定义对话框的布局
        View customView = getLayoutInflater().inflate(R.layout.dialog_email, null);
        builder.setView(customView);

        // 获取输入框控件
        EditText recipientInput = customView.findViewById(R.id.recipientInput);
        EditText subjectInput = customView.findViewById(R.id.subjectInput);
//        EditText messageInput = customView.findViewById(R.id.messageInput);

        // 设置按钮
        builder.setPositiveButton("发送", (dialog, which) -> {
            String recipient = recipientInput.getText().toString();
            String subject = subjectInput.getText().toString();
//            String message = messageInput.getText().toString();

            if (recipient.isEmpty() || subject.isEmpty() ) {
                Toast.makeText(this, "请填写所有字段！", Toast.LENGTH_SHORT).show();
            } else {
//                sendEmail(recipient, subject, message);
            }
        });

        builder.setNegativeButton("取消", (dialog, which) -> dialog.cancel());

        builder.show();
    }


}
