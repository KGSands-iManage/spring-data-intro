package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.util.Set;

@javax.persistence.Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({Views.CustomerFull.class, Views.BookingFull.class})
    private Long id;

    @JsonView({Views.CustomerFull.class, Views.BookingFull.class})
    private String name = "";

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @JsonView(Views.CustomerFull.class)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer", fetch = FetchType.EAGER)
    private Set<Booking> bookings;

    public Set<Booking> getBookings() {
        return bookings;
    }
}
