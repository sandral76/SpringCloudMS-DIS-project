package com.clinic.appointment_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "patient-service")
public interface PatientClient {

    @GetMapping("/patients/{id}")
    ResponseEntity<Object> getPatientById(@PathVariable("id") Long id);

    @GetMapping("/patients/{id}/name")
    String getPatientName(@PathVariable("id") Long id);
}
