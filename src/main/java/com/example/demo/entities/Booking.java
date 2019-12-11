package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

@javax.persistence.Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;


    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "GMT")
    @Temporal(TemporalType.DATE)
    public Date startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "GMT")
    @Temporal(TemporalType.DATE)
    public Date endDate;

    public Long getId() {
        return id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    @JsonBackReference(value="car")
    @ManyToOne(optional = false)
    Car car;

    @JsonBackReference(value="customer")
    @ManyToOne(optional = false)
    Customer customer;


}
