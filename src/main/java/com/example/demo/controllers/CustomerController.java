package com.example.demo.controllers;

import com.example.demo.entities.Customer;
import com.example.demo.entities.Views;
import com.example.demo.exception.NotFoundException;
import com.example.demo.services.CustomerService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController("/api/customers")
@RequestMapping(value = "/api/customers")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @PostMapping
    public long createCustomer(@RequestBody Customer customer) {
        customerService.saveCustomer(customer);
        return customer.getId();
    }

    @JsonView(Views.CustomerFull.class)
    @GetMapping
    public Page<Customer> listAllCustomers(Pageable pageable) {
        return customerService.listAllCustomers(pageable);
    }

    @JsonView(Views.CustomerFull.class)
    @ResponseBody
    @GetMapping("/{id}")
    public Customer findCustomerById(@PathVariable("id") Long id) {
        Customer customer = customerService.findCustomerById(id);
        if (customer == null) {
            throw new NotFoundException();
        } else {
            return customer;
        }
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable("id") Long id) {
        Customer customer = findCustomerById(id);
        if(customer == null) {
            throw new NotFoundException();
        } else {
            customerService.deleteCustomer(id);
        }
    }
}
