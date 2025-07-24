package com.clinic.doctor_service;

import com.clinic.doctor_service.exception.DoctorNotFoundException;
import com.clinic.doctor_service.model.Doctor;
import com.clinic.doctor_service.repository.DoctorRepository;
import com.clinic.doctor_service.service.DoctorService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DoctorServiceTest {

    private DoctorRepository doctorRepository;
    private DoctorService doctorService;

    @BeforeEach
    void setUp() {
        doctorRepository = mock(DoctorRepository.class);
        doctorService = new DoctorService();
        var field = Arrays.stream(DoctorService.class.getDeclaredFields())
                .filter(f -> f.getType().equals(DoctorRepository.class))
                .findFirst()
                .orElseThrow();
        field.setAccessible(true);
        try {
            field.set(doctorService, doctorRepository);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetAllDoctors() {
        when(doctorRepository.findAll()).thenReturn(Arrays.asList(new Doctor(), new Doctor()));
        assertEquals(2, doctorService.getAllDoctors().size());
    }

    @Test
    void testGetDoctorByIdExists() {
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        Doctor result = doctorService.getDoctorById(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetDoctorByIdNotFound() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(DoctorNotFoundException.class, () -> doctorService.getDoctorById(1L));
    }

    @Test
    void testCreateDoctor() {
        Doctor doctor = new Doctor();
        when(doctorRepository.save(doctor)).thenReturn(doctor);
        assertEquals(doctor, doctorService.createDoctor(doctor));
    }

    @Test
    void testUpdateDoctor() {
        Doctor existing = new Doctor();
        existing.setId(1L);
        existing.setFirstName("Marinko");
        Doctor updated = new Doctor();
        updated.setFirstName("Marina");
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(doctorRepository.save(existing)).thenReturn(existing);
        Doctor result = doctorService.updateDoctor(1L, updated);
        assertEquals("Marina", result.getFirstName());
    }

    @Test
    void testDeleteDoctorExists() {
        when(doctorRepository.existsById(1L)).thenReturn(true);
        doctorService.deleteDoctor(1L);
        verify(doctorRepository).deleteById(1L);
    }

    @Test
    void testDeleteDoctorNotFound() {
        when(doctorRepository.existsById(1L)).thenReturn(false);
        assertThrows(DoctorNotFoundException.class, () -> doctorService.deleteDoctor(1L));
    }
}
