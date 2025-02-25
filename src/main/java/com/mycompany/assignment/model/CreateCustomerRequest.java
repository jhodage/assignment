package com.mycompany.assignment.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@AllArgsConstructor
public class CreateCustomerRequest {
    @NotNull
    @Size(min = 2, message = "First name should have at least 2 characters")
    private final String firstName;
    private final String middleName;
    @NotNull
    @Size(min = 2, message = "Last name should have at least 2 characters")
    private final String lastName;
    @NotNull
    @Size(min = 10, message = "Phone number should have at least 10 digits")
    private final String phone;
    @NotNull
    @Email
    private final String email;
}
