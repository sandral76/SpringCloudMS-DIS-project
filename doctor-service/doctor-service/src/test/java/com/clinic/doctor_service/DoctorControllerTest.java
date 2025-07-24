package com.clinic.doctor_service;

import com.clinic.doctor_service.controller.DoctorController;
import com.clinic.doctor_service.dto.DoctorDTO;
import com.clinic.doctor_service.exception.DoctorNotFoundException;
import com.clinic.doctor_service.model.Doctor;
import com.clinic.doctor_service.service.DoctorService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DoctorController.class)
public class DoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DoctorService doctorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllDoctors_returnsList() throws Exception {
        DoctorDTO dto1 = new DoctorDTO(1L, "Ana Jovic", "Cardiology", "123-456");
        DoctorDTO dto2 = new DoctorDTO(2L, "Marko Petrovic", "Neurology", "789-012");

        when(doctorService.getAllDoctors()).thenReturn(
                Arrays.asList(
                        new Doctor(1L, "Ana", "Jovic", "Cardiology","ana.jovic@gmail.com", "123-456"),
                        new Doctor(2L, "Marko", "Petrovic","marko.petrovic@gmail.com", "Neurology", "789-012")
                )
        );

        mockMvc.perform(get("/doctors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)))
                .andExpect(jsonPath("$[0].name", is("Ana Jovic")))
                .andExpect(jsonPath("$[0].specialization", is("Cardiology")))
                .andExpect(jsonPath("$[1].name", is("Marko Petrovic")));
    }

    @Test
    void getDoctorById_returnsDoctor() throws Exception {
        Doctor doctor = new Doctor(1L, "Ana", "Jovic", "Cardiology","ana.jovic@gmail.com", "123-456");
        when(doctorService.getDoctorById(1L)).thenReturn(doctor);

        mockMvc.perform(get("/doctors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Ana Jovic")))
                .andExpect(jsonPath("$.specialization", is("Cardiology")));
    }

    @Test
    void createDoctor_returnsCreatedDoctor() throws Exception {
        Doctor input = new Doctor(null, "Ana", "Jovic", "Cardiology", "ana.jovic@gmail.com", "123-456");
        Doctor saved = new Doctor(1L, "Ana", "Jovic", "Cardiology","ana.jovic@gmail.com",  "123-456");

        when(doctorService.createDoctor(any(Doctor.class))).thenReturn(saved);

        mockMvc.perform(post("/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Ana Jovic")))
                .andExpect(jsonPath("$.specialization", is("Cardiology")));
    }

    @Test
    void updateDoctor_returnsUpdatedDoctor() throws Exception {
        Doctor input = new Doctor(null, "Ana", "Jovic", "Dermatology","ana.jovic@gmail.com", "321-654");
        Doctor updated = new Doctor(1L, "Ana", "Jovic", "Dermatology","ana.jovic@gmail.com", "321-654");

        when(doctorService.updateDoctor(Mockito.eq(1L), any(Doctor.class))).thenReturn(updated);

        mockMvc.perform(put("/doctors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Ana Jovic")))
                .andExpect(jsonPath("$.specialization", is("Dermatology")));
    }

    @Test
    void deleteDoctor_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/doctors/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getDoctorName_returnsFullName() throws Exception {
        Doctor doctor = new Doctor(1L, "Ana", "Jovic", "Cardiology","ana.jovic@gmail.com", "123-456");

        when(doctorService.findById(1L)).thenReturn(Optional.of(doctor));

        mockMvc.perform(get("/doctors/1/name"))
                .andExpect(status().isOk())
                .andExpect(content().string("Ana Jovic"));
    }

    @Test
    void getDoctorName_returnsNotFound() throws Exception {
        when(doctorService.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/doctors/999/name"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void whenDoctorNotFound_thenReturns404() throws Exception {
        Long invalidId = 999L;

        when(doctorService.getDoctorById(invalidId))
            .thenThrow(new DoctorNotFoundException("Doctor with ID " + invalidId + " not found"));

        mockMvc.perform(get("/doctors/{id}", invalidId))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("Doctor with ID 999 not found"));
    }
}
