package com.mycompany.assignment.service;

import com.mycompany.assignment.entity.Customer;
import com.mycompany.assignment.model.CreateCustomerRequest;
import com.mycompany.assignment.model.UpdateCustomerRequest;
import com.mycompany.assignment.repository.CustomerRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(@NotNull CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * @return
     */
    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    /**
     * @param createCustomerRequest
     * @return
     */
    @Override
    @Transactional
    public Customer create(CreateCustomerRequest createCustomerRequest) {
        Customer customer = new Customer(null, createCustomerRequest.firstName(), createCustomerRequest.middleName(),
                createCustomerRequest.lastName(), createCustomerRequest.email(), createCustomerRequest.phone());
        return customerRepository.save(customer);
    }

    /**
     * @param updateCustomerRequest
     * @return
     */
    @Override
    @Transactional
    public Customer update(UpdateCustomerRequest updateCustomerRequest) {
        return findById(updateCustomerRequest.id()).map(customer -> {
            customer.setFirstName(updateCustomerRequest.firstName());
            customer.setLastName(updateCustomerRequest.lastName());
            customer.setEmail(updateCustomerRequest.email());
            customer.setMiddleName(updateCustomerRequest.middleName());
            customer.setPhone(updateCustomerRequest.phone());
            return customerRepository.save(customer);
        }).orElse(null);
    }

    /**
     * Deletes an existing customer by its id of type UUID
     */
    @Override
    @Transactional
    public void delete(UUID id) {
        customerRepository.deleteById(id);
    }

    /**
     * Finds a customer by its id of type UUID
     */
    @Override
    public Optional<Customer> findById(UUID id) {
        return customerRepository.findById(id);
    }
}
