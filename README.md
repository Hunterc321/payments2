# Payment Transactions REST API

## Overview
REST API for managing payment transactions in a bank account. This API allows for listing payments, triggering payments, and deleting payments. The project is implemented in Java using Spring Boot and H2 as an in-memory database. Additionally, the service is containerized using Docker.

### 1. Basic REST Endpoints
- **GET /payments**: List all payments.
- **POST /payments**: Create a new payment.
- **DELETE /payments/{id}**: Delete a payment by ID.

### 2. Database
-  **H2** as the in-memory database.
- `payment` table with the following columns:
    - `id` (Long, primary key)
    - `amount` (Decimal)
    - `currency` (String)
    - `fromAccount` (String)
    - `toAccount` (String)
    - `timestamp` (Timestamp)
  ####
- `payment_history` table with the following columns:
    - `id` (Long, primary key)
    - `payment_id` (Long)
    - `action` (String)
    - `timestamp` (Timestamp)
  ####
-  **Liquibase** as the database schema change management solution.
  


### 3. Implementation Details
-  Implement basic REST endpoints (GET, POST, DELETE).
-  Validate request payloads using SPRING's build in validation library
- Add exception handling by creating a global exception handler using ControllerAdvice.
- Implement basic unit tests using JUnit and MockMvc.
- Implement pagination for the GET `/payments` endpoint.
- Add filtering capabilities for  currency and amount.
- Integrate logging by using AOP and slf4j.
- Ensure concurrency control by making the requests transactions.
- Implement transaction history by creating `payment_history` table that tracks the changes made in the `payment` table.
- Secure the endpoints using Spring Security and JWT (JSON Web Token) to ensure only authenticated users can access/edit data.

### 4. Containerization
- Docker image for the application.

## Getting Started

### Prerequisites
- Java 21
- Maven
- Docker

### Running the Application Locally

1. **Clone the Repository**
   ```bash
   git clone https://github.com/Hunterc321/payments2.git
####
1. **Running the application inside Docker**
    ```bash
   docker build -t payments-app .
    docker run --name payments-container -p 8080:8080 payments-app

