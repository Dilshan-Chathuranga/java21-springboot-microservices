# Sequence Diagrams

## Successful Order Creation (Saga Success Flow)

Client → Order Service: POST /orders  
Order Service → Order Service: Create Order (status = CREATED)

Order Service (Saga Orchestrator) → Inventory Service: Reserve Inventory  
Inventory Service → Order Service: Reservation Successful  

Order Service → Payment Service: Process Payment  
Payment Service → Order Service: Payment Successful  

Order Service → Order Service: Mark Order COMPLETED  
Order Service → Client: Order Created Successfully  

---

## Order Failure – Inventory Unavailable (Saga Aborted Early)

Client → Order Service: POST /orders  
Order Service → Order Service: Create Order (status = CREATED)

Order Service (Saga Orchestrator) → Inventory Service: Reserve Inventory  
Inventory Service → Order Service: Reservation Failed  

Order Service → Order Service: Mark Order FAILED  
Order Service → Client: Order Failed (Inventory Unavailable)  

---

## Order Failure – Payment Failed (Saga Compensation Flow)

Client → Order Service: POST /orders  
Order Service → Order Service: Create Order (status = CREATED)

Order Service (Saga Orchestrator) → Inventory Service: Reserve Inventory  
Inventory Service → Order Service: Reservation Successful  

Order Service → Payment Service: Process Payment  
Payment Service → Order Service: Payment Failed  

Order Service → Inventory Service: Release Inventory (Compensating Action)  
Order Service → Order Service: Mark Order FAILED  

Order Service → Client: Order Failed (Payment Failure)  

Order Service → Payment Service: Process Payment (via Circuit Breaker)
Payment Service → Order Service: Failure / Timeout / Circuit Open

