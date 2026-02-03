# High Level Design (HLD)

## Overview
The system is a microservices-based order processing platform built using
Java 21 and Spring Boot 3.5+. It consists of three independently deployable
services: Order Service, Inventory Service, and Payment Service.

The primary goal of the system is to demonstrate clean service boundaries,
resilience, fault tolerance, and maintainable orchestration logic while
handling distributed business operations.

H2 is used for simplicity in the assignment; in production, a managed relational
database such as PostgreSQL would be used without impacting service design.

---

## Architecture
- Microservices Architecture
- Synchronous REST-based communication
- Database-per-Service pattern
- Containerized deployment using Docker and Docker Compose
- The system is eventually consistent due to Saga-based orchestration.
- All inter-service communication is secured using API-key–based authentication.

The Order Service acts as a central orchestrator that coordinates interactions
between Inventory and Payment services.

---

## Service Responsibilities

### Order Service
- Accept and validate order requests
- Act as the Saga Orchestrator
- Coordinate inventory reservation and payment processing
- Maintain the order lifecycle and state transitions
- Apply circuit breakers and handle failures gracefully

### Inventory Service
- Manage product inventory
- Validate stock availability
- Reserve inventory for orders
- Release inventory as a compensating action when required
- Remain completely unaware of orders and payments

### Payment Service
- Process payments for orders
- Explicitly return success or failure
- Maintain payment records
- No rollback logic inside the payment domain

---

## Communication Strategy
- RESTful APIs over HTTP
- JSON request/response payloads
- Synchronous request-response communication model
- Spring RestTemplate used for inter-service calls


This approach was selected to keep orchestration logic explicit and simple,
given the limited scope of the system.

---

## Resilience & Fault Tolerance
The Order Service implements the Circuit Breaker pattern using Resilience4j
for all downstream service calls.

Key benefits:
- Prevents cascading failures
- Enables fail-fast behavior when services are unavailable
- Protects system resources
- Allows downstream services time to recover

Circuit breaker fallbacks translate infrastructure-level failures into
domain-specific exceptions that can be handled by the Saga orchestrator.

---

## Saga Pattern

This system implements the **Saga Pattern using an orchestrated approach**.

The Order Service acts as the Saga Orchestrator and coordinates a sequence
of local transactions across Inventory and Payment services.

Each step in the Saga has a clearly defined compensating action:

- Inventory reservation → Inventory release on failure
- Payment failure → Order marked as FAILED

No distributed transactions (XA) are used.

A full event-driven Saga with asynchronous messaging was intentionally
not implemented due to the synchronous REST-based design and limited
scope of the assignment. This avoids over-engineering while preserving
correctness, clarity, and maintainability.

The design can be evolved into an event-driven Saga using a message broker
if system scale or complexity increases in the future.

---

## Deployment Strategy
- Each service is packaged as an independent Docker container
- One Dockerfile per service
- Docker Compose is used to orchestrate all services
- Services communicate over an internal Docker network
