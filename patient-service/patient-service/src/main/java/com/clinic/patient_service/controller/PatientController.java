package com.clinic.patient_service.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.clinic.patient_service.dto.PatientDTO;
import com.clinic.patient_service.mapper.PatientMapper;
import com.clinic.patient_service.model.Patient;
import com.clinic.patient_service.service.PatientService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @GetMapping
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
        List<PatientDTO> patientDTOs = patientService.getAllPatients()
                .stream()
                .map(PatientMapper::toDto)
                .toList();
        return ResponseEntity.ok(patientDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDTO> getPatientById(@PathVariable Long id) {
        Patient patient = patientService.getPatientById(id);
        PatientDTO dto = PatientMapper.toDto(patient);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<PatientDTO> createPatient(@Valid @RequestBody Patient patient) {
        Patient savedPatient = patientService.createPatient(patient);
        return new ResponseEntity<>(PatientMapper.toDto(savedPatient), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientDTO> updatePatient(@PathVariable Long id, @Valid @RequestBody Patient patient) {
            Patient updated = patientService.updatePatient(id, patient);
            return ResponseEntity.ok(PatientMapper.toDto(updated));
    }
 
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/name")
    public ResponseEntity<String> getPatientName(@PathVariable Long id) {
        return patientService.findById(id)
                .map(patient -> ResponseEntity.ok(patient.getFirstName() + " " + patient.getLastName()))
                .orElse(ResponseEntity.notFound().build());
    }


}


