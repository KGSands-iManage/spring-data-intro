package com.example.demo

import com.example.demo.controllers.LocationController
import com.example.demo.entities.Location
import com.example.demo.repositories.LocationRepository
import com.example.demo.services.LocationService
import com.fasterxml.jackson.databind.ObjectMapper
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put

@ContextConfiguration
@SpringBootTest(classes = DemoApplication.class)
class LocationControllerSpec extends Specification {

    MockMvc mockMvc
    @Autowired LocationController locationController
    @Autowired LocationRepository locationRepository
    @Autowired LocationService locationService
    @Autowired WebApplicationContext context
    ObjectMapper mapper = new ObjectMapper()

    Location l1;
    Location l2;
    String l1JsonString;
    String l2JsonString;

    def setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build()
        l1 = new Location([country:'America'])
        l2 = new Location([country:'Zimbabwe'])
        l1JsonString = mapper.writeValueAsString(l1)
        l2JsonString = mapper.writeValueAsString(l2)
    }

    def "Get by ID"() {
        given: "an entity"
            def e1 = locationRepository.save(new Location())
        when: "controller is called with an id"
            def result = locationController.findLocationById(e1.id)
        then: "result is an entity with an id"
            result.id == e1.id
            result.id > 0
        when: "findAll is called on the repository"
            def resultList = locationRepository.findAll()
        then: "some results"
            !resultList.isEmpty()
        and: "including our earlier id"
            result.id in resultList*.id
            for (i in result) {
                println("-----------------------------------------\nTesting\n" + i.id)
            }
    }

    def "Get by ID - REST"() {
        given: "an entity"
            def e1 = locationRepository.save(new Location()) //.save() is crudRepositroy, which is a parent of the ExampleController.
        when: "controller is called with an id"
            def result = mockMvc.perform(get("/api/locations/$e1.id")).andReturn().response.contentAsString
            def json = new JsonSlurper().parseText(result)
        then: "result is an entity with an id"
            json.id == e1.id
            json.id > 0
    }

    def "Post location"() {
        when: "user is added"
            def result = locationController.createLocation(l1)
        then: "check country matches"
            def check = locationRepository.findById(result)
            check.get().getCountry() == l1.country
    }

    def "Post location - REST"() {
        given: "a user is posted"
            def e1 = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/locations").contentType(MediaType.APPLICATION_JSON).content(l1JsonString))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().response.contentAsString
            def e1id = new JsonSlurper().parseText(e1)
        when: "a get request is called"
            def result = mockMvc.perform(get("/api/locations/$e1id")).andReturn().response.contentAsString
            def json = new JsonSlurper().parseText(result)
        then: "the ID has successfully been posted"
            json.id == e1id
        and: "the country matches"
            json.country == l1.country
    }

    def "Get all locations"() {
        when: "multiple entities"
            //add 1 more location
            Location l3 = new Location([country:'Switzerland'])
            def e1 = locationRepository.save(l1)
            def e2 = locationRepository.save(l2)
            def e3 = locationRepository.save(l3)
        then: "controller is called"
            def result = locationController.listAllLocations(PageRequest.of(1,2, Sort.by('country')))
            ArrayList<String> testSort = new ArrayList<>(Arrays.asList(e1.country,e2.country,e3.country));
            Collections.sort(testSort)
        expect: "country to be equal to the last country alphabetically"
            result.content[0].country == testSort.get(2)
        and: "number of results on the page to be 1"
            result.content.size() == 1
    }

    def "Get all locations - REST"() {
        given: "multiple locations are posted"
            //add 1 more location
            Location l3 = new Location([country:'Switzerland'])
            def e1 = locationRepository.save(l1)
            def e2 = locationRepository.save(l2)
            def e3 = locationRepository.save(l3)
        when: "controller is called"
            def result = mockMvc.perform(get("/api/locations?page=1&size=2&sort=country")).andReturn().response.contentAsString
            def json = new JsonSlurper().parseText(result)
            print(json)
        then: "multiple id matches"
            json.content[0].id == e2.id
        and: "multiple country matches"
            json.content[0].country == e2.country
    }

    def "Delete by ID"() {
        given: "an element is added"
        def e1 = locationRepository.save(l1)
        and:
        def t1 = locationRepository.findById(e1.id)
        t1.isPresent()
        t1.get().getCountry() == l1.country
        when: "controller is called with an id"
        locationController.deleteLocation(e1.id)
        then:
        def result = locationRepository.findById(e1.id)
        !result.isPresent()
    }

    def "Delete by ID - REST"() {
        given: "the ID is present"
            def e1 = locationRepository.save(l1)
            def result1 = mockMvc.perform(get("/api/locations/$e1.id")).andReturn().response.contentAsString
            def json1 = new JsonSlurper().parseText(result1)
            json1.id == e1.id
        and:
            mockMvc.perform(MockMvcRequestBuilders.delete("/api/locations/$e1.id"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            def result2 = mockMvc.perform(get("/api/locations/$e1.id")).andReturn().response.contentAsString
            result2 == null
    }

    def "Delete by ID - Null"() {
       expect: "null entry to return 418"
            mockMvc.perform(MockMvcRequestBuilders.delete("/api/locations/1"))
                .andExpect(MockMvcResultMatchers.status().isIAmATeapot())
    }

    def "Summary of locations - REST"() {
        given: "multiple locations are added"
            def e1 = locationRepository.save(l1)
            def e2 = locationRepository.save(l2)
        when: "a get request is called"
            def result = mockMvc.perform(get("/api/locations/list/summary")).andReturn().response.contentAsString
            def json = new JsonSlurper().parseText(result)
        then: "A list of countries are returned"
            json[0] == l1.country
            json[1] == l2.country
    }

}