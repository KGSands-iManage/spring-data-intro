package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.util.Set;

@javax.persistence.Entity
public class Location {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @JsonView({Views.LocationSummary.class, Views.LocationFull.class, Views.CarFull.class, Views.BookingFull.class})
    private Long id;

    @JsonView({Views.LocationFull.class, Views.LocationSummary.class, Views.CarFull.class, Views.BookingFull.class})
    private String country = "";

    public Long getId() {
        return id;
    }

    public String getCountry() {
        return country;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @JsonView(Views.LocationFull.class)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "location", fetch = FetchType.LAZY)
    private Set<Car> cars;

    public Set<Car> getCars() {
        return cars;
    }
}
