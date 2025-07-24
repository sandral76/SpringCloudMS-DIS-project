package com.clinic.doctor_service.service;

import com.clinic.doctor_service.exception.DoctorNotFoundException;
import com.clinic.doctor_service.model.Doctor;
import com.clinic.doctor_service.repository.DoctorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

	@Autowired
	private DoctorRepository doctorRepository;

	/**
	 * Returns a list of all doctors.
	 */
	public List<Doctor> getAllDoctors() {
		return doctorRepository.findAll();
	}

	/**
	 * Finds a patient by ID.
	 */
	public Doctor getDoctorById(Long id) {
		return doctorRepository.findById(id).orElseThrow(() -> new DoctorNotFoundException(id));
	}

	/**
	 * Saves a new doctor.
	 */
	public Doctor createDoctor(Doctor doctor) {
		return doctorRepository.save(doctor);
	}

	/**
	 * Updates an existing doctor by ID.
	 */
	public Doctor updateDoctor(Long id, Doctor updatedDoctor) {
		Doctor existingDoctor = doctorRepository.findById(id).orElseThrow(() -> new DoctorNotFoundException(id));
		updateDoctorData(existingDoctor, updatedDoctor);
		return doctorRepository.save(existingDoctor);
	}

	/**
	 * Deletes a doctor by ID.
	 */
	public void deleteDoctor(Long id) {
		if (!doctorRepository.existsById(id)) {
			throw new DoctorNotFoundException(id);
		}
		doctorRepository.deleteById(id);
	}

	private void updateDoctorData(Doctor existing, Doctor updated) {
		existing.setFirstName(updated.getFirstName());
		existing.setLastName(updated.getLastName());
		existing.setEmail(updated.getEmail());
		existing.setSpecialization(updated.getSpecialization());
		existing.setPhoneNumber(updated.getPhoneNumber());
	}

	public Optional<Doctor> findById(Long id) {
		return doctorRepository.findById(id);
	}

}
