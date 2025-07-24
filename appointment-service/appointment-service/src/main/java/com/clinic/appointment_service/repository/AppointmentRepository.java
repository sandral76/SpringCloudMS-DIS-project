package com.clinic.appointment_service.repository;

import com.clinic.appointment_service.model.Appointment;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
	
	List<Appointment> findByDoctorId(Long doctorId);
	List<Appointment> findByPatientId(Long patientId);
	List<Appointment> findByAppointmentDateTimeBetween(LocalDateTime start, LocalDateTime end);

}
