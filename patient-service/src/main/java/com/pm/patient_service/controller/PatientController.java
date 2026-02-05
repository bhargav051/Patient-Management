package com.pm.patient_service.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pm.patient_service.dto.PatientRequestDTO;
import com.pm.patient_service.dto.PatientResponseDTO;
import com.pm.patient_service.dto.Validators.CreatePatientValidationGroup;
import com.pm.patient_service.service.PatientService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/patients")
@Tag(name = "Patient", description = "API for managing patients")
public class PatientController {
	private final PatientService patientService;
 
	public PatientController(PatientService patientService) {
		this.patientService = patientService;
	}

	@GetMapping
	@Operation(summary = "Get Patients")
	public ResponseEntity<List<PatientResponseDTO>> getPatients() {
		List<PatientResponseDTO> patients = patientService.getPatients();
		return ResponseEntity.ok().body(patients);
	}

	@PostMapping
	@Operation(summary = "Create a new Patients")
	public ResponseEntity<PatientResponseDTO> createPatient(
			@Validated({jakarta.validation.groups.Default.class,
				CreatePatientValidationGroup.class}) 
			@RequestBody PatientRequestDTO patientRequestDTO) {
		PatientResponseDTO patientResponseDTO = patientService.createPatient(patientRequestDTO);
		return ResponseEntity.ok().body(patientResponseDTO);
	}
	
	@PutMapping("/{id}")
	@Operation(summary = "Update the patient")
	public ResponseEntity<PatientResponseDTO> updatePatient(@PathVariable UUID id,
			@Valid @RequestBody PatientRequestDTO patientRequestDTO){
		PatientResponseDTO patientResponseDTO = patientService.updatePatient(id, patientRequestDTO);
		return ResponseEntity.ok().body(patientResponseDTO);
	}
	
	@DeleteMapping("/{id}")
	@Operation(summary = "Delete the Patient")
	public ResponseEntity<Void> deletePatient(@PathVariable UUID id) {
		patientService.DeletePatient(id);
		return ResponseEntity.noContent().build();
	}
}
