package com.zuoye.newspaper;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherActivity extends AppCompatActivity {

    private TextView weatherTextView; // 显示天气信息的 TextView
    private static final String TAG = "WeatherActivity"; // 日志标签

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        weatherTextView = findViewById(R.id.weatherTextView);

        // 获取天气数据并更新UI
        getWeatherData();
    }

    private void getWeatherData_1() {
        // 用异步任务或线程去请求API数据
        new Thread(() -> {
            try {
                String apiKey = "95e4a3c44650dd7cf1a53e9dffd15eaa";  // 你的实际 API 密钥
                String city = "guangzhou";  // 要查询的城市
                String apiUrl = "https://api.weatherapi.com/v1/forecast.json?key=" + apiKey + "&q=" + city + "&days=7&lang=zh";
                Log.d(TAG, "请求的 API URL: " + apiUrl); // 打印请求的 URL

                // 创建 URL 对象并发起连接
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                // 获取 API 返回的响应数据
                InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
                BufferedReader reader = new BufferedReader(inputStreamReader);
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                // 打印响应的内容
                Log.d(TAG, "API 响应数据: " + response.toString());

                // 解析返回的 JSON 数据
                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray forecast = jsonResponse.getJSONObject("forecast").getJSONArray("forecastday");

                StringBuilder weatherInfo = new StringBuilder();

                // 遍历 forecast 数组，获取未来七天的天气
                for (int i = 0; i < forecast.length(); i++) {
                    JSONObject day = forecast.getJSONObject(i);
                    String date = day.getString("date");
                    JSONObject dayInfo = day.getJSONObject("day");
                    double tempMin = dayInfo.getDouble("mintemp_c");  // 最低温度
                    double tempMax = dayInfo.getDouble("maxtemp_c");  // 最高温度
                    String condition = dayInfo.getJSONObject("condition").getString("text");  // 天气描述

                    // 构建显示的天气信息
                    weatherInfo.append(date)
                            .append(": 最低温度 ")
                            .append(tempMin)
                            .append("°C, 最高温度 ")
                            .append(tempMax)
                            .append("°C, ")
                            .append(condition)
                            .append("\n");
                }

                // 更新UI
                runOnUiThread(() -> {
                    weatherTextView.setText(weatherInfo.toString());
                });

            } catch (Exception e) {
                Log.e(TAG, "请求天气数据时发生错误: " + e.getMessage(), e); // 打印错误信息
                runOnUiThread(() -> {
                    Toast.makeText(WeatherActivity.this, "无法加载天气数据: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }


    private void getWeatherData() {
        // 假设这是广州未来七天的天气预报（硬编码）
        StringBuilder weatherInfo = new StringBuilder();

        weatherInfo.append("广州近七日天气预报12月9日-12月15日天气预测：\n\n");

        // 第一天
        // 构建显示的天气信息，适应冬季气候
        weatherInfo.append("周一: 最低温度 15°C, 最高温度 22°C, 多云\n")
                .append("周二: 最低温度 16°C, 最高温度 23°C, 晴\n")
                .append("周三: 最低温度 14°C, 最高温度 21°C, 阴\n")
                .append("周四: 最低温度 15°C, 最高温度 22°C, 多云\n")
                .append("周五: 最低温度 16°C, 最高温度 24°C, 晴\n")
                .append("周六: 最低温度 17°C, 最高温度 25°C, 阴\n")
                .append("周日: 最低温度 16°C, 最高温度 23°C, 晴\n");


        // 更新UI，显示天气预报
        weatherTextView.setText(weatherInfo.toString());
    }


}
