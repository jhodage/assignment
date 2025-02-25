package com.mycompany.assignment.controller;

import com.mycompany.assignment.entity.Customer;
import com.mycompany.assignment.model.CreateCustomerRequest;
import com.mycompany.assignment.model.UpdateCustomerRequest;
import com.mycompany.assignment.service.CustomerService;
import com.mycompany.assignment.service.CustomerServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/customer")
public class CustomerController {
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
    @Operation(summary = "${swagger-ui.getAll.operation.summary}", description = "${swagger-ui.getAll.operation.description}")
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomer() {
        return new ResponseEntity<>(customerService.findAll(), HttpStatus.OK);
    }

    @Operation(summary = "${swagger-ui.getById.operation.summary}", description = "${swagger-ui.getById.operation.description}")
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@Parameter(description = "Id of customer to be searched") @PathVariable UUID id) {
        return customerService.findById(id).map(customer -> new ResponseEntity<>(customer, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "${swagger-ui.create.operation.summary}", description = "${swagger-ui.create.operation.description}")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Customer created successfully.", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Customer.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content),
            @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid customer details", content = @Content)})
    @PostMapping
    public ResponseEntity<?> createCustomer(@Valid
                                            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                    description = "Details of customer to create", required = true,
                                                    content = @Content(mediaType = "application/json",
                                                            schema = @Schema(implementation = CreateCustomerRequest.class)))
                                            @RequestBody CreateCustomerRequest createCustomerRequest) {
        if (createCustomerRequest == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Customer savedCustomer = customerService.create(createCustomerRequest);
        return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
    }

    @Operation(summary = "${swagger-ui.update.operation.summary}", description = "${swagger-ui.update.operation.description}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer modified successfully.", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Customer.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content),
            @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid customer details", content = @Content)})
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(@Parameter(description = "Id of customer to be modified") @NotNull @PathVariable UUID id,
                                            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                    description = "Details of customer to modify", required = true,
                                                    content = @Content(mediaType = "application/json",
                                                            schema = @Schema(implementation = UpdateCustomerRequest.class)))
                                            @Valid @RequestBody UpdateCustomerRequest updateCustomerRequest) {
        if (!id.equals(updateCustomerRequest.id())) {
            return ResponseEntity.badRequest().build();
        }
        Customer modifiedCustomer = customerService.update(updateCustomerRequest);
        if (modifiedCustomer != null) {
            return new ResponseEntity<>(modifiedCustomer, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "${swagger-ui.delete.operation.summary}", description = "${swagger-ui.delete.operation.description}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer deleted successfully.", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content),
            @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid customer id", content = @Content)})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@NotNull @PathVariable UUID id) {
        return customerService.findById(id).map(customer -> {
            customerService.delete(id);
            return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
