package com.zuoye.newspaper;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.zuoye.newspaper.db.HistoryManager;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private ListView newsListView;
    private NewsAdapter newsAdapter;

    private HistoryManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newsListView = findViewById(R.id.newsListView);
        manager = new HistoryManager(this);
        List<News> hisList = manager.getAllNewsHistory();
        newsAdapter = new NewsAdapter(this, hisList);
        newsListView.setAdapter(newsAdapter);

        newsListView.setOnItemClickListener((parentView, view, position, id) -> {
            List<News> newsList = newsAdapter.getNewsList();
            News selectedNews = newsList.get(position);
            String newsUrl = selectedNews.getNewsUrl();

            Intent intent = new Intent(HistoryActivity.this, NewsDetailActivity.class);
            intent.putExtra("newsUrl", newsUrl);
            startActivity(intent);
        });
    }
}