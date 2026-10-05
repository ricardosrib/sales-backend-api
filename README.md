# Sales System

A lightweight Spring Boot sales and budgeting API built with Java 17, Spring Data JPA, and an in-memory H2 database.

## Overview

This project models a simple sales workflow with product catalog management, stock tracking, budget (quote) creation, and budget confirmation.

The application exposes REST endpoints for:
- listing all products
- listing available products with stock
- retrieving product details and stock quantities
- retrieving customer information
- creating a budget (quote)
- confirming a budget and deducting stock
- updating stock quantities

## Architecture

The project follows a clean separation between layers:

- `controller` handles HTTP requests and routes them to use case classes
- `usecase` contains application use cases for business operations
- `domain.services` contains core business logic for sales and stock management
- `repository` contains JPA repositories, custom repository interfaces, and entity mappings
- `domain.model` contains in-memory domain model classes used by the service layer
- `config.DataInitializer` seeds initial data into the H2 database at startup

## Key Packages

- `com.example.sales_system.controller`
- `com.example.sales_system.usecase`
- `com.example.sales_system.domain.services`
- `com.example.sales_system.repository`
- `com.example.sales_system.domain.model`
- `com.example.sales_system.exception`

## Getting Started

### Requirements

- Java 17
- Maven

### Build and Run

```bash
cd /workspaces/sales-system
mvn clean package
mvn spring-boot:run
```

The app starts on the default Spring Boot port `8080`.

## Seeded Data

On startup, the application seeds the database with initial entities if none exist:

- Products:
  - `1` - Laptop (`1200.0`)
  - `2` - Mouse (`25.0`)
- Stock:
  - Product `1`: quantity `10`, minimum `1`, maximum `100`
  - Product `2`: quantity `50`, minimum `5`, maximum `200`
- Customer:
  - `1` - Acme Corporation

## API Endpoints

### Products

- `GET /api/products`
  - Returns all products

- `GET /api/products/available`
  - Returns only products with stock available

- `GET /api/products/{id}`
  - Returns a specific product by ID

- `GET /api/products/{id}/stock`
  - Returns current stock quantity for a product

### Customers

- `GET /api/customers/{id}`
  - Returns a customer by ID

### Budgets

- `GET /api/budgets?page=0&size=20`
  - Lists budgets newest first. Optional filters: `customerId`, `finalized`, `fromDate`, and `toDate` (`YYYY-MM-DD`). The response includes `content`, `page`, `size`, `totalElements`, and `totalPages`.
- `GET /api/customers/{id}/budgets?page=0&size=20`
  - Lists a customer's budgets, newest first.
- `DELETE /api/budgets/{id}`
  - Deletes an active, unconfirmed budget.
- `POST /api/budgets/{id}/cancel`
  - Cancels an active, unconfirmed budget. Cancelled budgets cannot be confirmed.
- `POST /api/budgets/{id}/duplicate`
  - Creates a new budget for the same customer and items, using current product prices.

- `POST /api/budgets`
  - Creates a new budget (quote)
  - Request body example:

```json
{
  "customerId": 1,
  "items": [
    { "productId": 1, "quantity": 2 },
    { "productId": 2, "quantity": 3 }
  ]
}
```

- `GET /api/budgets/{id}`
  - Retrieves a budget by ID

- `POST /api/budgets/{id}/confirm`
  - Confirms a budget, validates stock availability, deducts stock, and finalizes the budget

### Stock Management

- `GET /api/stocks`
  - Lists stock records with quantities and stock thresholds.
- `GET /api/stocks/low`
  - Lists stock records at or below their minimum stock level.

- `PUT /api/stocks/{id}?quantity={quantity}`
  - Updates stock quantity for the given product ID
  - The service caps quantity at the configured maximum stock level

## Error Handling

The application uses a global exception handler to return standardized `application/problem+json` responses for:

- `404 Not Found`
- `400 Bad Request`
- validation errors
- malformed JSON
- generic server errors

## Notes

- Data is stored in an H2 runtime database.
- The current configuration only seeds initial data and does not expose an H2 console.
- Budget confirmation verifies stock quantity and rejects confirmation when the budget has expired or the budget is already finalized.

## Build Info

- Spring Boot `3.5.3`
- Java `17`
- Dependencies:
  - `spring-boot-starter-web`
  - `spring-boot-starter-data-jpa`
  - `spring-boot-starter-validation`
  - `h2`
  - `spring-boot-starter-test` (test scope)

## Running Tests

```bash
mvn test
```
