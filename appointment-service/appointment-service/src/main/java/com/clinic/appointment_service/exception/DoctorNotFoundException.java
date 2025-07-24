package com.clinic.appointment_service.exception;

public class DoctorNotFoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DoctorNotFoundException(Long id) {
        super("Doctor with ID " + id + " not found.");
    }
}