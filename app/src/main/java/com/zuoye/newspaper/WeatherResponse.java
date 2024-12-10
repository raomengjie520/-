package com.zuoye.newspaper;

// 响应类用于解析 OpenWeatherMap API 返回的天气数据
public class WeatherResponse {
    private Main main;  // 温度和湿度信息
    private Weather[] weather;  // 天气描述

    // 获取温度和湿度信息
    public Main getMain() {
        return main;
    }

    // 获取天气描述
    public Weather[] getWeather() {
        return weather;
    }

    // 温度和湿度
    public static class Main {
        private float temp;  // 当前温度
        private int humidity;  // 湿度

        public float getTemp() {
            return temp;
        }

        public int getHumidity() {
            return humidity;
        }
    }

    // 天气描述
    public static class Weather {
        private String description;  // 天气描述

        public String getDescription() {
            return description;
        }
    }
}
