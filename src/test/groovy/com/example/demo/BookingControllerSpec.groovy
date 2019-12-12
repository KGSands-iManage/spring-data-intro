package com.example.demo

import com.example.demo.controllers.BookingController
import com.example.demo.entities.Booking
import com.example.demo.entities.Car
import com.example.demo.entities.Customer
import com.example.demo.entities.Location
import com.example.demo.exception.IllegalDateException
import com.example.demo.repositories.BookingRepository
import com.example.demo.repositories.CarRepository
import com.example.demo.repositories.CustomerRepository
import com.example.demo.repositories.LocationRepository
import com.example.demo.services.BookingService
import com.fasterxml.jackson.databind.ObjectMapper
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

@ContextConfiguration
@SpringBootTest(classes = DemoApplication.class)
class BookingControllerSpec extends Specification{

    MockMvc mockMvc
    @Autowired BookingController bookingController
    @Autowired BookingRepository bookingRepository
    @Autowired BookingService bookingService
    @Autowired WebApplicationContext context
    @Autowired CarRepository carRepository
    @Autowired CustomerRepository customerRepository
    @Autowired LocationRepository locationRepository
    ObjectMapper mapper = new ObjectMapper()

    Location l1
    Location l2
    Car car1
    Car car2
    Customer cust1
    Customer cust2

    def setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build()
        l1 = new Location([country:"Liberia"])
        l2 = new Location([country:"Mongolia"])
        car1 = new Car(name:'Fast Car', registration:'BA9 4TY', manufacturer:'Ferrari',model:'No Idea', transmission:'AUTOMATIC', category:'A',location:l1)
        car2 = new Car(name:'Slow Car', registration: 'MP9 JS7', manufacturer: "Me", model: 'XD24', transmission: 'MANUAL', category: 'D', location:l2)
        cust1 = new Customer([name:"Jim Bob II"])
        cust2 = new Customer([name:"What Zit Tooya"])
    }

    def "Get by ID - REST"() {
        given: "an entity"
            locationRepository.save(l1)
            carRepository.save(car1)
            customerRepository.save(cust2)
            def b1 = new Booking(startDate:(new Date("January 1, 2020")), endDate:(new Date("January 2, 2020")), car:car1, customer:cust2)
        def e1 = bookingRepository.save(b1) //.save() is crudRepositroy, which is a parent of the ExampleController.
        when: "controller is called with an id"
            def result = mockMvc.perform(get("/api/bookings/$e1.id")).andReturn().response.contentAsString
            print("\n\n\n\n" + result)
            def json = new JsonSlurper().parseText(result)
        then: "result is an entity with an id"
            json.id == e1.id
            json.id > 0
    }

    def "Post booking - REST"() {
        when: "booking is added"
            def l1_insert = locationRepository.save(l1)
            def car1_insert = carRepository.save(car1)
            def cust1_insert = customerRepository.save(cust2)
            def b1 = new Booking(startDate:(new Date("January 1, 2020")), endDate:(new Date("January 2, 2020")), car:car1, customer:cust2)
            def b1JsonString = mapper.writeValueAsString(b1)

            def e1 = mockMvc.perform(MockMvcRequestBuilders
                    .post("/api/bookings").contentType(MediaType.APPLICATION_JSON).content(b1JsonString))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn().response.contentAsString
            def e1id = new JsonSlurper().parseText(e1)
        then: "check dates match"
            def result = mockMvc.perform(get("/api/bookings/$e1id")).andReturn().response.contentAsString
            def json = new JsonSlurper().parseText(result)
            json.id == e1id
        and:
            json.car.location.id == l1_insert.getId()
    }

    def "Get all cars - REST"() {
        given: "multiple cars are posted"
            locationRepository.save(l1)
            locationRepository.save(l2)
            carRepository.save(car1)
            carRepository.save(car2)
            customerRepository.save(cust1)
            customerRepository.save(cust2)
            def b1 = new Booking(startDate:(new Date("January 1, 2020")), endDate:(new Date("January 2, 2020")), car:car1, customer:cust2)
            def b2 = new Booking(startDate:(new Date("December 12, 2019")), endDate:(new Date("December 13, 2019")), car:car1, customer:cust1)
            def b3 = new Booking(startDate:(new Date("October 13, 2020")), endDate:(new Date("October 14, 2020")), car:car2, customer:cust1)
            def e1 = bookingRepository.save(b1)
            def e2 = bookingRepository.save(b2)
            def e3 = bookingRepository.save(b3)
        when: "controller is called"
            def result = mockMvc.perform(get("/api/bookings?page=0&size=2&sort=startDate")).andReturn().response.contentAsString
            def json = new JsonSlurper().parseText(result)
        then: "original id matches 2nd element from 1st page"
            json.content[1].id == e1.id
    }

    def "Delete by ID - REST"() {
        given: "the ID is present"
            locationRepository.save(l1)
            carRepository.save(car1)
            customerRepository.save(cust2)
            def b1 = new Booking(startDate:(new Date("January 1, 2020")), endDate:(new Date("January 2, 2020")), car:car1, customer:cust2)
            def e1 = bookingRepository.save(b1)
            def result1 = mockMvc.perform(get("/api/bookings/$e1.id")).andReturn().response.contentAsString
            def json1 = new JsonSlurper().parseText(result1)
            json1.id == e1.id
        and:
            mockMvc.perform(MockMvcRequestBuilders.delete("/api/bookings/$e1.id"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
            def result2 = mockMvc.perform(get("/api/bookings/$e1.id")).andReturn().response.contentAsString
            result2 == null
    }

    def "Delete by ID - Null - Exception thrown"() {
        expect: "null entry to return 418"
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/bookings/1"))
                .andExpect(MockMvcResultMatchers.status().isIAmATeapot())
    }

    def "End date entered before start date - Exception"() {
        given: "Entity is set up with incorrect dates entered"
            locationRepository.save(l1)
            carRepository.save(car1)
            customerRepository.save(cust2)
            Booking b3 = new Booking(startDate:(new Date("January 1, 2020")), endDate:(new Date("December 31, 2019")), car:car1, customer:cust2)
        when:
            bookingController.createBooking(b3)
        then: "exception thrown"
            thrown IllegalDateException
    }

    def "Update booking - REST"() {
        given: "Existing entity"
            locationRepository.save(l1)
            carRepository.save(car1)
            customerRepository.save(cust2)
            def b1 = new Booking(startDate:(new Date("January 1, 2020")), endDate:(new Date("January 2, 2020")), car:car1, customer:cust2)
            def e1 = bookingRepository.save(b1)
        when: "booking is replaced"
            Booking b3 = new Booking(id:1L,startDate:(new Date("January 1, 2020")), endDate:(new Date("January 13, 2020")), car:car1, customer:cust2)
            def e2 = bookingController.createBooking(b3)
        then: "new booking is there"
            def result = bookingController.findBookingById(e1.id)
            result.id == e2
            result.endDate == b3.endDate
        and:
            e1.id == e2
    }

    def "List of bookings on a day - REST"() {
        given: "bookings added"
            locationRepository.save(l1)
            locationRepository.save(l2)
            carRepository.save(car1)
            carRepository.save(car2)
            customerRepository.save(cust1)
            customerRepository.save(cust2)
            def date = "01-01-2020"
            def b1 = new Booking(startDate:(new Date("January 1, 2020")), endDate:(new Date("January 2, 2020")), car:car2, customer:cust2)
            def b2 = new Booking(startDate:(new Date("January 1, 2020")), endDate:(new Date("December 13, 2020")), car:car1, customer:cust1)
            def e1 = bookingRepository.save(b1)
            def e2 = bookingRepository.save(b2)
        when: "page is called with sorting of location"
            def result = mockMvc.perform(get("/api/bookings/search-by-day/$date?page=0&size=1&sort=car.location.country")).andReturn().response.contentAsString
            def json = new JsonSlurper().parseText(result)
        then: "2nd booking is first on the first page"
            json.content[0].id == e2.id
        and: "country matches"
            json.content[0].car.location.country == l1.country


    }

}