package com.hs.healeon.repository;

import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;



@Repository
public interface PatientRepository extends JpaRepository<UUID, Id> {
}
