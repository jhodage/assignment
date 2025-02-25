package com.mycompany.assignment.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.UUID;

@Getter
public class UpdateCustomerRequest extends CreateCustomerRequest {
    @NotNull
    private final UUID id;

    public UpdateCustomerRequest(UUID id, String firstName, String middleName,
                                 String lastName, String phone, String email) {
        super(firstName, middleName, lastName, phone, email);
        this.id = id;
    }
}
