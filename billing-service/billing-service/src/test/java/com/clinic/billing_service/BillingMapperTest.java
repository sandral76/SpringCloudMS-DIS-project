package com.clinic.billing_service;

import com.clinic.billing_service.dto.BillingDTO;
import com.clinic.billing_service.mapper.BillingMapper;
import com.clinic.billing_service.model.Billing;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class BillingMapperTest {

    @Test
    public void testToDto_shouldMapEntityToDtoCorrectly() {
        Billing billing = new Billing();
        billing.setId(1L);
        billing.setAppointmentId(100L);
        billing.setAmount((double) 250);
        billing.setBillingDate(LocalDateTime.now());
        billing.setStatus("PAID");

        BillingDTO dto = BillingMapper.toDto(billing);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals(100L, dto.getAppointmentId());
        assertEquals(250, dto.getAmount());
        assertEquals("PAID", dto.getStatus());
    }

    @Test
    public void testToEntity_shouldMapDtoToEntityCorrectly() {
        BillingDTO dto = new BillingDTO();
        dto.setId(2L);
        dto.setAppointmentId(200L);
        dto.setAmount(300.00);
        dto.setStatus("UNPAID");

        Billing entity = BillingMapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(2L, entity.getId());
        assertEquals(200L, entity.getAppointmentId());
        assertEquals(300, entity.getAmount());
        assertEquals("UNPAID", entity.getStatus());
        assertNull(entity.getBillingDate());
    }
}
