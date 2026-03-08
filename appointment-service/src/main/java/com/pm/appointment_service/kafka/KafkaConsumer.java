package com.pm.appointment_service.kafka;

import java.time.Instant;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.google.protobuf.InvalidProtocolBufferException;
import com.pm.appointment_service.entity.CachedPatient;
import com.pm.appointment_service.repository.CachedPatientRepository;

import patient.events.PatientEvent;

@Service
public class KafkaConsumer {
	
	private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);
	
	private final CachedPatientRepository cachedPatientRepository;
	
	public KafkaConsumer(CachedPatientRepository cachedPatientRepository) {
		this.cachedPatientRepository = cachedPatientRepository;
	}

	@KafkaListener(
		topics = {"patient", "patient_updated"},
		groupId = "appointment-service"
	)
	public void consumeEvent(byte[] event) {

		try {

			PatientEvent patientEvent = PatientEvent.parseFrom(event);

			log.info("Patient Event Received: {}" , patientEvent.toString());
			
			CachedPatient cachedPatient = new CachedPatient();
			cachedPatient.setId(UUID.fromString(patientEvent.getPatientId()));
			cachedPatient.setFullName(patientEvent.getName());
			cachedPatient.setEmail(patientEvent.getEmail());	
			cachedPatient.setUpdatedAt(Instant.now());
			
			cachedPatientRepository.save(cachedPatient);

		} 
		catch (InvalidProtocolBufferException e) {
			log.error("Error deserealizing Patient Event : {}", e.getMessage());
		}
		catch (Exception e) {
			log.error("Error Consuming Patient Event : {}", e.getMessage());
		}
	}
}