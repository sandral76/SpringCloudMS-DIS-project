package com.clinic.appointment_service.mapper;

import com.clinic.appointment_service.dto.AppointmentDTO;
import com.clinic.appointment_service.model.Appointment;

public class AppointmetMapper {
	
	public static AppointmentDTO toDto(Appointment appointment, String doctorName, String patientName) {
		AppointmentDTO dto = new AppointmentDTO();
		dto.setId(appointment.getId());
		dto.setAppointmentTime(appointment.getAppointmentDateTime());
		dto.setStatus(appointment.getStatus());
		dto.setDoctorName(doctorName);
		dto.setPatientName(patientName);
		dto.setReason(appointment.getReason());
		return dto;
	}
	
}
