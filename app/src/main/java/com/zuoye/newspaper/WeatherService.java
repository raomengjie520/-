package com.zuoye.newspaper;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

// 定义与 OpenWeatherMap API 交互的接口
public interface WeatherService {

    // 获取天气数据的请求方法
    @GET("data/2.5/weather")  // 这是 OpenWeatherMap 获取天气数据的路径
    Call<WeatherResponse> getWeather(
            @Query("q") String city,  // 城市名称
            @Query("appid") String apiKey,  // OpenWeatherMap API 密钥
            @Query("units") String units  // 温度单位（metric: 摄氏度，imperial: 华氏度）
    );
}
