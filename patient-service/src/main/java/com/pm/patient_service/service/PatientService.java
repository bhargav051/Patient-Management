package com.pm.patient_service.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.pm.patient_service.dto.PagedPatientResponseDTO;
import com.pm.patient_service.dto.PatientRequestDTO;
import com.pm.patient_service.dto.PatientResponseDTO;
import com.pm.patient_service.exception.EmailAlreadyExistsException;
import com.pm.patient_service.exception.PatientNotFoundException;
import com.pm.patient_service.grpc.BillingServiceGrpcClient;
import com.pm.patient_service.kafka.KafkaProducer;
import com.pm.patient_service.mapper.PatientMapper;
import com.pm.patient_service.model.Patient;
import com.pm.patient_service.repository.PatientRepository;

@Service
public class PatientService {

	private static final Logger log = LoggerFactory.getLogger(PatientService.class);

	private PatientRepository patientRepository;
	private final BillingServiceGrpcClient billingServiceGrpcClient;
	private final KafkaProducer kafkaProducer;

	// constructor (dependency injection method)
	public PatientService(PatientRepository patientRepository, BillingServiceGrpcClient billingServiceGrpcClient,
			KafkaProducer kafkaProducer) {
		this.patientRepository = patientRepository;
		this.billingServiceGrpcClient = billingServiceGrpcClient;
		this.kafkaProducer = kafkaProducer;
	}

	// http://localhost:4004/api/patients?page=1&size=10
	public PagedPatientResponseDTO getPatients(int page, int size, String sort, String sortField, String searchValue) {

		Pageable pageable = PageRequest.of(page-1, size,
				sort.equalsIgnoreCase("desc") ?
						Sort.by(sortField).descending() : 
						Sort.by(sortField).ascending());
		
		Page<Patient> patientPage;
		
		if(searchValue == null || searchValue.isBlank()) {
			patientPage = patientRepository.findAll(pageable);
		}else {
			patientPage = patientRepository.findByNameContainingIgnoreCase(searchValue, pageable);
		}
		
		List<PatientResponseDTO> patientResponseDtos = patientPage.getContent()
																.stream()
																.map(PatientMapper::toDTO)
																.toList();
		
		return new PagedPatientResponseDTO(
				patientResponseDtos,
				patientPage.getNumber(),
				patientPage.getSize(),
				patientPage.getTotalPages(),
				(int)patientPage.getTotalElements());
	}

	public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {

		String email = patientRequestDTO.getEmail();

		if (patientRepository.existsByEmail(email)) {
			throw new EmailAlreadyExistsException("A patient already exists with this email: " + email);
		}

		log.info("CREATE PATIENT SERVICE HIT");

		Patient patient = PatientMapper.toModel(patientRequestDTO);
		Patient savedPatient = patientRepository.save(patient);

		log.info("PATIENT SAVED, calling billing gRPC");

		billingServiceGrpcClient.createBillingAccount(savedPatient.getId().toString(), savedPatient.getName(),
				savedPatient.getEmail());

		kafkaProducer.sendEvent(savedPatient);

		log.info("BILLING gRPC CALL DONE");

		return PatientMapper.toDTO(savedPatient);
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
