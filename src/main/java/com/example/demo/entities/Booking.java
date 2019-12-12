package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.util.Date;

@javax.persistence.Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({Views.BookingFull.class, Views.CustomerFull.class})
    private Long id;

    @JsonView({Views.BookingFull.class, Views.CustomerFull.class})
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "GMT")
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @JsonView({Views.BookingFull.class, Views.CustomerFull.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "GMT")
    @Temporal(TemporalType.DATE)
    private Date endDate;

    public Long getId() {
        return id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    @JsonView(Views.BookingFull.class)
    @ManyToOne(optional = false)
    private Car car;

    @JsonView(Views.BookingFull.class)
    @ManyToOne(optional = false)
    private Customer customer;

    public Car getCar() {
        return car;
    }

    public Customer getCustomer() {
        return customer;
    }
}
