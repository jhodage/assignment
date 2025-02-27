package com.mycompany.assignment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.assignment.entity.Customer;
import com.mycompany.assignment.service.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerServiceImpl customerServiceImpl;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetCustomerById() throws Exception {
        UUID customerId = UUID.randomUUID();
        Customer customer = new Customer(customerId, "John", "P", "Doe",
                "john.doe@mycompany.com", "9876543210");
        when(customerServiceImpl.findById(customerId)).thenReturn(Optional.of(customer));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/customer/"+customerId.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(customerId.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("john.doe@mycompany.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone").value("9876543210"));
    }

    @Test
    public void testGetAllCustomer() throws Exception {
        List<Customer> customers = new ArrayList<>();
        UUID customerId1 = UUID.randomUUID();
        UUID customerId2 = UUID.randomUUID();
        customers.add(new Customer(customerId1, "John", "P", "Doe",
                "john.doe@mycompany.com", "9876543210"));
        customers.add((new Customer(customerId2, "Peter", "S", "Smith",
                "peter.smith@mycompany.com", "8769762123")));
        when(customerServiceImpl.findAll()).thenReturn(customers);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/customer"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(customerId1.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastName").value("Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(customerId2.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].firstName").value("Peter"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].lastName").value("Smith"));
    }
}