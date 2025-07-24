package com.clinic.doctor_service.mapper;

import com.clinic.doctor_service.dto.DoctorDTO;
import com.clinic.doctor_service.model.Doctor;

public class DoctorMapper {
	public static DoctorDTO toDto(Doctor doctor) {
		String fullName = doctor.getFirstName() + " " + doctor.getLastName();
		return new DoctorDTO(doctor.getId(), fullName,doctor.getSpecialization(), doctor.getPhoneNumber());
	}

	public static Doctor toEntity(DoctorDTO dto) {
		String[] parts = dto.getName().split(" ", 2);
		String firstName = parts.length > 0 ? parts[0] : "";
		String lastName = parts.length > 1 ? parts[1] : "";

		Doctor doctor = new Doctor();
		doctor.setId(dto.getId());
		doctor.setFirstName(firstName);
		doctor.setLastName(lastName);
		doctor.setSpecialization(dto.getSpecialization());
		doctor.setPhoneNumber(dto.getPhone());
		return doctor;
	}
}
