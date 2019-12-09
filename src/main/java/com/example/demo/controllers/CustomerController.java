package com.example.demo.controllers;

import com.example.demo.entities.Customer;
import com.example.demo.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
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

    @GetMapping
    public Page<Customer> listAllCustomers(Pageable pageable) {
        return customerService.listAllCustomers(pageable);
    }

    @ResponseBody
    @GetMapping("/{id}")
    public Customer findCustomerById(@PathVariable("id") Long id) {
        return customerService.findCustomerById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable("id") Long id) {
        customerService.deleteCustomer(id);
    }

}
