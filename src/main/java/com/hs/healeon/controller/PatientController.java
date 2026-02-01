package com.hs.healeon.controller;

import com.hs.healeon.dto.PatientRequestDTO;
import com.hs.healeon.dto.PatientResponseDTO;
import com.hs.healeon.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

//

//    controller methods

    //    GET ALL PATIENT DETAILS
    @GetMapping
    public ResponseEntity<List<PatientResponseDTO>> getPatients() {
        return ResponseEntity.status(HttpStatus.OK).body(patientService.getPatients());
    }


    //    Create a new patient
    @PostMapping
    public ResponseEntity<PatientResponseDTO> createPatient(@Valid @RequestBody PatientRequestDTO patientRequestDTO) {
      return  ResponseEntity.status(HttpStatus.CREATED).body(patientService.createPatient(patientRequestDTO));

    }


//end of class
}
