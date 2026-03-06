package com.pm.patient_service.kafka;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.kafka.support.SendResult;

import com.pm.patient_service.model.Patient;

import billing.events.BillingAccountEvent;
import patient.events.PatientEvent;

@Service
public class KafkaProducer {

	private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class);
	private final KafkaTemplate<String, byte[]> kafkaTemplate;

	public KafkaProducer(KafkaTemplate<String, byte[]> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	public void sendEvent(Patient patient) {
		try {
            PatientEvent event = PatientEvent.newBuilder()
                    .setPatientId(patient.getId().toString())
                    .setName(patient.getName())
                    .setEmail(patient.getEmail())
                    .setEventType("PATIENT_CREATED")
                    .build();

            // SYNCHRONOUS send with proper error handling
            SendResult<String, byte[]> result = kafkaTemplate.send("patient", event.toByteArray()).get();
            
            RecordMetadata metadata = result.getRecordMetadata();
            log.info("Message COMMITTED to Kafka | Topic: {}, Partition: {}, Offset: {}", 
                metadata.topic(), 
                metadata.partition(), 
                metadata.offset());
                
        } catch (Exception e) {
            log.error("Failed to send message to Kafka", e);
            throw new RuntimeException("Kafka publish failed", e);
        }
	}
	
	// for circuit breaker
	public void sendBillingAccountEvent(String patientId, String name, String email) {
		try {
			BillingAccountEvent event = BillingAccountEvent.newBuilder()
															.setPatientId(patientId)
															.setName(name)
															.setEmail(email)
															.setEventType("BILLING_ACCOUNT_CREATE_REQUESTED")
															.build();
			
			SendResult<String, byte[]> result = kafkaTemplate.send("billing-account", event.toByteArray()).get();
			RecordMetadata metadata = result.getRecordMetadata();
			log.info("Billing account creation info send to kafka | Topic: {}, Partition: {}, Offset: {}",
					metadata.topic(),
					metadata.partition(),
					metadata.offset());
		} catch (Exception e) {
            log.error("Failed to send message to Kafka", e);
            throw new RuntimeException("Kafka publish failed", e);
        }
	}
}
