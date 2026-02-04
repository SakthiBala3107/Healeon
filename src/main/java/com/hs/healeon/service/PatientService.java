package com.hs.healeon.service;

import com.hs.healeon.dto.PatientRequestDTO;
import com.hs.healeon.dto.PatientResponseDTO;
import com.hs.healeon.exception.EmailAlreadyExistsException;
import com.hs.healeon.exception.PatientNotFoundException;
import com.hs.healeon.grpc.BillingServiceGrpcClient;
import com.hs.healeon.mapper.PatientMapper;
import com.hs.healeon.models.Patient;
import com.hs.healeon.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final BillingServiceGrpcClient billingServiceGrpcClient;

    // HELPER METHODS
    // CREATE
    private void validateEmailForCreate(String email) {
        if (patientRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }
    }

    // UPDATE
    private void validateEmailForUpdate(String email, UUID id) {
        if (patientRepository.existsByEmailAndIdNot(email, id)) {
            throw new EmailAlreadyExistsException(email);
        }
    }

    // GET ALL PATIENT DETAILS
    public List<PatientResponseDTO> getPatients() {
        List<Patient> patients = patientRepository.findAll();

        return patients.stream().map(PatientMapper::toDTO).toList();
    }

    // CREATE NEW PATIENT
    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {

        // CHECK IF EMAIL ALREADY EXISTS
        validateEmailForCreate(patientRequestDTO.getEmail());

        Patient newPatient = patientRepository.save(PatientMapper.toModel(patientRequestDTO));


//        CREATING BILLING ACCOUNT(SERVICE) PATIENT  VIA GRPC CALL

        billingServiceGrpcClient.createBillingAccount(newPatient.getId().toString(),
                newPatient.getName(),
                newPatient.getEmail());

        return PatientMapper.toDTO(newPatient);
    }

    // UPDATE PATIENT
    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO) {

        Patient patient = patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException(id));

        // CHECK IF EMAIL ALREADY EXISTS
        validateEmailForUpdate(patientRequestDTO.getEmail(), id);

        // UPDATE FIELDS
        patient.setName(patientRequestDTO.getName());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setDateOfBirth(patientRequestDTO.getDateOfBirth());

        Patient updatedPatient = patientRepository.save(patient);

        // CONVERT ENTITY TO DTO
        return PatientMapper.toDTO(updatedPatient);
    }

    //    Delete patient
    public void deletePatient(UUID id) {
        patientRepository.deleteById(id);
    }
}
