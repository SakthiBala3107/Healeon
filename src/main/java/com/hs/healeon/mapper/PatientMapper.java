package com.hs.healeon.mapper;


import com.hs.healeon.dto.PatientRequestDTO;
import com.hs.healeon.dto.PatientResponseDTO;
import com.hs.healeon.models.Patient;

import java.time.LocalDate;

public class PatientMapper {


    //    CONVERTING PATIENT ENTITY TO PATIENT DTO
    public static PatientResponseDTO toDTO(Patient patient) {
        PatientResponseDTO patientDTO = new PatientResponseDTO();

        patientDTO.setId(patient.getId().toString());
        patientDTO.setName(patient.getName());
        patientDTO.setEmail(patient.getEmail());
        patientDTO.setAddress(patient.getAddress());
        patientDTO.setDateOfBirth(patient.getDateOfBirth().toString());
//       once set we have to return
        return patientDTO;
    }

//    CONVERT DTO TO ENTITY(patient)

    public static Patient toModel(PatientRequestDTO patientRequestDTO) {
        Patient patient = new Patient();

        patient.setName(patientRequestDTO.getName());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setDateOfBirth(patientRequestDTO.getDateOfBirth());
        patient.setRegisteredDate(patientRequestDTO.getRegisteredDate());

        return patient;
    }


//    end of class
}
