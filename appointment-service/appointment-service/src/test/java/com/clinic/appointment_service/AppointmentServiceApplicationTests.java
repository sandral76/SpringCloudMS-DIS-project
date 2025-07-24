package com.clinic.appointment_service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import com.clinic.appointment_service.client.DoctorClient;
import com.clinic.appointment_service.client.PatientClient;
import com.clinic.appointment_service.event.AppointmentFinishedEvent;
import com.clinic.appointment_service.exception.AppointmentNotFoundException;
import com.clinic.appointment_service.exception.DoctorNotFoundException;
import com.clinic.appointment_service.exception.PatientNotFoundException;
import com.clinic.appointment_service.messaging.BillingMessageProducer;
import com.clinic.appointment_service.model.Appointment;
import com.clinic.appointment_service.repository.AppointmentRepository;
import com.clinic.appointment_service.request.AppointmentRequest;
import com.clinic.appointment_service.service.AppointmentService;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDateTime;
import java.util.*;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class AppointmentServiceApplicationTests {

	@InjectMocks
    private AppointmentService appointmentService;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PatientClient patientClient;

    @Mock
    private DoctorClient doctorClient;

    @Mock
    private BillingMessageProducer billingMessageProducer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllAppointments() {
        List<Appointment> mockList = List.of(new Appointment(), new Appointment());
        when(appointmentRepository.findAll()).thenReturn(mockList);

        List<Appointment> result = appointmentService.getAllAppointments();
        assertEquals(2, result.size());
    }

    @Test
    void testGetAppointmentById_found() {
        Appointment appointment = new Appointment();
        appointment.setId(1L);
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        Appointment result = appointmentService.getAppointmentById(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetAppointmentById_notFound() {
        when(appointmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AppointmentNotFoundException.class, () -> {
            appointmentService.getAppointmentById(1L);
        });
    }

    @Test
    void testCreateAppointment_success() {
        AppointmentRequest request = new AppointmentRequest(1L, 2L, LocalDateTime.now(), "Test");
        when(patientClient.getPatientById(1L)).thenReturn(null);
        when(doctorClient.getDoctorById(2L)).thenReturn(null);

        Appointment saved = new Appointment();
        saved.setId(1L);
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(saved);

        Appointment result = appointmentService.createAppointment(request);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testCreateAppointment_patientNotFound() {
        AppointmentRequest request = new AppointmentRequest(1L, 2L, LocalDateTime.now(), "Test");

        when(patientClient.getPatientById(1L)).thenThrow(FeignException.NotFound.class);

        assertThrows(PatientNotFoundException.class, () -> {
            appointmentService.createAppointment(request);
        });
    }

    @Test
    void testCreateAppointment_doctorNotFound() {
        AppointmentRequest request = new AppointmentRequest(1L, 2L, LocalDateTime.now(), "Test");

        when(patientClient.getPatientById(1L)).thenReturn(null);
        when(doctorClient.getDoctorById(2L)).thenThrow(FeignException.NotFound.class);

        assertThrows(DoctorNotFoundException.class, () -> {
            appointmentService.createAppointment(request);
        });
    }

    @Test
    void testUpdateAppointment_finishedTriggersEvent() {
        Appointment existing = new Appointment();
        existing.setId(1L);
        existing.setStatus("SCHEDULED");

        Appointment update = new Appointment();
        update.setPatientId(1L);
        update.setDoctorId(2L);
        update.setAppointmentDateTime(LocalDateTime.now());
        update.setReason("Routine checkup");
        update.setStatus("FINISHED");

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(patientClient.getPatientById(1L)).thenReturn(null);
        when(doctorClient.getDoctorById(2L)).thenReturn(null);
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(existing);

        Appointment result = appointmentService.updateAppointment(1L, update);
        verify(billingMessageProducer).sendAppointmentFinishedEvent(any(AppointmentFinishedEvent.class));
        assertEquals("FINISHED", result.getStatus());
    }

    @Test
    void testDeleteAppointment_exists() {
        when(appointmentRepository.existsById(1L)).thenReturn(true);
        doNothing().when(appointmentRepository).deleteById(1L);

        assertDoesNotThrow(() -> appointmentService.deleteAppointment(1L));
    }

    @Test
    void testDeleteAppointment_notExists() {
        when(appointmentRepository.existsById(1L)).thenReturn(false);

        assertThrows(AppointmentNotFoundException.class, () -> appointmentService.deleteAppointment(1L));
    }

    @Test
    void testFindByDoctorId_found() {
        List<Appointment> list = List.of(new Appointment());
        when(appointmentRepository.findByDoctorId(1L)).thenReturn(list);

        List<Appointment> result = appointmentService.findByDoctorId(1L);
        assertEquals(1, result.size());
    }

    @Test
    void testFindByDoctorId_notFound() {
        when(appointmentRepository.findByDoctorId(1L)).thenReturn(Collections.emptyList());

        assertThrows(AppointmentNotFoundException.class, () -> appointmentService.findByDoctorId(1L));
    }

    @Test
    void testFindByPatientId_found() {
        List<Appointment> list = List.of(new Appointment());
        when(appointmentRepository.findByPatientId(1L)).thenReturn(list);

        List<Appointment> result = appointmentService.findByPatientId(1L);
        assertEquals(1, result.size());
    }

    @Test
    void testFindByPatientId_notFound() {
        when(appointmentRepository.findByPatientId(1L)).thenReturn(Collections.emptyList());

        assertThrows(AppointmentNotFoundException.class, () -> appointmentService.findByPatientId(1L));
    }

    @Test
    void testFindByDate_found() {
        List<Appointment> list = List.of(new Appointment());
        when(appointmentRepository.findByAppointmentDateTimeBetween(any(), any())).thenReturn(list);

        List<Appointment> result = appointmentService.findByDate(LocalDateTime.now().toLocalDate());
        assertEquals(1, result.size());
    }

    @Test
    void testFindByDate_notFound() {
        when(appointmentRepository.findByAppointmentDateTimeBetween(any(), any()))
                .thenReturn(Collections.emptyList());

        assertThrows(AppointmentNotFoundException.class, () -> {
            appointmentService.findByDate(LocalDateTime.now().toLocalDate());
        });
    }
}

