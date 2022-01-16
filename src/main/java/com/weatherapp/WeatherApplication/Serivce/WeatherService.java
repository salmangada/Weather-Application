package com.weatherapp.WeatherApplication.Serivce;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
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
    private JSONObject weather;

    @Value("${API_KEY}")
    private String API_key;


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

    //Main request method
    public JSONObject getWeather(){
        try{
            HttpClient client = HttpClient.newHttpClient();

            System.out.println(API_key);
            String API_URL ="https://api.openweathermap.org/data/2.5/weather?q="+getCityName()+"&units="+getUnit()+"&appid="+API_key;
            HttpRequest request =   HttpRequest.newBuilder().uri(URI.create(API_URL)).build();

            HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            return new JSONObject(httpResponse.body().toString());

        }catch (IOException | InterruptedException e){
            e.printStackTrace();
        }
        return null;
    }

}
