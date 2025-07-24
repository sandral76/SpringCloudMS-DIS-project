package com.clinic.appointment_service.service;

import com.clinic.appointment_service.exception.AppointmentNotFoundException;
import com.clinic.appointment_service.exception.DoctorNotFoundException;
import com.clinic.appointment_service.model.Appointment;
import com.clinic.appointment_service.repository.AppointmentRepository;
import com.clinic.appointment_service.request.AppointmentRequest;
import com.clinic.appointment_service.exception.PatientNotFoundException;
import com.clinic.appointment_service.messaging.BillingMessageProducer;
import com.clinic.appointment_service.client.DoctorClient;
import com.clinic.appointment_service.client.PatientClient;
import com.clinic.appointment_service.event.AppointmentFinishedEvent;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

	@Autowired
	private AppointmentRepository appointmentRepository;

	@Autowired
	private PatientClient patientClient;

	@Autowired
	private DoctorClient doctorClient;
	
	@Autowired
	private  BillingMessageProducer billingMessageProducer;


	/**
	 * Returns a list of all appointments.
	 */
	public List<Appointment> getAllAppointments() {
		return appointmentRepository.findAll();
	}

	/**
	 * Finds a appointment by ID.
	 */
	public Appointment getAppointmentById(Long id) {
		return appointmentRepository.findById(id).orElseThrow(() -> new AppointmentNotFoundException(id));
	}

	/**
	 * Saves a new appointment.
	 */
	@CircuitBreaker(name = "doctor-service", fallbackMethod = "handleDoctorFallback")
	public Appointment createAppointment(AppointmentRequest request) {
		try {
			patientClient.getPatientById(request.getPatientId());
		} catch (FeignException.NotFound e) {
			throw new PatientNotFoundException(request.getPatientId());
		}

		try {
			doctorClient.getDoctorById(request.getDoctorId());
		} catch (FeignException.NotFound e) {
			throw new DoctorNotFoundException(request.getDoctorId());
		}

		Appointment appointment = new Appointment();
		appointment.setPatientId(request.getPatientId());
		appointment.setDoctorId(request.getDoctorId());
		appointment.setAppointmentDateTime(request.getAppointmentTime());
		appointment.setReason(request.getReason());
		appointment.setStatus("SCHEDULED"); // default status

		return appointmentRepository.save(appointment);
	}

	/**
	 * Updates an existing appointment by ID.
	 */
	public Appointment updateAppointment(Long id, Appointment updatedAppointment) {
		Appointment existingAppointment = appointmentRepository.findById(id)
				.orElseThrow(() -> new AppointmentNotFoundException(id));

		try {
			patientClient.getPatientById(updatedAppointment.getPatientId());
		} catch (FeignException.NotFound e) {
			throw new PatientNotFoundException(updatedAppointment.getPatientId());
		}

		try {
			doctorClient.getDoctorById(updatedAppointment.getDoctorId());
		} catch (FeignException.NotFound e) {
			throw new DoctorNotFoundException(updatedAppointment.getDoctorId());
		}

		existingAppointment.setPatientId(updatedAppointment.getPatientId());
		existingAppointment.setDoctorId(updatedAppointment.getDoctorId());
		existingAppointment.setAppointmentDateTime(updatedAppointment.getAppointmentDateTime());
		existingAppointment.setReason(updatedAppointment.getReason());
		existingAppointment.setStatus(updatedAppointment.getStatus());
		if (updatedAppointment.getStatus().equalsIgnoreCase("FINISHED")) {
		    AppointmentFinishedEvent event = new AppointmentFinishedEvent();
		    event.setAppointmentId(id);
		    event.setPatientId(updatedAppointment.getPatientId());
		    event.setDoctorId(updatedAppointment.getDoctorId());
		    billingMessageProducer.sendAppointmentFinishedEvent(event);
		}
		return appointmentRepository.save(existingAppointment);
	}

	/**
	 * Deletes a appointment by ID.
	 */
	public void deleteAppointment(Long id) {
		if (!appointmentRepository.existsById(id)) {
			throw new AppointmentNotFoundException(id);
		}
		appointmentRepository.deleteById(id);
	}

	/**
	 * Finds a appointments for one doctor.
	 */
	public List<Appointment> findByDoctorId(Long doctorId) {
		List<Appointment> appointments = appointmentRepository.findByDoctorId(doctorId);

		if (appointments.isEmpty()) {
			throw new AppointmentNotFoundException("No appointments found for doctor with ID: " + doctorId);
		}

		return appointments;
	}

	/**
	 * Finds a appointments for one patient.
	 */
	@CircuitBreaker(name = "patient-service", fallbackMethod = "fallbackGetPatient")
	public List<Appointment> findByPatientId(Long patientId) {
		List<Appointment> appointments = appointmentRepository.findByPatientId(patientId);

		if (appointments.isEmpty()) {
			throw new AppointmentNotFoundException("No appointments found for patient with ID: " + patientId);
		}

		return appointments;
	}

	/**
	 * Finds a appointments for specific date.
	 */
	public List<Appointment> findByDate(LocalDate date) {
		LocalDateTime startOfDay = date.atStartOfDay();
		LocalDateTime endOfDay = date.plusDays(1).atStartOfDay().minusNanos(1);

		List<Appointment> appointments = appointmentRepository.findByAppointmentDateTimeBetween(startOfDay, endOfDay);

		if (appointments.isEmpty()) {
			throw new AppointmentNotFoundException("No appointments found for date: " + date);
		}

		return appointments;
	}

	public Optional<Appointment> findById(Long id) {
		return appointmentRepository.findById(id);
	}
	
	public Appointment handleDoctorFallback(AppointmentRequest request, Throwable t) {
	    throw new RuntimeException("Doctor service unavailable: " + t.getMessage());
	}
	
	public Appointment fallbackGetPatient(Long id, Throwable t) {
	    throw new RuntimeException("Patient service unavailable: " + t.getMessage());
	}
}
