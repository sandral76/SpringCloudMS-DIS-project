package com.clinic.doctor_service;

import com.clinic.doctor_service.dto.DoctorDTO;
import com.clinic.doctor_service.mapper.DoctorMapper;
import com.clinic.doctor_service.model.Doctor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DoctorMapperTest {

    @Test
    void toDto_shouldMapDoctorToDtoCorrectly() {
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setFirstName("Sonja");
        doctor.setLastName("Vasic");
        doctor.setSpecialization("Cardiologist");
        doctor.setPhoneNumber("123456789");

        DoctorDTO dto = DoctorMapper.toDto(doctor);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Sonja Vasic");
        assertThat(dto.getSpecialization()).isEqualTo("Cardiologist");
        assertThat(dto.getPhone()).isEqualTo("123456789");
    }

    @Test
    void toEntity_shouldMapDtoToDoctorCorrectly() {
        DoctorDTO dto = new DoctorDTO(2L, "Boban Simic", "Neurologist", "987654321");

        Doctor doctor = DoctorMapper.toEntity(dto);

        assertThat(doctor).isNotNull();
        assertThat(doctor.getId()).isEqualTo(2L);
        assertThat(doctor.getFirstName()).isEqualTo("Boban");
        assertThat(doctor.getLastName()).isEqualTo("Simic");
        assertThat(doctor.getSpecialization()).isEqualTo("Neurologist");
        assertThat(doctor.getPhoneNumber()).isEqualTo("987654321");
    }

    @Test
    void toEntity_shouldHandleSingleNameCorrectly() {
        DoctorDTO dto = new DoctorDTO(3L, "Petar", "Neurologist", "000111222");
        Doctor doctor = DoctorMapper.toEntity(dto);

        assertThat(doctor.getFirstName()).isEqualTo("Petar");
        assertThat(doctor.getLastName()).isEqualTo("");
        assertThat(doctor.getSpecialization()).isEqualTo("Neurologist");
        assertThat(doctor.getPhoneNumber()).isEqualTo("000111222");
    }
}
