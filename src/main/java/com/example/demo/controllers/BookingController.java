package com.example.demo.controllers;

import com.example.demo.entities.Booking;
import com.example.demo.entities.Car;
import com.example.demo.entities.Views;
import com.example.demo.exception.IllegalDateException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.services.BookingService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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

    @JsonView(Views.BookingFull.class)
    @GetMapping
    public Page<Booking> listAllBookings(Pageable pageable) {
        return bookingService.listAllBookings(pageable);
    }

    @JsonView(Views.BookingFull.class)
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

    @JsonView(Views.BookingFull.class)
    @GetMapping("/search-by-day/{startDate}")
    public Page<Booking> listBookingsOnDate(@PathVariable("startDate")String date, Pageable pageable) throws ParseException {
        DateFormat dmt = new SimpleDateFormat("dd-MM-yyyy");
        Date enteredDate =  dmt.parse(date);
        return bookingService.listBookingsOnDate(enteredDate, pageable);
    }
}
