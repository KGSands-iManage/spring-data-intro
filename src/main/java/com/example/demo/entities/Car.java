package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Set;

@javax.persistence.Entity
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String registration;

    private String manufacturer;

    private String model;

    @Enumerated(EnumType.STRING)
    private Transmission transmission;

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

    @JsonBackReference(value="location")
    @ManyToOne(optional = false)
    Location location;

    @JsonManagedReference(value="car")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "car", fetch = FetchType.EAGER)
    Set<Booking> bookings;
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
