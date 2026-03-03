package com.pm.patient_service.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pm.patient_service.model.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {
	boolean existsByEmail(String email);

	Page<Patient> findByNameContainingIgnoreCase(String searchValue, Pageable pageable);
}
