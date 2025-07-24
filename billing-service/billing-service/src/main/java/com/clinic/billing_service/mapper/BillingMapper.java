package com.clinic.billing_service.mapper;

import com.clinic.billing_service.dto.BillingDTO;
import com.clinic.billing_service.model.Billing;

public class BillingMapper {
	public static BillingDTO toDto(Billing billing) {
        BillingDTO dto = new BillingDTO();
        dto.setId(billing.getId());
        dto.setAppointmentId(billing.getAppointmentId());
        dto.setAmount(billing.getAmount());
        dto.setStatus(billing.getStatus());
        return dto;
    }

    public static Billing toEntity(BillingDTO dto) {
        Billing billing = new Billing();
        billing.setId(dto.getId());
        billing.setAppointmentId(dto.getAppointmentId());
        billing.setAmount(dto.getAmount());
        billing.setBillingDate(billing.getBillingDate());
        billing.setStatus(dto.getStatus());
        return billing;
    }
}
