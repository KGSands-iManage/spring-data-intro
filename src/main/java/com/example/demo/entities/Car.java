package com.example.demo.entities;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.util.Set;

@javax.persistence.Entity
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({Views.CarFull.class, Views.LocationFull.class, Views.BookingFull.class})
    private Long id;

    @JsonView({Views.CarFull.class, Views.LocationFull.class})
    private String name;

    @JsonView({Views.CarFull.class, Views.LocationFull.class})
    private String registration;

    @JsonView({Views.CarFull.class, Views.LocationFull.class})
    private String manufacturer;

    @JsonView({Views.CarFull.class, Views.LocationFull.class})
    private String model;

    @JsonView({Views.CarFull.class, Views.LocationFull.class})
    @Enumerated(EnumType.STRING)
    private Transmission transmission;

    @JsonView({Views.CarFull.class, Views.LocationFull.class})
    @Enumerated(EnumType.STRING)
    private Category category;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRegistration() {
        return registration;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getModel() {
        return model;
    }

    public Transmission getTransmission() {
        return transmission;
    }

    public Category getCategory() {
        return category;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "car", fetch = FetchType.LAZY)
    private Set<Booking> bookings;

    @JsonView({Views.CarFull.class, Views.BookingFull.class})
    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    private Location location;

    public Set<Booking> getBookings() {
        return bookings;
    }

    public Location getLocation() {
        return location;
    }
}

enum Transmission {
    MANUAL,
    AUTOMATIC
}

enum Category {
    S,
    A,
    B,
    C,
    D
}
