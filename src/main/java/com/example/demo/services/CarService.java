package com.example.demo.services;

import com.example.demo.entities.Car;
import com.example.demo.repositories.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {

    @Autowired
    CarRepository carRepository;

    public void saveCar(Car car) {
        carRepository.save(car);
    }

    public Page<Car> listAllCars(Pageable pageable) {
        return carRepository.findAll(pageable);
    }

    public Car findCarById(Long id) {
        return carRepository.findById(id).orElse(null);
    }

    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }

    public List<Car> listCarsByLocationId(Long id) {
        return carRepository.findByLocationId(id);
    }

}
