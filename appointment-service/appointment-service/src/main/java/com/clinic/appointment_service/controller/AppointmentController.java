package com.clinic.appointment_service.controller;

import com.clinic.appointment_service.dto.AppointmentDTO;
import com.clinic.appointment_service.model.Appointment;
import com.clinic.appointment_service.request.AppointmentRequest;
import com.clinic.appointment_service.service.AppointmentHelper;
import com.clinic.appointment_service.service.AppointmentService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

	@Autowired
    private AppointmentService appointmentService;
	
	@Autowired
    private AppointmentHelper appointmentHelper;
	
	@GetMapping
    public ResponseEntity<List<AppointmentDTO>> getAllAppointments() {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        List<AppointmentDTO> dtoList = appointmentHelper.toDtoList(appointments);
        return ResponseEntity.ok(dtoList);
    }
	
	@GetMapping("/{id}")
    public ResponseEntity<AppointmentDTO> getAppointmentById(@PathVariable Long id) {
		Appointment appointment = appointmentService.getAppointmentById(id);
		AppointmentDTO dto = appointmentHelper.toDto(appointment);
        return ResponseEntity.ok(dto);
    }
    
	@PostMapping
    public ResponseEntity<AppointmentDTO> createAppointmentDTO(@Valid @RequestBody AppointmentRequest appointment) {
		Appointment savedAppointment = appointmentService.createAppointment(appointment);
        return new ResponseEntity<>(appointmentHelper.toDto(savedAppointment), HttpStatus.CREATED);
    }
	
	@PutMapping("/{id}")
    public ResponseEntity<AppointmentDTO> updateAppointment(@PathVariable Long id, @Valid @RequestBody Appointment appointment) {
		Appointment updated = appointmentService.updateAppointment(id, appointment);
            return ResponseEntity.ok(appointmentHelper.toDto(updated));
    }
	
	@DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
		appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByDoctor(@PathVariable Long doctorId) {
        List<Appointment> appointments = appointmentService.findByDoctorId(doctorId);
        return ResponseEntity.ok(appointmentHelper.toDtoList(appointments));
    }
    
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByPatient(@PathVariable Long patientId) {
        List<Appointment> appointments = appointmentService.findByPatientId(patientId);
        return ResponseEntity.ok(appointmentHelper.toDtoList(appointments));
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<Appointment> appointments = appointmentService.findByDate(date);
        return ResponseEntity.ok(appointmentHelper.toDtoList(appointments));
    }

}
