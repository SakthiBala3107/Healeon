package com.hs.healeon.service;

import com.hs.healeon.dto.PatientRequestDTO;
import com.hs.healeon.dto.PatientResponseDTO;
import com.hs.healeon.exception.EmailAlreadyExistsException;
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
//                patient-> PatientMapper.toDTO(patient)
                .map(PatientMapper::toDTO)
                .toList();
    }

    //    create new patient entry
    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        boolean isEmailExists = patientRepository.existsByEmail(patientRequestDTO.getEmail());

        if (isEmailExists) {
            throw new EmailAlreadyExistsException(
                    "A patient with this email already exists: " + patientRequestDTO.getEmail()
            );
        }

        Patient newPatient = patientRepository.save(
                PatientMapper.toModel(patientRequestDTO)
        );
        return PatientMapper.toDTO(newPatient);
    }

//
}
