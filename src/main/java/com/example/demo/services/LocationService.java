package com.example.demo.services;

import com.example.demo.entities.Car;
import com.example.demo.entities.Location;
import com.example.demo.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class LocationService {

    @Autowired
    LocationRepository locationRepository;

    public void saveLocation(Location location) {
        locationRepository.save(location);
    }

    public Page<Location> listAllLocations(Pageable pageable){
        return locationRepository.findAll(pageable);
    }

    public Location findLocationById(Long id) {
        return locationRepository.findById(id).orElse(null);
    }

    public void deleteLocation(long id){
        locationRepository.deleteById(id);
    }

    public List<Location> findAll() {
        return locationRepository.findAll();
    }
}
