package com.darshil.weather_service.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.*;

@RestController
@RequestMapping("/api/geocode")
@CrossOrigin(origins = "http://localhost:5173")
public class GeocodingController {

    @Value("${openweathermap.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/{city}")
    public ResponseEntity<?> getCoordinates(@PathVariable String city) {
        try {
            String url = String.format(
                    "https://api.openweathermap.org/geo/1.0/direct?q=%s&limit=1&appid=%s",
                    city, apiKey
            );

            List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);

            if (response == null || response.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "City not found"));
            }

            Map<String, Object> location = response.get(0);
            Map<String, Object> result = Map.of(
                    "name", location.get("name"),
                    "lat", location.get("lat"),
                    "lon", location.get("lon")
            );

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
