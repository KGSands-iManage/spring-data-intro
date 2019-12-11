package com.example.demo.controllers;

import com.example.demo.entities.Booking;
import com.example.demo.entities.Car;
import com.example.demo.exception.IllegalDateException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/api/bookings")
@RequestMapping(value = "/api/bookings")
public class BookingController {

    @Autowired
    BookingService bookingService;

    @PostMapping
    public long createBooking(@RequestBody Booking booking) {
        if (booking.getEndDate().compareTo(booking.getStartDate()) < 0){
            throw new IllegalDateException();
        } else {
            bookingService.saveBooking(booking);
            return booking.getId();
        }
    }

    @GetMapping
    public Page<Booking> listAllBookings(Pageable pageable) {
        return bookingService.listAllBookings(pageable);
    }

    @ResponseBody
    @GetMapping("/{id}")
    public Booking findBookingById(@PathVariable("id") Long id) {
        Booking booking = bookingService.findBookingById(id);
        if (booking == null) {
            throw new NotFoundException();
        } else {
            return booking;
        }
    }

    @DeleteMapping("/{id}")
    public void deleteBooking(@PathVariable("id") Long id) {
        Booking booking = findBookingById(id);
        if (booking == null) {
            throw new NotFoundException();
        } else {
            bookingService.deleteBooking(id);
        }
    }
}
