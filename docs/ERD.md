# Entity Relationship Diagram (ERD)

This document defines the core entities for each microservice and clearly
establishes data ownership boundaries. Each service owns its database and
no database is shared across services.

Logical relationships exist between Order → Inventory and Order → Payment, 
but no physical database relationships are enforced.
---

## Order Service

### Entity: Order

| Field        | Type        | Description |
|-------------|------------|-------------|
| id          | Long        | Primary key |
| productCode| String      | Product identifier |
| quantity    | int         | Quantity ordered |
| amount      | BigDecimal | Total order amount |
| status      | Enum       | CREATED, COMPLETED, FAILED |
| createdAt  | Timestamp  | Order creation time |

**Notes:**
- Order Service owns the complete order lifecycle.
- Status transitions are controlled only by the Order Service.
- No foreign keys to Inventory or Payment databases.

---

## Inventory Service

### Entity: Inventory

| Field              | Type   | Description |
|-------------------|--------|-------------|
| id                | Long   | Primary key |
| productCode       | String | Product identifier |
| availableQuantity | int    | Available stock quantity |

**Notes:**
- Inventory Service has no knowledge of orders or payments.
- Inventory reservation and release are local transactions.
- Compensation is triggered externally by the Order Service (Saga).

---

## Payment Service

### Entity: Payment

| Field   | Type        | Description |
|--------|-------------|-------------|
| id     | Long        | Primary key |
| orderId| Long        | Order identifier (reference only) |
| amount | BigDecimal | Payment amount |
| status | Enum       | SUCCESS, FAILED |
| createdAt | Timestamp | Payment time |

**Notes:**
- orderId is stored only for traceability.
- No foreign key constraint to Order Service database.
- Payment Service does not perform compensations.

---

## Data Ownership & Relationships

- Each service owns its database (Database-per-Service pattern).
- No cross-service joins or foreign key constraints.
- All inter-service coordination is handled via REST APIs.
- Consistency across services is achieved using the Saga pattern.

---

## Design Rationale

This ERD design ensures:
- Loose coupling between services
- Clear ownership of data
- Independent scaling and deployment
- Compatibility with Saga-based orchestration
