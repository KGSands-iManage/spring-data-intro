package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

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

    @JsonBackReference
    @ManyToOne(optional = false)
    Location location;

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
