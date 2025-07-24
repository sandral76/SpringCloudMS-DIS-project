package com.clinic.billing_service;

import com.clinic.billing_service.controller.BillingController;
import com.clinic.billing_service.exception.BillingNotFoundException;
import com.clinic.billing_service.model.Billing;
import com.clinic.billing_service.service.BillingService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BillingController.class)
public class BillingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BillingService billingService;

    @Autowired
    private ObjectMapper objectMapper;

    private Billing createBilling(Long id) {
        Billing billing = new Billing();
        billing.setId(id);
        billing.setAppointmentId(1L);
        billing.setAmount((double) 100);
        billing.setBillingDate(LocalDateTime.now());
        billing.setStatus("PENDING");
        return billing;
    }

    @Test
    void testGetAllBillings() throws Exception {
        Billing billing1 = createBilling(1L);
        Billing billing2 = createBilling(2L);

        List<Billing> billings = Arrays.asList(billing1, billing2);
        Mockito.when(billingService.getAllBillings()).thenReturn(billings);

        mockMvc.perform(get("/billings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testGetBillingById() throws Exception {
        Billing billing = createBilling(1L);
        Mockito.when(billingService.getBillingById(1L)).thenReturn(billing);

        mockMvc.perform(get("/billings/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.appointmentId").value(1L))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void testCreateBilling() throws Exception {
        Billing billing = createBilling(null);
        Billing savedBilling = createBilling(1L);

        Mockito.when(billingService.createBilling(any(Billing.class))).thenReturn(savedBilling);

        mockMvc.perform(post("/billings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(billing)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testUpdateBilling() throws Exception {
        Billing billing = createBilling(1L);

        Mockito.when(billingService.updateBilling(eq(1L), any(Billing.class))).thenReturn(billing);

        mockMvc.perform(put("/billings/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(billing)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testDeleteBilling() throws Exception {
        mockMvc.perform(delete("/billings/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(billingService).deleteBilling(1L);
    }
    
    @Test
    void testGetBillingByIdNotFound() throws Exception {
        Mockito.when(billingService.getBillingById(999L))
               .thenThrow(new BillingNotFoundException("Billing not found"));

        mockMvc.perform(get("/billings/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Billing not found"));
    }

}

