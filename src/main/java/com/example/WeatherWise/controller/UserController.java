package com.example.WeatherWise.controller;

import com.example.WeatherWise.model.Location;
import com.example.WeatherWise.model.User;
import com.example.WeatherWise.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import org.springframework.web.client.RestTemplate;
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.register(user);
    }

    @PostMapping("/login")
    public User login(@RequestBody User user) {
        return userService.login(user);
    }

    @PostMapping("/{userId}/add_location")
    public Map<String,String> addLocation(@RequestBody String location, @PathVariable Long userId) {
        return userService.addLocation(location,userId);
    }

    @GetMapping("/get_locations/{userId}")
    public List<Location> getLocations(@PathVariable Long userId) {
        return userService.getLocations(userId);
    }

    @GetMapping("/weather/{lat}/{lon}")
    public ResponseEntity<?> getWeatherData(@PathVariable String lat, @PathVariable String lon) {
        String apiKey = "ad46bca0cb15937504da590a8559bbae";
        String apiUrl = "http://api.openweathermap.org/data/2.5/onecall?lat=" + lat + "&lon=" + lon + "&units=metric&exclude=alerts&appid=" + apiKey;
        RestTemplate restTemplate = new RestTemplate();
        return ResponseEntity.ok( restTemplate.getForObject(apiUrl, String.class));
    }
}
