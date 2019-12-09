package com.example.demo.controllers;

import com.example.demo.entities.Location;
import com.example.demo.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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
        return locationService.findLocationById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteLocation(@PathVariable("id") long id){
        locationService.deleteLocation(id);
    }
}
