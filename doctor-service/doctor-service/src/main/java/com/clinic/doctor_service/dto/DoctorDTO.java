package com.clinic.doctor_service.dto;

import jakarta.validation.constraints.Pattern;

public class DoctorDTO {
	private Long id;
	private String name;
	private String specialization;
	@Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Invalid phone number")
	private String phone;

	public DoctorDTO() {

	}

	public DoctorDTO(Long id, String name, String specialization,
			@Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Invalid phone number") String phone) {
		super();
		this.id = id;
		this.name = name;
		this.specialization = specialization;
		this.phone = phone;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpecialization() {
		return specialization;
	}

	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
