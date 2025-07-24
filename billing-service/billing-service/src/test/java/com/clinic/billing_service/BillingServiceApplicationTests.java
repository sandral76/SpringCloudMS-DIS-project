package com.clinic.billing_service;

import com.clinic.billing_service.client.AppointmentClient;
import com.clinic.billing_service.dto.AppointmentDto;
import com.clinic.billing_service.exception.AppointmentNotFoundException;
import com.clinic.billing_service.exception.BillingNotFoundException;
import com.clinic.billing_service.model.Billing;
import com.clinic.billing_service.repository.BillingRepository;
import com.clinic.billing_service.service.BillingService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BillingServiceTest {

    @Mock
    private BillingRepository billingRepository;

    @Mock
    private AppointmentClient appointmentClient;

    @InjectMocks
    private BillingService billingService;

    private Billing billing;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        billing = new Billing();
        billing.setId(1L);
        billing.setAppointmentId(100L);
        billing.setAmount((double) 150);
        billing.setBillingDate(LocalDateTime.now());
        billing.setStatus("UNPAID");
    }

    /**
     * Test retrieving all billings.
     */
    @Test
    void getAllBillings_ReturnsListOfBillings() {
        when(billingRepository.findAll()).thenReturn(List.of(billing));

        List<Billing> result = billingService.getAllBillings();

        assertEquals(1, result.size());
        verify(billingRepository, times(1)).findAll();
    }

    /**
     * Test retrieving billing by valid ID.
     */
    @Test
    void getBillingById_ValidId_ReturnsBilling() {
        when(billingRepository.findById(1L)).thenReturn(Optional.of(billing));

        Billing result = billingService.getBillingById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(billingRepository, times(1)).findById(1L);
    }

    /**
     * Test retrieving billing by invalid ID throws exception.
     */
    @Test
    void getBillingById_InvalidId_ThrowsException() {
        when(billingRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(BillingNotFoundException.class, () -> billingService.getBillingById(2L));
    }

    /**
     * Test creating a billing when appointment exists.
     */
    @Test
    void createBilling_ValidAppointment_SavesBilling() {
        // Arrange
        Long appointmentId = 1L;
        Billing billing = new Billing();
        billing.setAppointmentId(appointmentId);
        billing.setAmount(150.0);
        billing.setStatus("PENDING");
        billing.setBillingDate(LocalDateTime.now());

        when(appointmentClient.getAppointmentById(appointmentId))
                .thenReturn(new AppointmentDto(appointmentId, "Doctor", "Patient", LocalDate.now(), "FINISHED"));

        when(billingRepository.save(any(Billing.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Billing savedBilling = billingService.createBilling(billing);

        assertNotNull(savedBilling);
        assertEquals(appointmentId, savedBilling.getAppointmentId());
        assertEquals("PENDING", savedBilling.getStatus());
        verify(appointmentClient).getAppointmentById(appointmentId);
        verify(billingRepository).save(billing);
    }


    /**
     * Test creating a billing when appointment does not exist throws exception.
     */
    @Test
    void createBilling_InvalidAppointment_ThrowsException() {
        doThrow(RuntimeException.class).when(appointmentClient).getAppointmentById(100L);

        assertThrows(AppointmentNotFoundException.class, () -> billingService.createBilling(billing));
    }

    /**
     * Test updating billing with valid ID.
     */
    @Test
    void updateBilling_ValidId_UpdatesBilling() {
        Billing updated = new Billing();
        updated.setAppointmentId(101L);
        updated.setAmount((double) 200);
        updated.setBillingDate(LocalDateTime.now());
        updated.setStatus("PAID");

        when(billingRepository.findById(1L)).thenReturn(Optional.of(billing));
        when(billingRepository.save(any(Billing.class))).thenReturn(billing);

        Billing result = billingService.updateBilling(1L, updated);

        assertNotNull(result);
        assertEquals("PAID", result.getStatus());
        verify(billingRepository).save(any(Billing.class));
    }

    /**
     * Test updating billing with invalid ID throws exception.
     */
    @Test
    void updateBilling_InvalidId_ThrowsException() {
        when(billingRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(BillingNotFoundException.class, () -> billingService.updateBilling(2L, billing));
    }

    /**
     * Test deleting billing with valid ID.
     */
    @Test
    void deleteBilling_ValidId_DeletesBilling() {
        when(billingRepository.existsById(1L)).thenReturn(true);

        billingService.deleteBilling(1L);

        verify(billingRepository, times(1)).deleteById(1L);
    }

    /**
     * Test deleting billing with invalid ID throws exception.
     */
    @Test
    void deleteBilling_InvalidId_ThrowsException() {
        when(billingRepository.existsById(2L)).thenReturn(false);

        assertThrows(BillingNotFoundException.class, () -> billingService.deleteBilling(2L));
    }

    /**
     * Test marking billing as paid.
     */
    @Test
    void markAsPaid_ValidId_UpdatesStatusToPaid() {
        when(billingRepository.findById(1L)).thenReturn(Optional.of(billing));
        when(billingRepository.save(any(Billing.class))).thenReturn(billing);

        Billing result = billingService.markAsPaid(1L);

        assertEquals("PAID", result.getStatus());
        verify(billingRepository).save(any(Billing.class));
    }

}
