package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Set;

@javax.persistence.Entity
//@NamedEntityGraph(
//        name = "Location",
//        attributeNodes = {
//                @NamedAttributeNode("id"),
//                @NamedAttributeNode("country")
//        })
public class Location {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

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

    public Set<Car> getCars() {
        return cars;
    }

    @JsonManagedReference(value = "location")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "location", fetch = FetchType.LAZY)
    Set<Car> cars;
}
