package com.darshil.weather_service.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

@Service
public class WeatherService {

    // API key read from environment variable
    @Value("${weather.api.key}")
    private String apiKey;

    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

    public Map<String, Object> getWeather(String city) {
        RestTemplate restTemplate = new RestTemplate();

        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("q", city)
                .queryParam("appid", apiKey)
                .queryParam("units", "metric")
                .toUriString();

        String response = restTemplate.getForObject(url, String.class);
        JSONObject json = new JSONObject(response);

        Map<String, Object> result = new HashMap<>();
        result.put("city", json.getString("name"));
        result.put("country", json.getJSONObject("sys").getString("country"));
        result.put("temperature", json.getJSONObject("main").getDouble("temp"));
        result.put("humidity", json.getJSONObject("main").getInt("humidity"));
        result.put("wind", json.getJSONObject("wind").getDouble("speed"));
        result.put("condition", json.getJSONArray("weather").getJSONObject(0).getString("description"));
        result.put("timezone", json.getInt("timezone"));

        // Convert sunrise/sunset to readable format in city's local time
        int timezoneOffset = json.getInt("timezone"); // in seconds
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        long sunrise = json.getJSONObject("sys").getLong("sunrise") + timezoneOffset;
        long sunset = json.getJSONObject("sys").getLong("sunset") + timezoneOffset;
        long currentTime = (System.currentTimeMillis() / 1000) + timezoneOffset;

        result.put("sunriseTime", sdf.format(new Date(sunrise * 1000)));
        result.put("sunsetTime", sdf.format(new Date(sunset * 1000)));
        result.put("localTime", sdf.format(new Date(currentTime * 1000)));

        return result;
    }
}
