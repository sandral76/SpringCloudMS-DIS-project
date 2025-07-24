package com.clinic.appointment_service.service;

import java.util.List;
import java.util.stream.Collectors;

import com.clinic.appointment_service.client.DoctorClient;
import com.clinic.appointment_service.client.PatientClient;
import com.clinic.appointment_service.dto.AppointmentDTO;
import com.clinic.appointment_service.mapper.AppointmetMapper;
import com.clinic.appointment_service.model.Appointment;

import org.springframework.stereotype.Service;


@Service
public class AppointmentHelper {

    private final DoctorClient doctorClient;
    private final PatientClient patientClient;

    public AppointmentHelper(DoctorClient doctorClient, PatientClient patientClient) {
        this.doctorClient = doctorClient;
        this.patientClient = patientClient;
    }

    public AppointmentDTO toDto(Appointment appointment) {
        String doctorName = doctorClient.getDoctorName(appointment.getDoctorId());
        String patientName = patientClient.getPatientName(appointment.getPatientId());

        return AppointmetMapper.toDto(appointment, doctorName, patientName);
    }

    public List<AppointmentDTO> toDtoList(List<Appointment> appointments) {
        return appointments.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}

