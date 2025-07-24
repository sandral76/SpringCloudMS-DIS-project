package com.clinic.appointment_service.messaging;

import com.clinic.appointment_service.event.AppointmentFinishedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BillingMessageProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.routing-key}")
    private String routingKey;

    public BillingMessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendAppointmentFinishedEvent(AppointmentFinishedEvent event) {
        rabbitTemplate.convertAndSend(exchange, routingKey, event);
    }

}
