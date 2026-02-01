package com.hs.healeon.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
//    ID
    @Id
    @GeneratedValue
    private UUID id;

//    name
    @NotNull
    private String name;

//    email
    @NotNull
    @Email
    @Column(unique = true)
    private String email;

//    Address
    @NotNull
    private String address;

    //Date of Birth
    @NotNull
    private LocalDate dateOfBirth;

//    RegisteredDate
    @NotNull
    private LocalDate registeredDate;


}
