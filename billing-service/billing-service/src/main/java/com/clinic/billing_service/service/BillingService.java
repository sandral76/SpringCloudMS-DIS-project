package com.clinic.billing_service.service;

import com.clinic.billing_service.client.AppointmentClient;
import com.clinic.billing_service.exception.AppointmentNotFoundException;
import com.clinic.billing_service.exception.BillingNotFoundException;
import com.clinic.billing_service.model.Billing;
import com.clinic.billing_service.repository.BillingRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BillingService {

	@Autowired
	private BillingRepository billingRepository;
	
	@Autowired
	private AppointmentClient appointmentClient;
	
	/**
	 * Returns a list of all billings.
	 */
	public List<Billing> getAllBillings() {
		return billingRepository.findAll();
	}
	
	/**
	 * Finds a billing by ID.
	 */
	public Billing getBillingById(Long id) {
		return billingRepository.findById(id).orElseThrow(() -> new BillingNotFoundException(id));
	}
	
	@CircuitBreaker(name = "appointment-service", fallbackMethod = "fallbackGetAppointment")
    public void checkAppointmentExists(Long appointmentId) {
        appointmentClient.getAppointmentById(appointmentId);
    }

    public void fallbackGetAppointment(Long appointmentId, Throwable t) {
        throw new AppointmentNotFoundException(appointmentId);
    }
	/**
	 * Saves a new billing.
	 */
    public Billing createBilling(Billing billing) {
        try {
            checkAppointmentExists(billing.getAppointmentId());
        } catch (Exception e) {
            throw new AppointmentNotFoundException(billing.getAppointmentId());
        }
        return billingRepository.save(billing);
    }
	
	/**
	 * Updates an existing billing by ID.
	 */
	public Billing updateBilling(Long id, Billing updatedBilling) {
		Billing existingBilling = billingRepository.findById(id).orElseThrow(() -> new BillingNotFoundException(id));
		updateBillingData(existingBilling, updatedBilling);
		return billingRepository.save(existingBilling);
	}
	
	/**
	 * Deletes a billing by ID.
	 */
	public void deleteBilling(Long id) {
		if (!billingRepository.existsById(id)) {
			throw new BillingNotFoundException(id);
		}
		billingRepository.deleteById(id);
	}
	
	private void updateBillingData(Billing existing, Billing updated) {
		existing.setAppointmentId(updated.getAppointmentId());
		existing.setAmount(updated.getAmount());
		existing.setBillingDate(updated.getBillingDate());
		existing.setStatus(updated.getStatus());
	}
	
	public Billing markAsPaid(Long id) {
		Billing billing = billingRepository.findById(id).orElseThrow();
		billing.setStatus("PAID");
		return billingRepository.save(billing);
	}
	
	public Optional<Billing> findById(Long id) {
		return billingRepository.findById(id);
	}
}
