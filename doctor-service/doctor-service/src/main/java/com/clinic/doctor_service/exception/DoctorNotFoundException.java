package com.clinic.doctor_service.exception;

public class DoctorNotFoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DoctorNotFoundException(Long id) {
		super("Doctor with ID " + id + " not found.");
	}

	public DoctorNotFoundException(String message) {
		super(message);
	}
}
