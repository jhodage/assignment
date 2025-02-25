package com.mycompany.assignment.controller;

import com.mycompany.assignment.entity.Customer;
import com.mycompany.assignment.model.CreateCustomerRequest;
import com.mycompany.assignment.model.UpdateCustomerRequest;
import com.mycompany.assignment.service.CustomerService;
import com.mycompany.assignment.service.CustomerServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/customer")
public class CustomerController {
    @Value("${email.unique.constraint.name}")
    private String emailUniqueConstraintName;
    @Value("${email.unique.constraint.violation.error}")
    private String emailUniqueConstraintErrorMessage;

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerServiceImpl customerService) {
        this.customerService = customerService;
    }

    /**
     * Enpoint to retrieve all customers
     *
     * @return all customers available in system
     */
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomer() {
        return new ResponseEntity<>(customerService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable UUID id) {
        return customerService.findById(id).map(customer -> new ResponseEntity<>(customer, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<?> createCustomer(@Valid @RequestBody CreateCustomerRequest createCustomerRequest) {
        if (createCustomerRequest == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            Customer savedCustomer = customerService.create(createCustomerRequest);
            return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
        } catch (Exception e) {
            return getFailureResponse(e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(@NotNull @PathVariable UUID id, @Valid @RequestBody UpdateCustomerRequest updateCustomerRequest) {
        if (!id.equals(updateCustomerRequest.getId())) {
            return ResponseEntity.badRequest().build();
        }
        try {
            Customer modifiedCustomer = customerService.update(updateCustomerRequest);
            if (modifiedCustomer != null) {
                return new ResponseEntity<>(modifiedCustomer, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return getFailureResponse(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable UUID id) {
        return customerService.findById(id).map(customer -> {
            customerService.delete(id);
            return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleException(MethodArgumentNotValidException exception) {
        String customException = exception.getBindingResult().getFieldErrors().stream()
                .map(x -> x.getField() +": "+ x.getDefaultMessage())
                .collect(Collectors.joining(","));

        return  ResponseEntity.badRequest().body(customException);
    }

    private ResponseEntity<?> getFailureResponse(Exception e) {
        if (e.getMessage().contains("constraint [customer." + emailUniqueConstraintName + "]")) {
            return ResponseEntity.badRequest().body(emailUniqueConstraintErrorMessage);
        }
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
