package com.example.demo.services;

import com.example.demo.entities.Booking;
import com.example.demo.repositories.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
public class BookingService {

    @Autowired
    BookingRepository bookingRepository;

    public void saveBooking(Booking booking) {
        bookingRepository.save(booking);
    }

    public Page<Booking> listAllBookings(Pageable pageable) {
        return bookingRepository.findAll(pageable);
    }

    public Booking findBookingById(Long id) {
        return bookingRepository.findById(id).orElse(null);
    }

    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }

    public Page<Booking> listBookingsOnDate(Date date, Pageable pageable) {
        return bookingRepository.findBookingsByStartDate(date, pageable);
    }
}
