package com.clinic.appointment_service;

import com.clinic.appointment_service.dto.AppointmentDTO;
import com.clinic.appointment_service.mapper.AppointmetMapper;
import com.clinic.appointment_service.model.Appointment;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class AppointmetMapperTest {

    @Test
    public void testToDto_shouldMapAppointmentToDtoCorrectly() {
        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setAppointmentDateTime(LocalDateTime.of(2025, 7, 21, 14, 30));
        appointment.setStatus("FINISHED");
        appointment.setReason("Routine checkup");

        String doctorName = "Dr. Ana Nikolic";
        String patientName = "Jovana Simic";

        AppointmentDTO dto = AppointmetMapper.toDto(appointment, doctorName, patientName);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals(LocalDateTime.of(2025, 7, 21, 14, 30), dto.getAppointmentTime());
        assertEquals("FINISHED", dto.getStatus());
        assertEquals("Dr. Ana Nikolic", dto.getDoctorName());
        assertEquals("Jovana Simic", dto.getPatientName());
        assertEquals("Routine checkup", dto.getReason());
    }
}
