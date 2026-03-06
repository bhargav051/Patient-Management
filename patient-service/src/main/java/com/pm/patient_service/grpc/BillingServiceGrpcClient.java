package com.pm.patient_service.grpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.pm.patient_service.kafka.KafkaProducer;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

@Service
public class BillingServiceGrpcClient {

	private static final Logger log = LoggerFactory.getLogger(BillingServiceGrpcClient.class);
	// help us create synchronus calls to the billing service
	private final BillingServiceGrpc.BillingServiceBlockingStub blockingStub;
	
	private final KafkaProducer kafkaProducer; 

	public BillingServiceGrpcClient(@Value("${billing.service.address:localhost}") String serverAddress,
			@Value("${billing.service.grpc.port:9001}") int serverPort,
			KafkaProducer kafkaProducer) {
		
		log.info("Connecting to Billing service GRPC service at {}:{}", serverAddress, serverPort);
		
		ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress, serverPort).usePlaintext().build();
		blockingStub = BillingServiceGrpc.newBlockingStub(channel);
		
		this.kafkaProducer = kafkaProducer;
	}
	
	@CircuitBreaker(name = "billingService", fallbackMethod = "billingFallBack")
	@Retry(name = "billingRetry")
	public BillingResponse createBillingAccount(String patientId, String name, String email) {
		BillingRequest request = BillingRequest.newBuilder().setPatientId(patientId).setEmail(email).setName(name)
				.build();

		BillingResponse response = blockingStub.createBillingAccount(request);
		log.info("Received response from Billing Service {}", response);
		return response;
	}
	
	public BillingResponse billingFallBack(String patientId, String name, String email, Throwable t) {
		log.warn("[CIRCUIT BREAKER]: Billing service is unavailable, Triggered " + "fallback: {}", t.getMessage());
		
		// send the patient billing account creation info. to kafka and whenever billing service is up again 
		// then fetch the information form kafka and create the billing account
		kafkaProducer.sendBillingAccountEvent(patientId, name, email);
		
		return BillingResponse.newBuilder()
				.setAccountId("")
				.setStatus("PENDING")
				.build();
	}
}
