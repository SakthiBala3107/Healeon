package com.hs.healeon.service;

import com.hs.healeon.dto.PatientResponseDTO;
import com.hs.healeon.mapper.PatientMapper;
import com.hs.healeon.models.Patient;
import com.hs.healeon.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {


    private final PatientRepository patientRepository;

//logics

//    RETURN ALL THE DETAILS OF THE PATIENTS(DTO)
    public List<PatientResponseDTO> getPatients() {
        List<Patient> patients = patientRepository.findAll();

        return patients.stream()
                .map(PatientMapper::toDTO)
                .toList();
    }

//
}
