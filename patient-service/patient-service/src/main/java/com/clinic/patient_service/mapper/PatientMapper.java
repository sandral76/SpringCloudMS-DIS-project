package com.clinic.patient_service.mapper;

import com.clinic.patient_service.dto.PatientDTO;
import com.clinic.patient_service.model.Patient;

public class PatientMapper {

	public static PatientDTO toDto(Patient patient) {
		String fullName = patient.getFirstName() + " " + patient.getLastName();
		return new PatientDTO(patient.getId(), fullName, patient.getPhone());
	}

	public static Patient toEntity(PatientDTO dto) {
		String[] parts = dto.getName().split(" ", 2);
		String firstName = parts.length > 0 ? parts[0] : "";
		String lastName = parts.length > 1 ? parts[1] : "";

		Patient patient = new Patient();
		patient.setId(dto.getId());
		patient.setFirstName(firstName);
		patient.setLastName(lastName);
		patient.setPhone(dto.getPhone());
		return patient;
	}

}
