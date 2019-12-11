package com.example.demo

import com.example.demo.controllers.CarController
import com.example.demo.entities.Car
import com.example.demo.entities.Location
import com.example.demo.repositories.CarRepository
import com.example.demo.repositories.LocationRepository
import com.example.demo.services.CarService
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
class CarControllerSpec extends Specification{

    MockMvc mockMvc
    @Autowired CarController carController
    @Autowired CarRepository carRepository
    @Autowired CarService carService
    @Autowired WebApplicationContext context
    @Autowired LocationRepository locationRepository
    ObjectMapper mapper = new ObjectMapper()

    Location l1;
    Location l2;
    String l1JsonString;
    String l2JsonString;
    Car c1;
    Car c2;
    String c1JsonString;
    String c2JsonString;

    def setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build()
        l1 = new Location([country:'Lithuania'])
        l2 = new Location([country:'Egypt'])
        l1JsonString = mapper.writeValueAsString(l1)
        l2JsonString = mapper.writeValueAsString(l2)
        c1 = new Car(name:'Fast Car', registration:'BA9 4TY', manufacturer:'Ferrari',model:'No Idea', transmission:'AUTOMATIC', category:'A',location:l1)
        c2 = new Car(name:'Slow Car', registration: 'MP9 JS7', manufacturer: "Me", model: 'XD24', transmission: 'MANUAL', category: 'D', location:l1)
        c1JsonString = mapper.writeValueAsString(c1)
        c2JsonString = mapper.writeValueAsString(c2)
    }

    def "Get by ID - REST"() {
        given: "an entity (location saved as it is a required field in Car)"
            def l1_save = locationRepository.save(l1)
           def e1 = carRepository.save(c1) //.save() is crudRepositroy, which is a parent of the ExampleController.
        when: "controller is called with an id"
            def result = mockMvc.perform(get("/api/cars/$e1.id")).andReturn().response.contentAsString
            def json = new JsonSlurper().parseText(result)
        then: "result is an entity with an id"
            json.id == e1.id
            json.id > 0
    }

    def "Get all cars - REST"() {
        given: "multiple cars are posted (and their relevant locations)"
            def l1_save = locationRepository.save(l1)
            def l2_save = locationRepository.save(l2)
            Car c3 = new Car([name:'Super Fast Car', registration:'XDD 900', manufacturer:'Irarref', model:'Onifotrop', transmission:'MANUAL', category:'S', location:l2])
            def e1 = carRepository.save(c1)
            def e2 = carRepository.save(c2)
            def e3 = carRepository.save(c3)
        when: "controller is called"
            def result = mockMvc.perform(get("/api/cars?page=0&size=2&sort=name")).andReturn().response.contentAsString
            def json = new JsonSlurper().parseText(result)
        then: "multiple id matches"
            json.content[1].id == e2.id
        and: "multiple name matches"
            json.content[1].name == e2.name
    }

    def "Post car"() {
        when: "car is added (with loc)"
        def l1_post = locationRepository.save(l1)
        def result = carController.createCar(c1)
        then: "check name matches"
        def check = carRepository.findById(result)
        check.get().getName() == c1.name
    }

    def "Delete by ID - REST"() {
        given: "the ID is present"
            def l1_post = locationRepository.save(l1)
            def e1 = carRepository.save(c1)
            def result1 = mockMvc.perform(get("/api/cars/$e1.id")).andReturn().response.contentAsString
            def json1 = new JsonSlurper().parseText(result1)
            json1.id == e1.id
        and:
            mockMvc.perform(MockMvcRequestBuilders.delete("/api/cars/$e1.id"))
                 .andExpect(MockMvcResultMatchers.status().isOk())
            def result2 = mockMvc.perform(get("/api/cars/$e1.id")).andReturn().response.contentAsString
            result2 == null
    }

    def "Delete by ID - Null"() {
        expect: "null entry to return 418"
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/cars/1"))
                .andExpect(MockMvcResultMatchers.status().isIAmATeapot())
    }

    def "List all cars at location by ID - REST"() {
        given: "location and 2 cars present"
            def l1_post = locationRepository.save(l1)
            def e1 = carRepository.save(c1)
            def e2 = carRepository.save(c2)
        when: "call is invoked with l1's ID"
            def result = mockMvc.perform(get("/api/cars/list-by-location/$l1_post.id")).andReturn().response.contentAsString
            def json = new JsonSlurper().parseText(result)
        then: "Cars to be returned"
            json[0].id == e1.id
            json[1].id == e2.id
    }

}
