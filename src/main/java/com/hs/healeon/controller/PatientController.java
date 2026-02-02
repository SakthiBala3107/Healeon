package com.hs.healeon.controller;

import com.hs.healeon.dto.PatientRequestDTO;
import com.hs.healeon.dto.PatientResponseDTO;
import com.hs.healeon.dto.validators.CreatePatientValidationGroup;
import com.hs.healeon.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
@Tag(name="Patient", description = "API for managing Patients")
public class PatientController {

    private final PatientService patientService;

//

//    controller methods

    //    GET ALL PATIENT DETAILS
    @GetMapping
    @Operation(summary = "Get Patients")
    public ResponseEntity<List<PatientResponseDTO>> getPatients() {
        return ResponseEntity.status(HttpStatus.OK).body(patientService.getPatients());
    }


    //    Create a new patient
    @PostMapping
    @Operation(summary = "Create  a new patient")
    public ResponseEntity<PatientResponseDTO> createPatient(@Validated({Default.class, CreatePatientValidationGroup.class}) @RequestBody PatientRequestDTO
                                                                    patientRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(patientService.createPatient(patientRequestDTO));

    }


    //    Update patient details
    @PutMapping("/{id}")
    @Operation(summary = "Update Patient details")
    public ResponseEntity<PatientResponseDTO> updatePatient(@PathVariable UUID id,
                                                            @Validated({Default.class}) @RequestBody PatientRequestDTO patientRequestDTO) {

        PatientResponseDTO patientResponseDTO = patientService.updatePatient(id, patientRequestDTO);
        return ResponseEntity.ok().body(patientResponseDTO);
    }

    //    Delete Patient
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Patient")
    public ResponseEntity<Void> deletePatient(@PathVariable UUID id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }

}
