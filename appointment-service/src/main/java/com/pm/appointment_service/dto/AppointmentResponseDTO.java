package com.pm.appointment_service.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class AppointmentResponseDTO {

	private UUID id;
	private UUID patientID;
	private String patientName;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private String reason;
	private Long version;

	public AppointmentResponseDTO() {
		super();
	}

	public AppointmentResponseDTO(UUID id, UUID patientID, String patientName, LocalDateTime startTime,
			LocalDateTime endTime, String reason, Long version) {
		super();
		this.id = id;
		this.patientID = patientID;
		this.patientName = patientName;
		this.startTime = startTime;
		this.endTime = endTime;
		this.reason = reason;
		this.version = version;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getPatientID() {
		return patientID;
	}

	public void setPatientID(UUID patientID) {
		this.patientID = patientID;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
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

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

}
