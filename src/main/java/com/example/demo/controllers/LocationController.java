package com.example.demo.controllers;

import com.example.demo.entities.Car;
import com.example.demo.entities.Location;
import com.example.demo.exception.NotFoundException;
import com.example.demo.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController("/api/locations")
@RequestMapping(value = "/api/locations")
public class LocationController {

    @Autowired
    LocationService locationService;

    @PostMapping
    public long createLocation(@RequestBody Location location) {
        locationService.saveLocation(location);
        return location.getId();
    }

    @GetMapping
    public Page<Location> listAllLocations(Pageable pageable) {
        return locationService.listAllLocations(pageable);
    }

    @ResponseBody
    @GetMapping(path = "/{id}")
    public Location findLocationById(@PathVariable("id") Long id){
        Location location = locationService.findLocationById(id);
        if (location == null) {
            throw new NotFoundException();
        } else {
            return locationService.findLocationById(id);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteLocation(@PathVariable("id") long id){
        Location location = locationService.findLocationById(id);
        if (location == null) {
            throw new NotFoundException();
        } else {
            locationService.deleteLocation(id);
        }
    }

//    @GetMapping(path = "/list")
//    public List<Location> listAll(){
//        return locationService.findAll();
//    }

    @GetMapping(path = "/list/summary")
    public List<String> listAllSummary() {
        List<Location> locations = locationService.findAll();
        List<String> locationSummary = new ArrayList<>();
        locations.forEach(location -> locationSummary.add(location.getCountry()));
        return locationSummary;
    }


}
