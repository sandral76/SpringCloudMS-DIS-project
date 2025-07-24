package com.clinic.patient_service;

import com.clinic.patient_service.exception.PatientNotFoundException;
import com.clinic.patient_service.model.Patient;
import com.clinic.patient_service.repository.PatientRepository;
import com.clinic.patient_service.service.PatientService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

    private Patient patient;

    @BeforeEach
    void setUp() {
        patient = new Patient();
        patient.setId(1L);
        patient.setFirstName("Ana");
        patient.setLastName("Nikolic");
        patient.setEmail("anan@gmail.com");
        patient.setPhone("123456");
    }

    @Test
    void testGetAllPatients() {
        List<Patient> patients = List.of(patient);
        when(patientRepository.findAll()).thenReturn(patients);

        List<Patient> result = patientService.getAllPatients();

        assertEquals(1, result.size());
        assertEquals("Ana", result.get(0).getFirstName());
        verify(patientRepository, times(1)).findAll();
    }

    @Test
    void testGetPatientById_Found() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        Patient result = patientService.getPatientById(1L);

        assertNotNull(result);
        assertEquals("Ana", result.getFirstName());
        verify(patientRepository).findById(1L);
    }

    @Test
    void testGetPatientById_NotFound() {
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class, () -> patientService.getPatientById(1L));
        verify(patientRepository).findById(1L);
    }

    @Test
    void testCreatePatient() {
        when(patientRepository.save(patient)).thenReturn(patient);

        Patient result = patientService.createPatient(patient);

        assertNotNull(result);
        assertEquals("Ana", result.getFirstName());
        verify(patientRepository).save(patient);
    }

    @Test
    void testUpdatePatient_Found() {
        Patient updated = new Patient();
        updated.setFirstName("Ivana");
        updated.setLastName("Nikolic");
        updated.setEmail("ivanan@gmail.com");
        updated.setPhone("987-654");

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(patientRepository.save(any(Patient.class))).thenAnswer(inv -> inv.getArgument(0));

        Patient result = patientService.updatePatient(1L, updated);

        assertEquals("Ivana", result.getFirstName());
        assertEquals("Nikolic", result.getLastName());
        verify(patientRepository).findById(1L);
        verify(patientRepository).save(patient);
    }

    @Test
    void testUpdatePatient_NotFound() {
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class, () -> patientService.updatePatient(1L, new Patient()));
        verify(patientRepository).findById(1L);
    }

    @Test
    void testDeletePatient_Exists() {
        when(patientRepository.existsById(1L)).thenReturn(true);

        patientService.deletePatient(1L);

        verify(patientRepository).deleteById(1L);
    }

    @Test
    void testDeletePatient_NotFound() {
        when(patientRepository.existsById(1L)).thenReturn(false);

        assertThrows(PatientNotFoundException.class, () -> patientService.deletePatient(1L));
        verify(patientRepository, never()).deleteById(anyLong());
    }

    @Test
    void testFindById() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        Optional<Patient> result = patientService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Ana", result.get().getFirstName());
        verify(patientRepository).findById(1L);
    }
}
