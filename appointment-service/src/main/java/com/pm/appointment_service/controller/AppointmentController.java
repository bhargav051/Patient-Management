package com.pm.appointment_service.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pm.appointment_service.dto.AppointmentResponseDTO;
import com.pm.appointment_service.service.AppointmentService;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {
	
	private final AppointmentService appointmentService;
	
	public AppointmentController(AppointmentService appointmentService) {
		this.appointmentService = appointmentService;
	}
	
	@GetMapping
	public List<AppointmentResponseDTO> getAppointmentsByDateRange(
			@RequestParam LocalDateTime from,
			@RequestParam LocalDateTime to){
		return appointmentService.getAppointmentsByDateRange(from, to);
	}
}
