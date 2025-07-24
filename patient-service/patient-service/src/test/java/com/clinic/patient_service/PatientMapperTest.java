package com.clinic.patient_service;

import com.clinic.patient_service.dto.PatientDTO;
import com.clinic.patient_service.mapper.PatientMapper;
import com.clinic.patient_service.model.Patient;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PatientMapperTest {

    @Test
    void toDto_shouldMapPatientToDtoCorrectly() {
        // given
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setFirstName("Ana");
        patient.setLastName("Markovic");
        patient.setPhone("123456789");

        // when
        PatientDTO dto = PatientMapper.toDto(patient);

        // then
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Ana Markovic");
        assertThat(dto.getPhone()).isEqualTo("123456789");
    }

    @Test
    void toEntity_shouldMapDtoToPatientCorrectly() {
    	PatientDTO dto = new PatientDTO(2L, "Ivana Nikolic", "987654321");

        Patient patient = PatientMapper.toEntity(dto);

        assertThat(patient).isNotNull();
        assertThat(patient.getId()).isEqualTo(2L);
        assertThat(patient.getFirstName()).isEqualTo("Ivana");
        assertThat(patient.getLastName()).isEqualTo("Nikolic");
        assertThat(patient.getPhone()).isEqualTo("987654321");
    }

    @Test
    void toEntity_shouldHandleDtoWithSingleName() {
        PatientDTO dto = new PatientDTO(3L, "Marija", "000111222");

        Patient patient = PatientMapper.toEntity(dto);

        assertThat(patient).isNotNull();
        assertThat(patient.getFirstName()).isEqualTo("Marija");
        assertThat(patient.getLastName()).isEqualTo("");
    }
}
