package com.darshil.weather_service.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

@RestController
@RequestMapping("/api/geocode")
@CrossOrigin(origins = "http://localhost:5173")
public class GeocodingController {

    @Value("${openweathermap.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/{city}")
    public Object getCoordinates(@PathVariable String city) {
        String url = "https://api.openweathermap.org/geo/1.0/direct?q=" + city + "&limit=1&appid=" + apiKey;
        return restTemplate.getForObject(url, Object.class);
    }
}
