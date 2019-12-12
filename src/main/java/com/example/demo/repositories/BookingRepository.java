package com.example.demo.repositories;

import com.example.demo.entities.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;

public interface BookingRepository extends CrudRepository<Booking, Long>, PagingAndSortingRepository<Booking, Long> {
    @Override
    Page<Booking> findAll(Pageable pageable);

    Page<Booking> findBookingsByStartDate(Date date, Pageable pageable);
}
