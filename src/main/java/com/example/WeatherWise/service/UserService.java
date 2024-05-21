package com.example.WeatherWise.service;

import com.example.WeatherWise.model.Location;
import com.example.WeatherWise.model.User;
import com.example.WeatherWise.repository.LocationRepository;
import com.example.WeatherWise.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LocationRepository locationRepository;

    public User login(User user) {
        LOGGER.info("Login attempt for user: {}", user.getEmail());
        try {
            User userFromDB = userRepository.findByEmail(user.getEmail());
            if (userFromDB != null && userFromDB.getPassword().equals(user.getPassword())) {
                LOGGER.info("Login successful for user: {}", user.getEmail());
                return userFromDB;
            } else {
                LOGGER.warn("Login failed for user: {}", user.getEmail());
                return null;
            }
        } catch (Exception e) {
            LOGGER.error("Error during login for user: {}. Error: {}", user.getEmail(), e.getMessage());
            return null;
        }
    }

    public User register(User user) {
        LOGGER.info("Starting registration for user: {}", user.getEmail());
        try {
            if (userRepository.existsByEmail(user.getEmail())) {
                LOGGER.warn("User already exists with email: {}", user.getEmail());
                return null;
            }
            User registeredUser = userRepository.save(user);
            LOGGER.info("User registered successfully: {}", user.getEmail());
            return registeredUser;
        } catch (Exception e) {
            LOGGER.error("Error during registration for user: {}. Error: {}", user.getEmail(), e.getMessage());
            return null;
        }
    }

    public Map<String, String> addLocation(String location, Long userId) {
        LOGGER.info("Adding location '{}' for user with ID: {}", location, userId);
        Map<String, String> response = new HashMap<>();
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User Not Found"));
            Location currLocation = locationRepository.findByLocationName(location);
            if (currLocation == null) {
                LOGGER.info("Location '{}' not found in the repository, creating a new one.", location);
                Location newLocation = new Location();
                newLocation.setLocationName(location);
                locationRepository.save(newLocation);
            }
            Location finalLocation = locationRepository.findByLocationName(location);
            user.getLocationList().add(finalLocation);
            userRepository.save(user);
            response.put("message", "Success");
            LOGGER.info("Location '{}' added for user with ID: {}", location, userId);
        } catch (Exception e) {
            LOGGER.error("Error adding location '{}' for user with ID: {}. Error: {}", location, userId, e.getMessage());
            response.put("message", "Failure: " + e.getMessage());
        }
        return response;
    }

    public List<Location> getLocations(Long userId) {
        LOGGER.info("Fetching locations for user with ID: {}", userId);
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User Not Found"));
            List<Location> locations = user.getLocationList();
            LOGGER.info("Fetched {} locations for user with ID: {}", locations.size(), userId);
            return locations;
        } catch (Exception e) {
            LOGGER.error("Error fetching locations for user with ID: {}. Error: {}", userId, e.getMessage());
            return null;
        }
    }
}
