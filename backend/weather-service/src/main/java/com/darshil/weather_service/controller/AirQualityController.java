package com.darshil.weather_service.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

@RestController
@RequestMapping("/api/airquality")
public class AirQualityController {

    @Value("${openweathermap.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/{lat}/{lon}")
    public Object getAirQuality(@PathVariable double lat, @PathVariable double lon) {
        String url = String.format(
                "https://api.openweathermap.org/data/2.5/air_pollution?lat=%s&lon=%s&appid=%s",
                lat, lon, apiKey
        );
        return restTemplate.getForObject(url, Object.class);
    }
}
