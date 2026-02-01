package com.hs.healeon.repository;

import com.hs.healeon.models.Patient;
import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;



@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {

//    Custom methods

    boolean existsByEmail(String email);
}
