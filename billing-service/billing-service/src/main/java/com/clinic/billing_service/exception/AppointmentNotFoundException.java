package com.clinic.billing_service.exception;

public class AppointmentNotFoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AppointmentNotFoundException(String message) {
		super(message);
	}

	public AppointmentNotFoundException(Long id) {
		super("Appointment with ID " + id + " not found.");
	}
}
