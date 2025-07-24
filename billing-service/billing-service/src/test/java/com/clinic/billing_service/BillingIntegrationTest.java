package com.clinic.billing_service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BillingIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    /*@Test
    public void testGetAllBills() throws Exception {
        mockMvc.perform(get("/billings"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testCreateBill() throws Exception {
        String newBillJson = """
            {
                "patientId": 1,
                "amount": 100.0,
            }
        """;

        mockMvc.perform(post("/billings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newBillJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.amount").value(100.0));
    }*/
}
