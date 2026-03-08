package com.pm.appointment_service.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AppointmentRequestDTO {

	@NotNull(message = "Patient ID is required")
	private UUID patientId;

	@NotNull(message = "Start time is required")
	@Future(message = "Start time must be in future")
	private LocalDateTime startTime;

	@NotNull(message = "End time is required")
	@Future(message = "End time must be in future")
	private LocalDateTime endTime;

	@NotNull(message = "Reason is required")
	@Size(max = 255, message = "Reason must be less than equal to 255 characters")
	private String reason;

	private long version = 0L;

	public AppointmentRequestDTO() {
	}

	public AppointmentRequestDTO(@NotNull(message = "Patient ID is required") UUID patientId,
			@NotNull(message = "Start time is required") @Future(message = "Start time must be in future") LocalDateTime startTime,
			@NotNull(message = "End time is required") @Future(message = "End time must be in future") LocalDateTime endTime,
			@NotNull(message = "Reason is required") @Size(max = 255, message = "Reason must be less than equal to 255 characters") String reason) {
		super();
		this.patientId = patientId;
		this.startTime = startTime;
		this.endTime = endTime;
		this.reason = reason;
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
