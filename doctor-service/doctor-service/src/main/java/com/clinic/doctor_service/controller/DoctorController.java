package com.clinic.doctor_service.controller;

import com.clinic.doctor_service.dto.DoctorDTO;
import com.clinic.doctor_service.mapper.DoctorMapper;
import com.clinic.doctor_service.model.Doctor;
import com.clinic.doctor_service.service.DoctorService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping
    public ResponseEntity<List<DoctorDTO>> getAllDoctors() {
        List<DoctorDTO> doctorDTOs = doctorService.getAllDoctors()
                .stream()
                .map(DoctorMapper::toDto)
                .toList();
        return ResponseEntity.ok(doctorDTOs);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<DoctorDTO> getDctorById(@PathVariable Long id) {
        Doctor doctor = doctorService.getDoctorById(id);
        DoctorDTO dto = DoctorMapper.toDto(doctor);
        return ResponseEntity.ok(dto);
    }
    
    @PostMapping
    public ResponseEntity<DoctorDTO> createDoctor(@Valid @RequestBody Doctor doctor) {
    	Doctor savedDoctor = doctorService.createDoctor(doctor);
        return new ResponseEntity<>(DoctorMapper.toDto(savedDoctor), HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<DoctorDTO> updateDoctor(@PathVariable Long id, @Valid @RequestBody Doctor doctor) {
    	Doctor updated = doctorService.updateDoctor(id, doctor);
            return ResponseEntity.ok(DoctorMapper.toDto(updated));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
    	doctorService.deleteDoctor(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{id}/name")
    public ResponseEntity<String> getDoctorName(@PathVariable Long id) {
        return doctorService.findById(id)
                .map(doctor -> ResponseEntity.ok(doctor.getFirstName() + " " + doctor.getLastName()))
                .orElse(ResponseEntity.notFound().build());
    }

}
