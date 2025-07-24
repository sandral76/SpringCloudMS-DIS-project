package com.clinic.billing_service.exception;

public class BillingNotFoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BillingNotFoundException(Long id) {
		super("Billing with ID " + id + " not found.");
	}

	public BillingNotFoundException(String message) {
		super(message);
	}
}
