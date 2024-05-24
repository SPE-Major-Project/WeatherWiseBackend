package com.example.WeatherWise.controller;

import com.example.WeatherWise.model.Location;
import com.example.WeatherWise.model.User;
import com.example.WeatherWise.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/user")
public class UserController {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        LOGGER.info("Starting registration for user: {}", user.getEmail());
        User newUser = userService.register(user);
        if(newUser.getEmail()!=null)
            LOGGER.info("User registered successfully: {}", user.getEmail());
        return newUser ;
    }

    @PostMapping("/login")
    public User login(@RequestBody User user) {
        LOGGER.info("Login attempt for user: {}", user.getEmail());
        User loggedInUser = userService.login(user);
        if (loggedInUser != null) {
            LOGGER.info("Login successful for user: {}", loggedInUser.getEmail());
        } else {
            LOGGER.warn("Login failed for user: {}", user.getEmail());
        }
        return loggedInUser;
    }

    @PostMapping("/{userId}/add_location")
    public Map<String, String> addLocation(@RequestBody String location, @PathVariable Long userId) {
        LOGGER.info("Adding location for user with ID: {}", userId);
        Map<String, String> result = userService.addLocation(location, userId);
        LOGGER.info("Location added for user with ID: {}", userId);
        return result;
    }

    @GetMapping("/get_locations/{userId}")
    public List<Location> getLocations(@PathVariable Long userId) {
        LOGGER.info("Fetching locations for user with ID: {}", userId);
        List<Location> locations = userService.getLocations(userId);
        LOGGER.info("Fetched {} locations for user with ID: {}", locations.size(), userId);
        return locations;
    }

    @GetMapping("/weather/{lat}/{lon}")
    public ResponseEntity<?> getWeatherData(@PathVariable String lat, @PathVariable String lon) {
        LOGGER.info("Starting weather data fetch for coordinates: lat={}, lon={}", lat, lon);
        String apiKey = "ad46bca0cb15937504da590a8559bbae";
        String apiUrl = "http://api.openweathermap.org/data/2.5/onecall?lat=" + lat + "&lon=" + lon + "&units=metric&exclude=alerts&appid=" + apiKey;
        RestTemplate restTemplate = new RestTemplate();
        try {
            LOGGER.debug("Sending request to OpenWeatherMap API: {}", apiUrl);
            String weatherData = restTemplate.getForObject(apiUrl, String.class);
            LOGGER.info("Successfully fetched weather data for coordinates: lat={}, lon={}", lat, lon);
            LOGGER.debug("Weather data: {}", weatherData);
            return ResponseEntity.ok(weatherData);
        } catch (Exception e) {
            LOGGER.error("Error fetching weather data for coordinates: lat={}, lon={}. Error: {}", lat, lon, e.getMessage());
            return ResponseEntity.status(500).body("Error fetching weather data");
        }
    }

}