package com.hs.healeon.service;

import com.hs.healeon.dto.PatientRequestDTO;
import com.hs.healeon.dto.PatientResponseDTO;
import com.hs.healeon.exception.EmailAlreadyExistsException;
import com.hs.healeon.exception.PatientNotFoundException;
import com.hs.healeon.grpc.BillingServiceGrpcClient;
import com.hs.healeon.kafka.KafkaProducer;
import com.hs.healeon.mapper.PatientMapper;
import com.hs.healeon.models.Patient;
import com.hs.healeon.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j  // ✅ Lombok generates 'log' automatically
public class PatientService {

    private final PatientRepository patientRepository;
    private final BillingServiceGrpcClient billingServiceGrpcClient;
    private final KafkaProducer kafkaProducer;

    // --------------------
    // HELPER METHODS
    private void validateEmailForCreate(String email) {
        if (patientRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }
    }

    private void validateEmailForUpdate(String email, UUID id) {
        if (patientRepository.existsByEmailAndIdNot(email, id)) {
            throw new EmailAlreadyExistsException(email);
        }
    }

    // --------------------
    // GET ALL PATIENT DETAILS
    public List<PatientResponseDTO> getPatients() {
        List<Patient> patients = patientRepository.findAll();
        return patients.stream().map(PatientMapper::toDTO).toList();
    }

    // --------------------
    // CREATE NEW PATIENT
    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {

        // 1️⃣ Check if email already exists
        validateEmailForCreate(patientRequestDTO.getEmail());

        // 2️⃣ Save patient to DB
        Patient newPatient = patientRepository.save(
                PatientMapper.toModel(patientRequestDTO)
        );

        // 3️⃣ Create billing account via gRPC
        billingServiceGrpcClient.createBillingAccount(
                newPatient.getId().toString(),
                newPatient.getName(),
                newPatient.getEmail()
        );

        // 4️⃣ Log before sending to Kafka
        log.info("Sending patient details to Kafka: id={}, email={}, name={}",
                newPatient.getId(),
                newPatient.getEmail(),
                newPatient.getName()
        );

        // 5️⃣ Send patient-created event to Kafka
        kafkaProducer.sendEvent(newPatient);

        // 6️⃣ Log after sending
        log.info("Patient details successfully sent to Kafka for id={}",
                newPatient.getId()
        );

        // 7️⃣ Return response DTO
        return PatientMapper.toDTO(newPatient);
    }

    // --------------------
    // UPDATE PATIENT
    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO) {

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException(id));

        validateEmailForUpdate(patientRequestDTO.getEmail(), id);

        patient.setName(patientRequestDTO.getName());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setDateOfBirth(patientRequestDTO.getDateOfBirth());

        Patient updatedPatient = patientRepository.save(patient);

        return PatientMapper.toDTO(updatedPatient);
    }

    // --------------------
    // DELETE PATIENT
    public void deletePatient(UUID id) {
        patientRepository.deleteById(id);
    }
}
