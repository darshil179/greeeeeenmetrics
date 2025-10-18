package com.darshil.weather_service.service;

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

    private static final String API_KEY = "5e5778cd98c2e8bd3bdd545cdc2da10a"; // API Key
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
        result.put("wind", json.getJSONObject("wind").getDouble("speed"));
        result.put("sunrise", json.getJSONObject("sys").getLong("sunrise"));
        result.put("sunset", json.getJSONObject("sys").getLong("sunset"));
        result.put("timezone", json.getInt("timezone"));
        result.put("country", json.getJSONObject("sys").getString("country"));
        result.put("condition", json.getJSONArray("weather").getJSONObject(0).getString("description"));


        // Convert sunrise/sunset to readable format in local timezone
        int timezoneOffset = json.getInt("timezone"); // in seconds
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        long sunrise = json.getJSONObject("sys").getLong("sunrise") + timezoneOffset;
        long sunset = json.getJSONObject("sys").getLong("sunset") + timezoneOffset;
        result.put("sunriseTime", sdf.format(new Date(sunrise * 1000)));
        result.put("sunsetTime", sdf.format(new Date(sunset * 1000)));

        // current local time in the city
        long currentTime = (System.currentTimeMillis() / 1000) + timezoneOffset;
        result.put("localTime", sdf.format(new Date(currentTime * 1000)));

        return result;
    }
}
