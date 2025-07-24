package com.clinic.patient_service.service;

import com.clinic.patient_service.exception.PatientNotFoundException;
import com.clinic.patient_service.model.Patient;
import com.clinic.patient_service.repository.PatientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

	@Autowired
	private PatientRepository patientRepository;

	/**
	 * Returns a list of all patients.
	 */
	public List<Patient> getAllPatients() {
		return patientRepository.findAll();
	}

	/**
	 * Finds a patient by ID.
	 */
	public Patient getPatientById(Long id) {
	    return patientRepository.findById(id)
	            .orElseThrow(() -> new PatientNotFoundException(id));
	}

	/**
	 * Saves a new patient.
	 */
	public Patient createPatient(Patient patient) {
		return patientRepository.save(patient);
	}

	/**
	 * Updates an existing patient by ID.
	 */
	public Patient updatePatient(Long id, Patient updatedPatient) {
		Patient existingPatient = patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException(id));
		updatePatientData(existingPatient, updatedPatient);
		return patientRepository.save(existingPatient);
	}

	/**
	 * Deletes a patient by ID.
	 */
	public void deletePatient(Long id) {
		if (!patientRepository.existsById(id)) {
			throw new PatientNotFoundException(id);
		}
		patientRepository.deleteById(id);
	}
  
	private void updatePatientData(Patient existing, Patient updated) {
		existing.setFirstName(updated.getFirstName());
		existing.setLastName(updated.getLastName());
		existing.setEmail(updated.getEmail());
		existing.setPhone(updated.getPhone());
	}
	
	public Optional<Patient> findById(Long id) {
	    return patientRepository.findById(id);
	}


}
