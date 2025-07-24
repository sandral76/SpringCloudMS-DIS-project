package com.clinic.appointment_service.request;

import java.time.LocalDateTime;

public class AppointmentRequest {

	private Long patientId;
	private Long doctorId;
	private LocalDateTime appointmentTime;
	private String reason;

	public AppointmentRequest(Long patientId, Long doctorId, LocalDateTime appointmentTime, String reason) {
		super();
		this.patientId = patientId;
		this.doctorId = doctorId;
		this.appointmentTime = appointmentTime;
		this.reason = reason;
	}

	public AppointmentRequest() {
	}

	public Long getPatientId() {
		return patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	public Long getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Long doctorId) {
		this.doctorId = doctorId;
	}

	public LocalDateTime getAppointmentTime() {
		return appointmentTime;
	}

	public void setAppointmentTime(LocalDateTime appointmentTime) {
		this.appointmentTime = appointmentTime;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
