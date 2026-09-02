# FleetTrack — Vehicle Fleet Management System

FleetTrack is a monolithic backend platform built with Java 21 and Spring Boot 4.1.1. It provides a secure and scalable REST API for managing users, vehicle fleets, orders, and administrative operations, with JWT-based authentication and role-based authorization. The platform also supports real-time communication via WebSockets, email notifications, PostgreSQL persistence, and CSV data exports.

---

## 🚀 Key Features & Architecture

- **Stateless Authentication & RBAC**: Secured with Spring Security & JWT. Features strict Role-Based Access Control (**ADMIN** vs **FLEET_MANAGER**).
- **Real-Time GPS Streaming**: Built-in Spring WebSocket using STOMP protocol to stream live vehicle coordinates to dashboard subscribers.
- **High-Performance Redis Caching**: Redis caching applied to read-heavy queries (vehicle and driver summaries) for sub-millisecond retrieval speeds.
- **Database Migrations with Flyway**: Version-controlled relational database schema management ensuring consistent deployments across environments.
- **Dynamic Filtering & Pagination**: Spring Data JPA Criteria specifications supporting dynamic criteria, multi-field sorting, and pagination.
- **Automated Scheduling**: Cron jobs powered by Spring Scheduler to flag overdue maintenance and clean up temporary logs.
- **PDF Reporting Engine**: Native PDF document generation powered by **OpenPDF (v3.0.5)** covering Fleet Status, Maintenance History, and Driver Activity.
- **Modern Java Standards**: Java `record` types used for immutable DTO definitions alongside compile-time **MapStruct** mappers.

---

## 🛠 Tech Stack

* **Java Version**: Java 21
* **Framework**: Spring Boot 4.1.1
* **Web**: Spring MVC
* **Security**: Spring Security + JSON Web Token (JWT) + BCrypt
* **Database**: PostgreSQL
* **ORM**: Spring Data JPA / Hibernate
* **Email**: Spring Boot Mail
* **Real-Time Communication**: WebSocket
* **API Documentation**: Springdoc OpenAPI / Swagger UI
* **Build Tool**: Gradle

---
## ⚙️ Environment Variables & Setup

The application uses environment variables for environment independence. An `.env.example` file is provided in the repository root.

### 1. Configure `.env` File

Copy `.env.example` to create your local `.env` file:

    cp .env.example .env

Ensure your `.env` contains the following parameters:

    # Server Configuration
    SERVER_PORT=8080

    # Database Configuration
    POSTGRES_DB=fleettrack_db
    POSTGRES_USER=postgres
    POSTGRES_PASSWORD=your-password
    DB_PORT=5432

    # Redis Configuration
    REDIS_HOST=localhost
    REDIS_PORT=6379

    # JWT Security Settings
    JWT_SECRET=your-secret-key
    JWT_EXPIRATION=3600000

---

## 🔐 Security & Role-Based Access Control (RBAC)

Authentication is completely stateless via JWT Bearer tokens. Request boundaries are defined as follows:

| Endpoint Pattern | Http Method | Permitted Roles | Description |
| :--- | :--- | :--- | :--- |
| `/api/v1/auth/**` | `POST` | `PermitAll` | Public user registration & login |
| `/ws/**`, `/ws` | `ALL` | `PermitAll` | WebSocket STOMP handshake endpoints |
| `/swagger-ui/**`, `/api-docs/**` | `GET` | `PermitAll` | OpenAPI interactive documentation |
| `/api/v1/reports/**` | `GET` | `ADMIN`, `FLEET_MANAGER` | Export system metrics to PDF |
| `/api/v1/**` | `GET`, `POST`, `PUT` | `ADMIN`, `FLEET_MANAGER` | Standard fleet operations |
| `/api/v1/**` | `DELETE` | `ADMIN` | Destructive operations restricted to Admin |

---

## 📡 Real-Time GPS Tracking (WebSocket STOMP)

Location tracking is implemented through WebSocket STOMP topics:

- **Connection Endpoint**: `ws://localhost:8080/ws`
- **Publish Coordinates**: `/app/vehicles/{vehicleId}/location`
- **Subscribe to Vehicle Stream**: `/topic/vehicles/{vehicleId}/location`

---

## 📑 API Endpoint Reference

### 🔑 Authentication (`/api/v1/auth`)
- `POST /register`: Register a new user account.
- `POST /login`: Authenticate credentials and acquire a JWT token.

### 🚗 Vehicle Management (`/api/v1/vehicles`)
- `POST /`: Create a new vehicle.
- `GET /`: Retrieve paginated and filtered list of vehicles.
- `GET /{id}`: Retrieve a single vehicle by ID.
- `PUT /{id}`: Update vehicle properties and operational status.
- `DELETE /{id}`: Delete a vehicle (`ADMIN` only).
- `GET /{vehicleId}/location`: Get the latest stored GPS location for a vehicle.

### 👤 Driver Profiles (`/api/v1/drivers`)
- `POST /`: Register a new driver profile.
- `GET /`: Get paginated drivers (filterable by first name, last name, or email).
- `GET /{id}`: Get driver details by ID.
- `PUT /{id}`: Update driver information.
- `DELETE /{id}`: Delete driver profile (`ADMIN` only).

### 🤝 Vehicle Assignments (`/api/v1/vehicle-assignments`)
- `POST /`: Assign a vehicle to a driver.
- `GET /{id}`: Fetch assignment record details.
- `PATCH /{id}/unassign`: Unassign vehicle from driver.

### 🛠 Maintenance Records (`/api/v1/maintenance-records`)
- `POST /`: Create a new maintenance record.
- `GET /{id}`: Fetch maintenance record by ID.
- `GET /vehicle/{vehicleId}`: Fetch paginated maintenance history for a specific vehicle.
- `PUT /{id}`: Update maintenance information and status (`SCHEDULED`, `COMPLETED`, `OVERDUE`).
- `DELETE /{id}`: Delete a maintenance log (`ADMIN` only).

### 📄 PDF Reports (`/api/v1/reports`)
- `GET /fleet-status`: Export fleet breakdown and active driver allocations as a PDF.
- `GET /maintenance`: Export maintenance records, total expenditures, and average service costs as a PDF.
- `GET /driver-activity`: Export driver details, license info, and current vehicle assignments as a PDF.

---

## 🧪 Interactive OpenAPI Documentation

Start the Spring Boot application and navigate to the Swagger UI in your browser:

    http://localhost:8080/swagger-ui.html
