package com.pm.appointment_service.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pm.appointment_service.entity.CachedPatient;

public interface CachedPatientRepository extends JpaRepository<CachedPatient, UUID> {

}
