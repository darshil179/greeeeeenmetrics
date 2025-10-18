package com.darshil.weather_service.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@Service
public class WeatherService {

    private static final String API_KEY = "0543df33c76aea2da87366975aa878c8"; // API Key
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

    public Map<String, Object> getWeather(String city) {
        RestTemplate restTemplate = new RestTemplate();

        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("q", city)
                .queryParam("appid", API_KEY)
                .queryParam("units", "metric") // Celsius
                .toUriString();

        String response = restTemplate.getForObject(url, String.class);
        JSONObject json = new JSONObject(response);

        Map<String, Object> result = new HashMap<>();
        result.put("city", json.getString("name"));
        result.put("temperature", json.getJSONObject("main").getDouble("temp"));
        result.put("humidity", json.getJSONObject("main").getInt("humidity"));
        result.put("condition", json.getJSONArray("weather").getJSONObject(0).getString("description"));

        return result;
    }
}