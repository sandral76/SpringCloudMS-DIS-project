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

### Vrste komunikacije:
Pored sistemskih servisa, Synchronous (REST) komunikacija preko OpenFeign se koristi za direktne pozive između servisa, na primer:
- appointment-service komunicira sa patient-service i doctor-service kako bi proverio podatke o pacijentima i doktorima pre zakazivanja termina.

Asynchronous komunikacija pomoću RabbitMQ,
Kada se termin završi (appointment status = FINISHED):
- appointment-service šalje poruku na RabbitMQ,
billing-service presreće tu poruku i automatski kreira račun na osnovu podataka o završenom terminu.

### Testiranje mikroservisnog sistema

U okviru sistema sprovedeno je višeslojno testiranje koje obuhvata sledeće nivoe:

####  Unit testovi

Svaki mikroservis sadrži **unit testove** koji testiraju poslovnu logiku izolovano. Testirane su ključne metode u servisnim slojevima (`Service` klase), kao i validacije u kontrolerima i DTO klasama.

- Korišćeni alati: **JUnit 5**, **Mockito**

#### 🔗 Testiranje međuservisne komunikacije

S obzirom da mikroservisi međusobno komuniciraju, testirani su sledeći scenariji:

- **Sinhrona komunikacija preko Feign klijenata**:
  - Provereno da `appointment-service` uspešno dobavlja informacije o pacijentima i lekarima iz `patient-service` i `doctor-service`.
  - Provereno da `billing-service` može da dobije informacije o završenom terminu iz `appointment-service`.

- **Asinhrona komunikacija preko RabbitMQ**:
  - Testirano da `appointment-service` šalje poruku kada je termin završen.
  - `billing-service` je testiran da primi poruku, obradi događaj i kreira račun.

### Dockerizacija
Svi mikroservisi su dockerizovani, čime se omogućava jednostavno pokretanje i skaliranje celokupnog sistema.
Svaki servis sadrži Dockerfile i može se pokrenuti pomoću docker build i docker run komandi.
### Docker komande za pokretanje mikroservisa

|               | Komanda                                                                                                               |
|---------------------|------------------------------------------------------------------------------------------------------------------------|
|         | `docker run -d -p 8761:8761 --name eureka-server eureka-server-v1`                                                   |
|         | `docker run -d -p 8888:8888 --name config-server --link eureka-server config-server-v1`                             |
|            | `docker run -d -p 1433:1433 --name sqlserver -e "ACCEPT_EULA=Y" -e "SA_PASSWORD=Password123!" mcr.microsoft.com/mssql/server:2019-latest` |
|       | `docker run -d -p 8082:8082 --name patient-service-v3 --link eureka-server --link config-server --link sqlserver patient-service-v3` |
|        | `docker run -d -p 8083:8083 --name doctor-service-v1 --link eureka-server --link config-server --link sqlserver doctor-service-v1` |
|             | `docker run -d --hostname rabbitmq-host --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.12-management`        |
|       | `docker run -d -p 8085:8085 --name billing-service-v2 --link rabbitmq --link eureka-server --link config-server --link sqlserver billing-service-v2` |
|   | `docker run -d -p 8084:8084 --name appointment-service-v3 --link rabbitmq --link eureka-server --link config-server --link sqlserver appointment-service-v3` | |


Sistem može da se pokrene i pomoću docker-compose.yml fajla. 
### Sistem za alarmiranje - Circuit Breaker 
U okviru mikroservisne arhitekture implementiran je **circuit breaker** mehanizam korišćenjem biblioteke **Resilience4j**. Circuit breaker štiti sistem od kaskadnih grešaka i omogućava reagovanje u slučaju problema sa pojedinačnim servisima (npr. kada servis nije dostupan ili spor).
Korišćen u komunikaciji:
> - `appointment-service` i `doctor-service` / `patient-service`: jer svaki termin mora da proveri da li postoji validan lekar i pacijent. Ako neki od tih servisa ne odgovara, termin ne može biti kreiran ili ažuriran.
> - `billing-service` i `appointment-service`: jer se račun kreira samo kada je termin uspešno završen, te je neophodno proveriti status termina u `appointment-service`. Ukoliko servis nije dostupan, prekida se pokušaj povezivanja, a sistem može aktivirati fallback mehanizam ili sačekati oporavak.





