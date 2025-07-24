package com.clinic.appointment_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "doctor-service")
public interface DoctorClient {

    @GetMapping("/doctors/{id}")
    ResponseEntity<Object> getDoctorById(@PathVariable("id") Long id);

    @GetMapping("/doctors/{id}/name")
    String getDoctorName(@PathVariable("id") Long id);
}
