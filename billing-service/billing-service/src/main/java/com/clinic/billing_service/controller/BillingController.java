package com.clinic.billing_service.controller;

import com.clinic.billing_service.dto.BillingDTO;
import com.clinic.billing_service.mapper.BillingMapper;
import com.clinic.billing_service.model.Billing;
import com.clinic.billing_service.service.BillingService;


import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/billings")
public class BillingController {

    @Autowired
    private BillingService billingService;
    
    @GetMapping
    public ResponseEntity<List<BillingDTO>> getAllBillings() {
        List<BillingDTO> billingsDTOs = billingService.getAllBillings()
                .stream()
                .map(BillingMapper::toDto)
                .toList();
        return ResponseEntity.ok(billingsDTOs);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<BillingDTO> getBillingById(@PathVariable Long id) {
    	Billing billing = billingService.getBillingById(id);
    	BillingDTO dto = BillingMapper.toDto(billing);
        return ResponseEntity.ok(dto);
    }
    
    @PostMapping
    public ResponseEntity<BillingDTO> createBilling(@Valid @RequestBody Billing billing) {
    	Billing savedBilling = billingService.createBilling(billing);
        return new ResponseEntity<>(BillingMapper.toDto(savedBilling), HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<BillingDTO> updateBilling(@PathVariable Long id, @Valid @RequestBody Billing billing) {
    	Billing updated = billingService.updateBilling(id, billing);
            return ResponseEntity.ok(BillingMapper.toDto(updated));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBilling(@PathVariable Long id) {
    	billingService.deleteBilling(id);
        return ResponseEntity.noContent().build();
    }
    
}
