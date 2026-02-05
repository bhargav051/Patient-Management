package com.pm.patient_service.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.pm.patient_service.dto.PatientRequestDTO;
import com.pm.patient_service.dto.PatientResponseDTO;
import com.pm.patient_service.exception.EmailAlreadyExistsException;
import com.pm.patient_service.exception.PatientNotFoundException;
import com.pm.patient_service.grpc.BillingServiceGrpcClient;
import com.pm.patient_service.mapper.PatientMapper;
import com.pm.patient_service.model.Patient;
import com.pm.patient_service.repository.PatientRepository;

@Service
public class PatientService {

	private static final Logger log = LoggerFactory.getLogger(PatientService.class);

	private PatientRepository patientRepository;
	private final BillingServiceGrpcClient billingServiceGrpcClient; 

	// constructor (dependency injection method)
	public PatientService(PatientRepository patientRepository, BillingServiceGrpcClient billingServiceGrpcClient) {
		this.patientRepository = patientRepository;
		this.billingServiceGrpcClient = billingServiceGrpcClient;
	}

	public List<PatientResponseDTO> getPatients() {
		List<Patient> patients = patientRepository.findAll();
		List<PatientResponseDTO> patientResponseDTOs = patients.stream().map(patient -> PatientMapper.toDTO(patient))
				.collect(Collectors.toList());
		return patientResponseDTOs;
	}

	public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
		if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
			throw new EmailAlreadyExistsException(
					"A patient already exists with this email:" + patientRequestDTO.getEmail());
		}
		log.info("CREATE PATIENT SERVICE HIT");
		Patient newPatient = patientRepository.save(PatientMapper.toModel(patientRequestDTO));
		log.info("PATIENT SAVED, calling billing grpc");
		billingServiceGrpcClient.createBillingAccount(newPatient.getId().toString(), newPatient.getName(),
				newPatient.getEmail());
		log.info("BILLING GRPC CALL DONE");
		return PatientMapper.toDTO(newPatient);
	}

	public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO) {
		Patient patient = patientRepository.findById(id)
				.orElseThrow(() -> new PatientNotFoundException("patient not found with ID: " + id));

		if (!patientRequestDTO.getEmail().equals(patient.getEmail())
				&& patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
			throw new EmailAlreadyExistsException(
					"A patient already exists with this email:" + patientRequestDTO.getEmail());
		}

		patient.setName(patientRequestDTO.getName());
		patient.setAddress(patientRequestDTO.getAddress());
		patient.setEmail(patientRequestDTO.getEmail());
		patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));

		Patient updatedPatient = patientRepository.save(patient);

		return PatientMapper.toDTO(updatedPatient);
	}

	public void DeletePatient(UUID id) {
		patientRepository.deleteById(id);
	}
}
