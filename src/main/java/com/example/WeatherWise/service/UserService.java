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

    @Autowired
    private LocationService locationService;

    public User login(User user) {
        User userFromDB = userRepository.findByEmail(user.getEmail());
        if(userFromDB != null && userFromDB.getPassword().equals(user.getPassword())) {
            return userFromDB;
        }else {
            return null;
        }
    }

    public User register(User user) {
        return userRepository.save(user);
    }

    public Map<String,String> addLocation(String location, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User Not Found"));
        Location currLocation = locationRepository.findByLocationName(location);
        if(currLocation==null){
            Location newLocation = new Location();
            newLocation.setLocationName(location);
            locationRepository.save(newLocation);
        }
        Location finalLocation = locationRepository.findByLocationName(location);
        user.getLocationList().add(finalLocation);
        userRepository.save(user);
        Map<String,String> response = new HashMap<>();
        response.put("message","Success");
        return response;
    }

    public List<Location> getLocations(Long userId) {
        User user =  userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User Not Found"));
        return user.getLocationList();
    }
}
