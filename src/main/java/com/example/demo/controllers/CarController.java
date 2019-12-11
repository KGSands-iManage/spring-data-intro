package com.example.demo.controllers;

import com.example.demo.entities.Car;
import com.example.demo.exception.NotFoundException;
import com.example.demo.services.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/api/cars")
@RequestMapping(value = "/api/cars")
public class CarController {

    @Autowired
    CarService carService;

    @PostMapping
    public long createCar(@RequestBody Car car) {
        carService.saveCar(car);
        return car.getId();
    }

    @GetMapping
    public Page<Car> listAllCars(Pageable pageable) {
        return carService.listAllCars(pageable);
    }

    @ResponseBody
    @GetMapping("/{id}")
    public Car findCarById(@PathVariable("id") Long id) {
        Car car = carService.findCarById(id);
        if (car == null) {
            throw new NotFoundException();
        } else {
            return car;
        }
    }

    @DeleteMapping("/{id}")
    public void deleteCar(@PathVariable("id") Long id) {
        Car car = findCarById(id);
        if (car == null) {
            throw new NotFoundException();
        } else {
            carService.deleteCar(id);
        }
    }

    @GetMapping("list-by-location/{id}")
    public List<Car> ListCarsByLocationId(@PathVariable("id") Long locationId) {
        return carService.listCarsByLocationId(locationId);
    }

}
