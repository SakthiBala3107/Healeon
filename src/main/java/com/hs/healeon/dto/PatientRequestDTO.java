package com.hs.healeon.dto;

import com.hs.healeon.dto.validators.CreatePatientValidationGroup;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class PatientRequestDTO {

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String address;

    @NotNull
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;


    @NotNull(groups = CreatePatientValidationGroup.class,
            message = "Registered date is required")
    @PastOrPresent(groups = CreatePatientValidationGroup.class,
            message = "Registered date cannot be in the future")
    private LocalDate registeredDate;


    //    GETTERS & SETTERS
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDate getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(LocalDate registeredDate) {
        this.registeredDate = registeredDate;
    }
}
