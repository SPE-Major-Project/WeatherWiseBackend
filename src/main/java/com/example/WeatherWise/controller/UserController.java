package com.example.WeatherWise.controller;

import com.example.WeatherWise.model.Location;
import com.example.WeatherWise.model.User;
import com.example.WeatherWise.service.UserService;
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


    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        User newUser = userService.register(user);
        return newUser ;
    }

    @PostMapping("/login")
    public User login(@RequestBody User user) {
        User loggedInUser = userService.login(user);
        return loggedInUser;
    }

    @PostMapping("/{userId}/add_location")
    public Map<String, String> addLocation(@RequestBody String location, @PathVariable Long userId) {
        Map<String, String> result = userService.addLocation(location, userId);
        return result;
    }

    @GetMapping("/get_locations/{userId}")
    public List<Location> getLocations(@PathVariable Long userId) {
        List<Location> locations = userService.getLocations(userId);
        return locations;
    }

    @GetMapping("/weather/{lat}/{lon}")
    public ResponseEntity<?> getWeatherData(@PathVariable String lat, @PathVariable String lon) {
        String apiKey = "ad46bca0cb15937504da590a8559bbae";
        String apiUrl = "http://api.openweathermap.org/data/2.5/onecall?lat=" + lat + "&lon=" + lon + "&units=metric&exclude=alerts&appid=" + apiKey;
        RestTemplate restTemplate = new RestTemplate();
        try {
            String weatherData = restTemplate.getForObject(apiUrl, String.class);
            return ResponseEntity.ok(weatherData);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching weather data");
        }
    }

}
