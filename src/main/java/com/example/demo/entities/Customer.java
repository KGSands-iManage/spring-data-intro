package com.example.demo.entities;

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

//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parent", fetch = FetchType.EAGER)
//    Set<Car.java> children;
}
