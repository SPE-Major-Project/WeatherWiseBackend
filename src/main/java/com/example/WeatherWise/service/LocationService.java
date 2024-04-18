package com.example.WeatherWise.service;

import com.example.WeatherWise.model.Location;
import com.example.WeatherWise.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

//    public List<Location> getAllLocations() {
//
//    }

    public Location addLocation(Location location) {
        Location locationFromDb = locationRepository.findByLocationName(location.getLocationName());
        if(locationFromDb != null) {
            return locationFromDb;
        }else{
            return locationRepository.save(location);
        }
    }
}
