package com.example.demo

import com.example.demo.controllers.CustomerController
import com.example.demo.entities.Customer
import com.example.demo.repositories.CustomerRepository
import com.example.demo.services.CustomerService
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

@ContextConfiguration
@SpringBootTest(classes = DemoApplication.class)
class CustomerControllerSpec extends Specification{


    MockMvc mockMvc
    @Autowired CustomerController customerController
    @Autowired CustomerRepository customerRepository
    @Autowired CustomerService customerService
    @Autowired WebApplicationContext context
    ObjectMapper mapper = new ObjectMapper()

    Customer c1;
    Customer c2;
    String c1JsonString;
    String c2JsonString;

    def setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build()
        c1 = new Customer([name:'John Smith'])
        c2 = new Customer([name:'Mo Salah'])
        c1JsonString = mapper.writeValueAsString(c1)
        c2JsonString = mapper.writeValueAsString(c2)
    }

    def "Get by ID"() {
        given: "an entity"
        def e1 = customerRepository.save(new Customer())
        when: "controller is called with an id"
        def result = customerController.findCustomerById(e1.id)
        then: "result is an entity with an id"
        result.id == e1.id
        result.id > 0
        when: "findAll is called on the repository"
        def resultList = customerRepository.findAll()
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
        def e1 = customerRepository.save(new Customer()) //.save() is crudRepositroy, which is a parent of the ExampleController.
        when: "controller is called with an id"
        def result = mockMvc.perform(get("/api/customers/$e1.id")).andReturn().response.contentAsString
        def json = new JsonSlurper().parseText(result)
        then: "result is an entity with an id"
        json.id == e1.id
        json.id > 0
    }

    def "Post customer"() {
        when: "user is added"
        def result = customerController.createCustomer(c1)
        then: "check name matches"
        def check = customerRepository.findById(result)
        check.get().getName() == c1.name
    }

    def "Post customer - REST"() {
        given: "a user is posted"
        def e1 = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/customers").contentType(MediaType.APPLICATION_JSON).content(c1JsonString))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().response.contentAsString
        def e1id = new JsonSlurper().parseText(e1)
        when: "a get request is called"
        def result = mockMvc.perform(get("/api/customers/$e1id")).andReturn().response.contentAsString
        def json = new JsonSlurper().parseText(result)
        then: "the ID has successfully been posted"
        json.id == e1id
        and: "the name matches"
        json.name == c1.name
    }

    def "Get all customers"() {
        when: "multiple entities"
        //add 1 more customer
        Customer c3 = new Customer([name:'Microsoft Sam'])
        def e1 = customerRepository.save(c1)
        def e2 = customerRepository.save(c2)
        def e3 = customerRepository.save(c3)
        then: "controller is called"
        def result = customerController.listAllCustomers(PageRequest.of(1,2, Sort.by('name')))
        ArrayList<String> testSort = new ArrayList<>(Arrays.asList(e1.name,e2.name,e3.name));
        Collections.sort(testSort)
        expect: "name to be equal to the last name alphabetically"
        result.content[0].name == testSort.get(2)
        and: "number of results on the page to be 1"
        result.content.size() == 1
    }

    def "Get all customers - REST"() {
        given: "multiple customers are posted"
        //add 1 more customer
        Customer c3 = new Customer([name:'Microsoft Sam'])
        def e1 = customerRepository.save(c1)
        def e2 = customerRepository.save(c2)
        def e3 = customerRepository.save(c3)
        when: "controller is called"
        def result = mockMvc.perform(get("/api/customers?page=1&size=2&sort=name")).andReturn().response.contentAsString
        def json = new JsonSlurper().parseText(result)
        print(json)
        then: "multiple id matches"
        json.content[0].id == e2.id
        and: "multiple name matches"
        json.content[0].name == e2.name
    }

    def "Delete by ID"() {
        given: "an element is added"
        def e1 = customerRepository.save(c1)
        and:
        def t1 = customerRepository.findById(e1.id)
        t1.isPresent()
        t1.get().getName() == c1.name
        when: "controller is called with an id"
        customerController.deleteCustomer(e1.id)
        then:
        def result = customerRepository.findById(e1.id)
        !result.isPresent()
    }

    def "Delete by ID - REST"() {
        given: "the ID is present"
        def e1 = customerRepository.save(c1)
        def result1 = mockMvc.perform(get("/api/customers/$e1.id")).andReturn().response.contentAsString
        def json1 = new JsonSlurper().parseText(result1)
        json1.id == e1.id
        and:
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/customers/$e1.id"))
                .andExpect(MockMvcResultMatchers.status().isOk())
        def result2 = mockMvc.perform(get("/api/customers/$e1.id")).andReturn().response.contentAsString
        result2 == null
    }

    def "Delete by ID - Null"() {
        expect: "null entry to return 418"
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/locations/1"))
                .andExpect(MockMvcResultMatchers.status().isIAmATeapot())
    }

}
