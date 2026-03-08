package com.pm.appointment_service.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pm.appointment_service.entity.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID>{
	List<Appointment> findByStartTimeBetween(LocalDateTime from, LocalDateTime to);
}
