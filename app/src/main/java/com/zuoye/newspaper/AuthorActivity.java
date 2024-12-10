package com.zuoye.newspaper;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;

public class AuthorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author); // 设置布局文件

        // 找到 TextView 用于显示作者介绍
        TextView authorInfoTextView = findViewById(R.id.author_info_text);

        // 找到头像
        ImageView authorAvatarImageView = findViewById(R.id.author_avatar);

        // 在这里设置作者介绍的内容
        String authorInfo = "作者介绍：\n\n" +
                "这是一位有多年经验的作者，专注于写作关于新闻、\n" +
                "技术和软件开发方面的内容。通过深入分析，\n" +
                "为读者提供有价值的观点和知识。\n\n"+
                 "张鹏\n" +
                "学号：2022036143310\n" +
                "22计科1班\n" +
                "安装软件开发大作业";

        // 设置内容
        authorInfoTextView.setText(authorInfo);

        // 可以根据需要设置头像图片
        authorAvatarImageView.setImageResource(R.drawable.ic_author_avatar); // 设置头像
    }
}
