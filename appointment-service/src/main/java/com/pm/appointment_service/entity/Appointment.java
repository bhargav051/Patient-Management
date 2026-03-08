package com.pm.appointment_service.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Appointment {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;

	@NotNull(message = "Patient ID is required")
	@Column(nullable = false)
	private UUID patientId;

	@NotNull(message = "Start time is required")
	@Column(nullable = false)
	@Future(message = "Start time must be in future")
	private LocalDateTime startTime;

	@NotNull(message = "End time is required")
	@Column(nullable = false)
	@Future(message = "End time must be in future")
	private LocalDateTime endTime;

	@NotNull(message = "Reason is required")
	@Size(max = 255, message = "Reason must be less than equal to 255 characters")
	@Column(nullable = false, length = 255)
	private String reason;

	@Version
	@Column(nullable = false)
	private long version;

	public Appointment(UUID patientId, LocalDateTime startTime, LocalDateTime endTime, String reason) {
		super();
		this.patientId = patientId;
		this.startTime = startTime;
		this.endTime = endTime;
		this.reason = reason;
	}

	public Appointment() {
		super();
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getPatientId() {
		return patientId;
	}

	public void setPatientId(UUID patientId) {
		this.patientId = patientId;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

}
