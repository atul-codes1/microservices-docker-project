package com.pm.patient_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePatientRequest {
    @NotBlank(message = "Name is mandatory")
    @Size(max = 100,message ="Name size should be less than 100 character")
    private String name;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be Valid")
    private String email;

    @NotBlank(message ="Address is required")
    private String address;

    @NotBlank(message = "DOB is mandatory")
    private String dateOfBirth;

}
