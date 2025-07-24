package com.clinic.appointment_service;

import com.clinic.appointment_service.controller.AppointmentController;
import com.clinic.appointment_service.dto.AppointmentDTO;
import com.clinic.appointment_service.model.Appointment;
import com.clinic.appointment_service.request.AppointmentRequest;
import com.clinic.appointment_service.service.AppointmentHelper;
import com.clinic.appointment_service.service.AppointmentService;
import static org.mockito.ArgumentMatchers.anyList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.Mockito.verify;

import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;


class AppointmentControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	
	@Autowired
	private ObjectMapper objectMapper;

    @Mock
    private AppointmentService appointmentService;

    @Mock
    private AppointmentHelper appointmentHelper;

    @InjectMocks
    private AppointmentController appointmentController;

    private Appointment mockAppointment;
    private AppointmentDTO mockDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockAppointment = new Appointment();
        mockAppointment.setId(1L);

        mockDto = new AppointmentDTO();
        mockDto.setId(1L);
    }

    @Test
    void testGetAllAppointments() {
        when(appointmentService.getAllAppointments()).thenReturn(List.of(mockAppointment));
        when(appointmentHelper.toDtoList(anyList())).thenReturn(List.of(mockDto));

        ResponseEntity<List<AppointmentDTO>> response = appointmentController.getAllAppointments();

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).hasSize(1);
        verify(appointmentService).getAllAppointments();
    }

    @Test
    void testGetAppointmentById() {
        when(appointmentService.getAppointmentById(1L)).thenReturn(mockAppointment);
        when(appointmentHelper.toDto(mockAppointment)).thenReturn(mockDto);

        ResponseEntity<AppointmentDTO> response = appointmentController.getAppointmentById(1L);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(mockDto);
    }

    @Test
    void testCreateAppointmentDTO() {
        AppointmentRequest request = new AppointmentRequest();
        when(appointmentService.createAppointment(request)).thenReturn(mockAppointment);
        when(appointmentHelper.toDto(mockAppointment)).thenReturn(mockDto);

        ResponseEntity<AppointmentDTO> response = appointmentController.createAppointmentDTO(request);

        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getBody()).isEqualTo(mockDto);
    }

    @Test
    void testUpdateAppointment() {
        when(appointmentService.updateAppointment(eq(1L), any(Appointment.class))).thenReturn(mockAppointment);
        when(appointmentHelper.toDto(mockAppointment)).thenReturn(mockDto);

        ResponseEntity<AppointmentDTO> response = appointmentController.updateAppointment(1L, mockAppointment);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(mockDto);
    }

    @Test
    void testDeleteAppointment() {
        ResponseEntity<Void> response = appointmentController.deleteAppointment(1L);

        assertThat(response.getStatusCodeValue()).isEqualTo(204);
        verify(appointmentService).deleteAppointment(1L);
    }

    @Test
    void testGetAppointmentsByDoctor() {
        when(appointmentService.findByDoctorId(10L)).thenReturn(List.of(mockAppointment));
        when(appointmentHelper.toDtoList(anyList())).thenReturn(List.of(mockDto));

        ResponseEntity<List<AppointmentDTO>> response = appointmentController.getAppointmentsByDoctor(10L);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).containsExactly(mockDto);
    }

    @Test
    void testGetAppointmentsByPatient() {
        when(appointmentService.findByPatientId(20L)).thenReturn(List.of(mockAppointment));
        when(appointmentHelper.toDtoList(anyList())).thenReturn(List.of(mockDto));

        ResponseEntity<List<AppointmentDTO>> response = appointmentController.getAppointmentsByPatient(20L);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).containsExactly(mockDto);
    }

    @Test
    void testGetAppointmentsByDate() {
        LocalDate date = LocalDate.of(2025, 7, 20);
        when(appointmentService.findByDate(date)).thenReturn(List.of(mockAppointment));
        when(appointmentHelper.toDtoList(anyList())).thenReturn(List.of(mockDto));

        ResponseEntity<List<AppointmentDTO>> response = appointmentController.getAppointmentsByDate(date);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).containsExactly(mockDto);
    }
   
}
