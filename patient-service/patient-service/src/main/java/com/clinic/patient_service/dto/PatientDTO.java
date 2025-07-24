package com.clinic.patient_service.dto;

import jakarta.validation.constraints.Pattern;

public class PatientDTO {

	private Long id;
	private String name;

	@Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Invalid phone number")
	private String phone;

	public PatientDTO() {
	}

	public PatientDTO(Long id, String name,
			@Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Invalid phone number") String phone) {
		this.id = id;
		this.name = name;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
