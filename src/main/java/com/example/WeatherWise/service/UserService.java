package com.example.WeatherWise.service;

import com.example.WeatherWise.model.Location;
import com.example.WeatherWise.model.User;
import com.example.WeatherWise.repository.LocationRepository;
import com.example.WeatherWise.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LocationRepository locationRepository;

    public User login(User user) {
        try {
            User userFromDB = userRepository.findByEmail(user.getEmail());
            if (userFromDB != null && userFromDB.getPassword().equals(user.getPassword())) {
                return userFromDB;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public User register(User user) {
        try {
            if (userRepository.existsByEmail(user.getEmail())) {
                return null;
            }
            User registeredUser = userRepository.save(user);
            return registeredUser;
        } catch (Exception e) {
            return null;
        }
    }

    public Map<String, String> addLocation(String location, Long userId) {
        Map<String, String> response = new HashMap<>();
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User Not Found"));
            Location currLocation = locationRepository.findByLocationName(location);
            if (currLocation == null) {
                Location newLocation = new Location();
                newLocation.setLocationName(location);
                locationRepository.save(newLocation);
            }
            Location finalLocation = locationRepository.findByLocationName(location);
            user.getLocationList().add(finalLocation);
            userRepository.save(user);
            response.put("message", "Success");
        } catch (Exception e) {
            response.put("message", "Failure: " + e.getMessage());
        }
        return response;
    }

    public List<Location> getLocations(Long userId) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User Not Found"));
            List<Location> locations = user.getLocationList();
            return locations;
        } catch (Exception e) {
            return null;
        }
    }
}
