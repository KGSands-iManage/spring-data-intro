package com.example.demo.repositories;

import com.example.demo.entities.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CarRepository extends CrudRepository<Car, Long>, PagingAndSortingRepository<Car, Long> {
    @Override
    Page<Car> findAll(Pageable pageable);

    List<Car> findByLocationId(Long locationId);
}
