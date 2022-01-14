package com.weatherapp.WeatherApplication.Serivce;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class WeatherService{

    private String cityName;
    private String unit;
    private String API_key="7490f9f7fc2c28f0a641a27e5524a02a";
    private static String API ="api.openweathermap.org/data/2.5/weather?q=kolhapur&units=metric&appid=7490f9f7fc2c28f0a641a27e5524a02a";

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public JSONObject getWeather(){
        try{
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request =   HttpRequest.newBuilder().uri(URI.create(API)).build();

            HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

            return new JSONObject(httpResponse.body().toString());
        }catch (IOException | InterruptedException e){
            e.printStackTrace();
        }
        return null;
    }


//    public JSONArray getWeatherArray()  {
//        JSONArray weather = getWeather().getJSONArray("weather");
//        return weather;
//    }
//
//
//    public JSONObject getMain()  {
//        JSONObject main = getWeather().getJSONObject("main");
//        return main;
//    }
//
//
//    public JSONObject getWind() {
//        JSONObject wind = getWeather().getJSONObject("wind");
//        return wind;
//    }
//
//
//    public JSONObject getSystem() {
//        JSONObject sys = getWeather().getJSONObject("sys");
//        return sys;
//    }

}