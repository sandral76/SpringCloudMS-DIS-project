package com.clinic.billing_service.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.clinic.billing_service.service.EmailService;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Component
public class CircuitBreakerEventListener {

	private static final Logger log = LoggerFactory.getLogger(CircuitBreakerEventListener.class);
	private final CircuitBreakerRegistry circuitBreakerRegistry;
    private final EmailService emailService;
    private final Environment env;

    
    @Autowired
    public CircuitBreakerEventListener(CircuitBreakerRegistry registry, EmailService emailService,Environment env) {
        this.circuitBreakerRegistry = registry;
        this.emailService = emailService;
        this.env = env;

    }
    @PostConstruct
    public void registerEventListeners() {
        CircuitBreakerRegistry registry = CircuitBreakerRegistry.ofDefaults();

        registry.getAllCircuitBreakers().forEach(cb -> {
            cb.getEventPublisher()
              .onStateTransition(event -> {
                  if (event.getStateTransition().getToState().name().equals("OPEN")) {
                      String subject = "Circuit Breaker Opened: " + cb.getName();
                      String body = "Circuit breaker " + cb.getName() + " transitioned to OPEN state.";
                      emailService.sendEmail((env.getProperty("spring.mail.username")), subject, body);
                  }
              });
        });
    }
}
