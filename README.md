# SpringCloudMS-DIS-project

Ovaj sistem je dizajniran kao mikroservisna arhitektura namenjena za upravljanje klinikama i zdravstvenim uslugama. Svaki mikroservis ima jasno definisanu odgovornost i koristi sopstvenu bazu podataka. Komunikacija između servisa obavlja se sinhrono (putem REST i Feign klijenata) i asinhrono (putem RabbitMQ poruka).

## Tehnologije i alati

- Java 17
- Spring Boot
- Spring Cloud (Eureka, Config Server, OpenFeign, Gateway)
- RabbitMQ (za asinhronu komunikaciju)
- Docker (za kontejnerizaciju)
- Maven
- Microsoft SQL

### Mikroservisi:

| Mikroservis          | Port  | Opis |
|----------------------|-------|------|
| `eureka-service`     | 8761  | Service Discovery server, omogućava registrovanje i pronalaženje servisa |
| `config-service`     | 8888  | Centralizovani server za konfiguraciju mikroservisa (Spring Cloud Config) |
| `api-gateway`        | 8080  | API ulazna tačka |
| `patient-service`    | 8082  | Operacije nad pacijentima |
| `doctor-service`     | 8083  | Operacije nad doktorima |
| `appointment-service`| 8084  | Zakazivanje, izmena i praćenje termina između pacijenata i doktora (sync i async) |
| `billing-service`    | 8085  | Generisanje računa po završetku termina, RabbitMQ za eventove |
