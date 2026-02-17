package com.pm.analytics_service.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.google.protobuf.InvalidProtocolBufferException;

import patient.events.PatientEvent;

@Service
public class KafkaConsumer {

	private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

	// topics tells consumer to consume which topic data
	// groupId tells broker who the consumer is
	@KafkaListener(topics = "patient", groupId = "analytics-service")
	public void consumeEvent(@Payload byte[] event,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset) {
		
		log.info("📩 Received message from Kafka | Topic: {}, Partition: {}, Offset: {}", 
	            topic, partition, offset);
		
		try {
			PatientEvent patientEvent = PatientEvent.parseFrom(event);
			// perform any business related to analytics here

			log.info("Parsed Patient Event: [ID={}, Name={}, Email={}]",
                    patientEvent.getPatientId(),
                    patientEvent.getName(),
                    patientEvent.getEmail());
			
		} catch (InvalidProtocolBufferException e) {
			log.error("Error deserializing event {}", e.getMessage());
		}
	}
}
