package com.clinic.billing_service.repository;

import com.clinic.billing_service.model.Billing;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BillingRepository extends JpaRepository<Billing, Long> {
    List<Billing> findByAppointmentId(Long appointmentId);
}
