package com.zuoye.newspaper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.BaseAdapter;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NewsAdapter extends BaseAdapter {

    private Context context;
    private List<News> newsList;

    public NewsAdapter(Context context, List<News> newsArray) {
        this.context = context;
        if(newsArray==null) this.newsList = new ArrayList<>();
        else this.newsList =  newsArray;
    }

    @Override
    public int getCount() {
        return newsList.size();
    }

    @Override
    public Object getItem(int position) {
        return newsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.news_item, parent, false);
        }

        try {
            News newsItem = newsList.get(position);
            TextView titleTextView = convertView.findViewById(R.id.newsTitle);
            TextView dateTextView = convertView.findViewById(R.id.newsDate);
            ImageView imageView = convertView.findViewById(R.id.newsImage);

            titleTextView.setText(newsItem.getTitle());
            dateTextView.setText(newsItem.getDate());

            String imageUrl = newsItem.getThumbnailUrl();
            if (!imageUrl.isEmpty()) {
                Glide.with(context).load(imageUrl).into(imageView);
            } else {
                imageView.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }

    public void updateData(JSONArray newNewsArray) throws Exception {
        this.newsList = new ArrayList<>();
        for(int i=0;i<newNewsArray.length();i++){
            JSONObject newsItem = newNewsArray.getJSONObject(i);
            News news = new News(newsItem.getString("uniquekey"),
                    newsItem.getString("title"),
                    newsItem.getString("date"),
                    newsItem.getString("thumbnail_pic_s"),
                    newsItem.getString("url"));
            newsList.add(news);
        }
        Collections.shuffle(newsList);
        newsList = newsList.subList(0,20);
        notifyDataSetChanged();
    }

    public List<News> getNewsList() {
        return newsList;
    }
}