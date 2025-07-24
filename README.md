# SpringCloudMS-DIS-project

Ovaj sistem je dizajniran kao mikroservisna arhitektura namenjena za upravljanje klinikama i zdravstvenim uslugama. Svaki mikroservis ima jasno definisanu odgovornost i koristi sopstvenu bazu podataka. Komunikacija između servisa obavlja se sinhrono (putem REST i Feign klijenata) i asinhrono (putem RabbitMQ poruka).

### Mikroservisi:

| Mikroservis         | Opis                                                                 |
|---------------------|----------------------------------------------------------------------|
| `api-gateway`       | Ulazna tačka u sistem, koristi Spring Cloud Gateway za rutiranje     |
| `eureka-service`    | Service Discovery server, omogućava registrovanje i pronalaženje servisa |
| `config-service`    | Centralizovani server za konfiguraciju mikroservisa (Spring Cloud Config) |
| `doctor-service`    | Operacije nad doktorima                                          |
| `patient-service`   | Operacije nad pacijentima                                        |
| `appointment-service` | Zakazivanje, izmena i praćenje termina između pacijenata i doktora   |
| `billing-service`   | Generisanje računa po završetku termina, koristi RabbitMQ za eventove |
