package com.clinic.billing_service.listener;

import java.time.LocalDateTime;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.clinic.billing_service.event.AppointmentFinishedEvent;
import com.clinic.billing_service.model.Billing;
import com.clinic.billing_service.repository.BillingRepository;

@Component
public class AppointmentFinishedEventListener {

	private final BillingRepository billingRepository;

	public AppointmentFinishedEventListener(BillingRepository billingRepository) {
		this.billingRepository = billingRepository;
	}

	@RabbitListener(queues = "${rabbitmq.queue}")
	public void handleAppointmentFinishedEvent(AppointmentFinishedEvent event) {
		try {
			Billing billing = new Billing();
			billing.setAppointmentId(event.getAppointmentId());
			billing.setAmount(2500.0);
			billing.setBillingDate(LocalDateTime.now());

			billingRepository.save(billing);
		} catch (Exception e) {
			System.err.println("Error creating bill: " + e.getMessage());
		}
	}

}
