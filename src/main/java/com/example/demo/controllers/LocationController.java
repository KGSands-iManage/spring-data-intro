package com.example.demo.controllers;

import com.example.demo.entities.Location;
import com.example.demo.entities.Views;
import com.example.demo.exception.NotFoundException;
import com.example.demo.services.LocationService;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/api/locations")
@RequestMapping(value = "/api/locations")
public class LocationController {

    @Autowired
    LocationService locationService;

    @PostMapping
    public long createLocation(@RequestBody Location location){
        locationService.saveLocation(location);
        return location.getId();
    }

    @JsonView(Views.LocationSummary.class)
    @GetMapping
    public Page<Location> listAllLocations(Pageable pageable) {
        return locationService.listAllLocations(pageable);
    }

    @JsonView(Views.LocationFull.class)
    @ResponseBody
    @GetMapping(path = "/{id}")
    public Location findLocationById(@PathVariable("id") Long id){
        Location location = locationService.findLocationById(id);
        if (location == null) {
            throw new NotFoundException();
        } else {
            return location;
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

    @JsonView(Views.LocationSummary.class)
    @GetMapping(path = "/list/summary")
    public List<Location> listAllSummary() {
        return locationService.findAll();
    }
}
