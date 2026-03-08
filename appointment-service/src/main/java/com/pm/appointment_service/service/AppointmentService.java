package com.pm.appointment_service.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.pm.appointment_service.dto.AppointmentResponseDTO;
import com.pm.appointment_service.entity.Appointment;
import com.pm.appointment_service.entity.CachedPatient;
import com.pm.appointment_service.repository.AppointmentRepository;
import com.pm.appointment_service.repository.CachedPatientRepository;

@Service
public class AppointmentService {
	
	private final AppointmentRepository appointmentRepository;
	private final CachedPatientRepository cachedPatientRepository;
	
	public AppointmentService(AppointmentRepository appointmentRepository, CachedPatientRepository cachedPatientRepository) {
		this.appointmentRepository = appointmentRepository;
		this.cachedPatientRepository = cachedPatientRepository;
	}
	
	public List<AppointmentResponseDTO> getAppointmentsByDateRange(LocalDateTime from, LocalDateTime to){
		List<Appointment> appointments =  appointmentRepository.findByStartTimeBetween(from, to);
		return appointments.stream()
							.map(appointment -> {
								AppointmentResponseDTO appointmentResponseDTO = new AppointmentResponseDTO();
								
								appointmentResponseDTO.setId(appointment.getId());
								appointmentResponseDTO.setPatientID(appointment.getPatientId());
								appointmentResponseDTO.setStartTime(appointment.getStartTime());
								appointmentResponseDTO.setEndTime(appointment.getEndTime());
								appointmentResponseDTO.setReason(appointment.getReason());
								appointmentResponseDTO.setVersion(appointment.getVersion());
								
								String patientName = cachedPatientRepository.findById(appointment.getPatientId()).map(patient -> patient.getFullName()).orElse("NA");
								
								appointmentResponseDTO.setPatientName(patientName);
								
								return appointmentResponseDTO;
							}).toList();
	}
}
