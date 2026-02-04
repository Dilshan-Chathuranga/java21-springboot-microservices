# Java 21 Spring Boot Microservices – Order System

## Overview

This project implements a microservices-based order processing system using Java 21 and Spring Boot 3.

The system demonstrates:

- Synchronous inter-service communication
- API-key–based security
- Circuit Breaker pattern (Resilience4j)
- Saga-style compensation logic
- Proper logging
- Unit & controller-level testing
- Docker Compose–based deployment

---

## Services

| Service | Port | Responsibility |
|--------|------|----------------|
| Order Service | 8081 | Accept orders, orchestrate inventory & payment |
| Inventory Service | 8082 | Reserve and release product stock |
| Payment Service | 8083 | Process payments |

---

## Tech Stack

- Java 21
- Spring Boot 3
- Spring Web / JPA
- Resilience4j (Circuit Breaker)
- H2 (in-memory database)
- Docker & Docker Compose
- JUnit 5 + Mockito
- Lombok

---

## Project Structure

```text
java21-springboot-microservices
├── order-service
├── inventory-service
├── payment-service
├── docker-compose.yml
└── README.md
```

---

## How to Build & Run

### Prerequisites

- Java 21
- Maven 3.5+
- Docker & Docker Compose

---

### Build all services (JAR files)

From the project root directory:

```bash
mvn clean package
```

To skip tests:

```bash
mvn clean package -DskipTests
```

---

### Start services using Docker Compose

```bash
docker compose up --build
```

---

### Service URLs

| Service | URL |
|--------|-----|
| Order Service | http://localhost:8081 |
| Inventory Service | http://localhost:8082 |
| Payment Service | http://localhost:8083 |

---

### Stop services

```bash
docker compose down
```

---

## Security Model (API Key)

Each service requires an `X-API-KEY` header.

| Service | API Key |
|--------|--------|
| Order Service | order-secret-key |
| Inventory Service | inventory-secret-key |
| Payment Service | payment-secret-key |

---

## API Usage

### Create Order – SUCCESS

```http
POST http://localhost:8081/orders
```

Headers:
```text
Content-Type: application/json
X-API-KEY: order-secret-key
```

Body:
```json
{
  "productCode": "P100",
  "quantity": 1,
  "amount": 100
}
```

Response:
```json
{
  "id": 1,
  "productCode": "P100",
  "quantity": 1,
  "amount": 100,
  "status": "COMPLETED"
}
```

---

### Payment Failure → Compensation

```bash
docker compose stop payment-service
```

Response:
```json
{
  "id": 2,
  "productCode": "P100",
  "quantity": 1,
  "amount": 100,
  "status": "FAILED"
}
```

---

## Circuit Breaker

Resilience4j protects payment calls.

```yaml
slidingWindowSize: 5
minimumNumberOfCalls: 3
failureRateThreshold: 50
waitDurationInOpenState: 10s
```

Example log:
```text
CircuitBreaker 'paymentService' is OPEN and does not permit further calls
```

---

## Testing

Run all tests:

```bash
mvn test
```

---

## Logs

View logs:

```bash
docker compose logs -f order-service
docker compose logs -f inventory-service
docker compose logs -f payment-service
```

### Health Checks

Each service exposes a health endpoint:

- Order: http://localhost:8081/actuator/health
- Inventory: http://localhost:8082/actuator/health
- Payment: http://localhost:8083/actuator/health

