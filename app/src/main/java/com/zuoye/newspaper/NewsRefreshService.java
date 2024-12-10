package com.zuoye.newspaper;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NewsRefreshService extends Service {

//    private static final String API_URL = "http://v.juhe.cn/toutiao/index?type=top&key=d268884b9b07c0eb9d6093dc54116018";
    private static final String API_URL = "http://113.44.93.197:5500/demo.json";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(this::fetchNewsData).start();

        return START_STICKY;
    }

    private void fetchNewsData() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(API_URL)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String jsonResponse = response.body().string();
                parseAndUpdateNews(jsonResponse);
            } else {
                showToast("请求失败");
            }
        } catch (IOException e) {
            showToast("网络错误");
        }
    }

    private void parseAndUpdateNews(String jsonResponse) {
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray newsArray = jsonObject.getJSONObject("result").getJSONArray("data");
            // 在主线程更新UI
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    updateNewsList(newsArray);
                }
            });
        } catch (Exception e) {
            showToast("api额度用完或已经更新");
        }
    }

    private void updateNewsList(JSONArray newsArray) {
        Intent intent = new Intent("com.zuoye.newspaper.ACTION_UPDATE_NEWS");
        intent.putExtra("news_data", newsArray.toString());
        sendBroadcast(intent);
    }

    private void showToast(String message) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(NewsRefreshService.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
