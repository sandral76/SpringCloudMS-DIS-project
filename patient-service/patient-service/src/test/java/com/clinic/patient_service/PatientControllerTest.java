package com.clinic.patient_service;

import com.clinic.patient_service.controller.PatientController;
import com.clinic.patient_service.dto.PatientDTO;
import com.clinic.patient_service.exception.PatientNotFoundException;
import com.clinic.patient_service.model.Patient;
import com.clinic.patient_service.service.PatientService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatientController.class)
public class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;

    @Autowired
    private ObjectMapper objectMapper;

    // helper
    private Patient samplePatient() {
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setFirstName("Ana");
        patient.setLastName("Nikolic");
        return patient;
    }

    private PatientDTO sampleDTO() {
        PatientDTO dto = new PatientDTO();
        dto.setId(1L);
        dto.setName("Ana");
        return dto;
    }

    @Test
    void getAllPatients_returnsList() throws Exception {
        List<Patient> patients = List.of(samplePatient());
        Mockito.when(patientService.getAllPatients()).thenReturn(patients);

        mockMvc.perform(get("/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Ana Nikolic"));
    }

    @Test
    void getPatientById_returnsPatient() throws Exception {
        Mockito.when(patientService.getPatientById(1L)).thenReturn(samplePatient());

        mockMvc.perform(get("/patients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ana Nikolic"));
    }

    @Test
    void createPatient_returnsCreated() throws Exception {
        Patient input = samplePatient();
        input.setId(null);
        Patient saved = samplePatient();

        Mockito.when(patientService.createPatient(any(Patient.class))).thenReturn(saved);

        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Ana Nikolic"));
    }

    @Test
    void updatePatient_returnsUpdated() throws Exception {
        Patient updated = samplePatient();
        updated.setFirstName("Ana");

        Mockito.when(patientService.updatePatient(eq(1L), any(Patient.class))).thenReturn(updated);

        mockMvc.perform(put("/patients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ana Nikolic"));
    }

    @Test
    void deletePatient_returnsNoContent() throws Exception {
        Mockito.doNothing().when(patientService).deletePatient(1L);

        mockMvc.perform(delete("/patients/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getPatientName_returnsFullName() throws Exception {
        Mockito.when(patientService.findById(1L)).thenReturn(Optional.of(samplePatient()));

        mockMvc.perform(get("/patients/1/name"))
                .andExpect(status().isOk())
                .andExpect(content().string("Ana Nikolic"));
    }

    @Test
    void getPatientName_returnsNotFound() throws Exception {
        Mockito.when(patientService.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/patients/999/name"))
                .andExpect(status().isNotFound());
    }
    @Test
    void whenPatientNotFound_thenReturns404() throws Exception {
        Long invalidId = 999L;

        when(patientService.getPatientById(invalidId))
            .thenThrow(new PatientNotFoundException("Patient with ID " + invalidId + " not found"));

        mockMvc.perform(get("/patients/{id}", invalidId))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("Patient with ID 999 not found"));
    }

}
