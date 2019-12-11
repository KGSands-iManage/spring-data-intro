package com.example.demo.repositories;

import com.example.demo.entities.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * See https://docs.spring.io/spring-data/jpa/docs/current/reference/html/
 */
@Repository
public interface LocationRepository extends CrudRepository<Location, Long>, PagingAndSortingRepository<Location, Long> {
    @Override
    Page<Location> findAll(Pageable pageable);

//    @EntityGraph(value = "Location")
    List<Location> findAll();
}
