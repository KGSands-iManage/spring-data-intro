package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Set;

@javax.persistence.Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String name = "";

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @JsonManagedReference(value="customer")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer", fetch = FetchType.EAGER)
    Set<Booking> bookings;
}
