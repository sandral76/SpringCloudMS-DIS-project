package com.clinic.appointment_service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.clinic.appointment_service.client.DoctorClient;
import com.clinic.appointment_service.client.DoctorDTO;
import com.clinic.appointment_service.client.PatientClient;
import com.clinic.appointment_service.controller.AppointmentController;
import com.clinic.appointment_service.messaging.BillingMessageProducer;
import com.clinic.appointment_service.service.AppointmentService;

import jakarta.ws.rs.core.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AppointmentIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private PatientClient patientClient;
    
    @MockBean
    private BillingMessageProducer billingMessageProducer; 
    
    /*@Test
    public void testCreateAndGetAppointment() throws Exception {
        String appointmentJson = """
            {
                "patientId": 1,
                "doctorId": 2,
                "date": "2025-07-21"
            }
        """;

        mockMvc.perform(post("/appointments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(appointmentJson))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.patientId").value(1));

        mockMvc.perform(get("/appointments/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.doctorId").value(2));
    }*/
}




