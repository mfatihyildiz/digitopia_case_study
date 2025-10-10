# Digitopia Case Study - Microservices Architecture

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2024.0.0-blue.svg)](https://spring.io/projects/spring-cloud)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue.svg)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-Compose-blue.svg)](https://docs.docker.com/compose/)

> **A comprehensive microservices-based system for managing users, organizations, and invitations using Spring Boot, PostgreSQL, and Docker.**

## Table of Contents

- [Project Overview](#-project-overview)
- [Architecture](#-architecture)
- [Technology Stack](#-technology-stack)
- [Quick Start](#-quick-start)
- [Data Model](#-data-model)
- [API Endpoints](#-api-endpoints)
- [Business Rules](#-business-rules)
- [Design Decisions](#-design-decisions)
- [Security Features](#-security-features)
- [Testing](#-testing)
- [Documentation](#-documentation)
- [License](#-license)

## Project Overview

This project implements a **microservices-based system** for the Digitopia case study, demonstrating advanced backend development skills with Spring Boot. The system manages three core entities: **Users**, **Organizations**, and **Invitations**, with complex business logic, validation, and inter-service communication.

### Key Features

- **Microservices Architecture** with service discovery
- **Complete CRUD Operations** for all entities
- **Comprehensive Input Validation** and sanitization
- **Advanced Search Capabilities** with pagination
- **Inter-Service Communication** with error handling
- **Scheduled Jobs** for business logic automation
- **Audit Trail** with user tracking
- **Health Monitoring** for all services
- **Docker Containerization** for easy deployment

### Case Study Requirements Coverage

| Requirement | Status | Implementation |
|-------------|--------|----------------|
| Microservices Architecture | **Complete** | 3 services + API Gateway + Discovery |
| CRUD Operations | **Complete** | All entities with full CRUD |
| Input Sanitization | **Complete** | XSS prevention, data validation |
| Database Indexing | **Complete** | Optimized search performance |
| Health Endpoints | **Complete** | `/healthz` for all services |
| Business Logic | **Complete** | All constraints implemented |
| Inter-Service Communication | **Complete** | REST calls with error handling |
| Documentation | **Complete** | Comprehensive docs included |

## Architecture

### System Architecture Diagram

```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   Client App    │    │   Swagger UI     │    │   Monitoring    │
└─────────┬───────┘    └─────────┬────────┘    └─────────┬───────┘
          │                      │                       │
          └──────────────────────┼───────────────────────┘
                                 │
                    ┌────────────▼────────────┐
                    │     API Gateway         │
                    │   (Port: 8080)          │
                    └────────────┬────────────┘
                                 │
                    ┌────────────▼────────────┐
                    │   Discovery Service     │
                    │   (Eureka - 8761)       │
                    └────────────┬────────────┘
                                 │
        ┌────────────────────────┼────────────────────────┐
        │                       │                        │
┌───────▼────────┐    ┌─────────▼─────────┐    ┌────────▼────────┐
│  User Service  │    │Organization Service│    │Invitation Service│
│   (Port: 8082) │    │   (Port: 8083)     │    │   (Port: 8084)   │
└─────────┬──────┘    └─────────┬──────────┘    └────────┬─────────┘
          │                     │                       │
┌─────────▼──────┐    ┌─────────▼──────────┐    ┌────────▼─────────┐
│  User Database │    │Organization Database│    │Invitation Database│
│  (PostgreSQL)  │    │   (PostgreSQL)      │    │   (PostgreSQL)   │
└────────────────┘    └────────────────────┘    └──────────────────┘
```

### Service Responsibilities

| Service | Responsibility | Database | Port |
|---------|---------------|----------|------|
| **API Gateway** | Request routing, load balancing | - | 8080 |
| **Discovery Service** | Service registration, health monitoring | - | 8761 |
| **User Service** | User management, authentication data | `user_db` | 8082 |
| **Organization Service** | Organization management, memberships | `organization_db` | 8083 |
| **Invitation Service** | Invitation workflow, expiration logic | `invitation_db` | 8084 |

## Technology Stack

### Backend Technologies
- **Java 17** - Modern Java features and performance
- **Spring Boot 3.3.4** - Rapid application development framework
- **Spring Cloud 2024.0.0** - Microservices ecosystem
- **Spring Data JPA** - Data persistence layer
- **Spring Web** - RESTful web services
- **Spring Validation** - Input validation framework

### Database & Storage
- **PostgreSQL 15** - Relational database
- **Hibernate** - ORM framework
- **Database-per-Service** - Data isolation pattern

### Infrastructure & DevOps
- **Docker** - Containerization platform
- **Docker Compose** - Multi-container orchestration
- **Maven** - Build automation and dependency management
- **Eureka** - Service discovery and registration

### Development Tools
- **Lombok** - Boilerplate code reduction
- **Spring Actuator** - Production-ready features
- **Swagger/OpenAPI** - API documentation
- **Spring Boot DevTools** - Development productivity

## Quick Start

### Prerequisites

Before running the application, ensure you have the following installed:

- **Docker** (version 20.10+)
- **Docker Compose** (version 2.0+)
- **Java 17+** (for local development)
- **Maven 3.8+** (for local development)
- **Git** (for cloning the repository)

### Installation & Setup

1. **Clone the Repository**
   ```bash
   git clone https://github.com/your-username/digitopia-case-study.git
   cd digitopia-case-study
   ```

2. **Start All Services with Docker Compose**
   ```bash
   # Build and start all services
   docker-compose up --build
   
   # Or run in detached mode
   docker-compose up --build -d
   ```

3. **Verify Services are Running**
   ```bash
   # Check service status
   docker-compose ps
   
   # View logs
   docker-compose logs -f
   ```

### Service Access Points

| Service | URL | Description |
|---------|-----|-------------|
| **API Gateway** | http://localhost:8080 | Main entry point |
| **Discovery Service** | http://localhost:8761 | Eureka dashboard |
| **Swagger UI** | http://localhost:8080/swagger-ui.html | API documentation |
| **User Service** | http://localhost:8082 | Direct service access |
| **Organization Service** | http://localhost:8083 | Direct service access |
| **Invitation Service** | http://localhost:8084 | Direct service access |

### Health Check Endpoints

All services provide health monitoring:

```bash
# Check individual service health
curl http://localhost:8082/healthz  # User Service
curl http://localhost:8083/healthz  # Organization Service  
curl http://localhost:8084/healthz  # Invitation Service
curl http://localhost:8080/actuator/health  # API Gateway
```

### Local Development Setup

For local development without Docker:

1. **Start PostgreSQL instances**
   ```bash
   # Start PostgreSQL containers only
   docker-compose up postgres-user postgres-org postgres-inv -d
   ```

2. **Run services individually**
   ```bash
   # Terminal 1 - Discovery Service
   cd discovery-service && ./mvnw spring-boot:run
   
   # Terminal 2 - User Service  
   cd user-service && ./mvnw spring-boot:run
   
   # Terminal 3 - Organization Service
   cd organization-service && ./mvnw spring-boot:run
   
   # Terminal 4 - Invitation Service
   cd invitation-service && ./mvnw spring-boot:run
   
   # Terminal 5 - API Gateway
   cd api-gateway && ./mvnw spring-boot:run
   ```

## Data Model

### Entity Relationship Diagram

```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│      USER       │    │   ORGANIZATION   │    │   INVITATION    │
├─────────────────┤    ├──────────────────┤    ├─────────────────┤
│ id: UUID        │    │ id: UUID         │    │ id: UUID        │
│ email: String   │    │ registryNumber   │    │ userId: UUID    │
│ fullName: String│    │ organizationName │    │ organizationId  │
│ normalizedName  │    │ normalizedName   │    │ invitationMsg   │
│ role: Enum      │    │ contactEmail     │    │ status: Enum    │
│ status: Enum    │    │ companySize      │    │ expirationDate  │
│ createdAt       │    │ yearFounded      │    │ createdAt       │
│ updatedAt       │    │ createdAt        │    │ updatedAt       │
│ createdBy       │    │ updatedAt        │    │ createdBy       │
│ updatedBy       │    │ createdBy        │    │ updatedBy       │
└─────────┬───────┘    │ updatedBy        │    └─────────────────┘
          │             └─────────┬────────┘             │
          │                       │                      │
          │                       │                      │
          └───────────────────────┼──────────────────────┘
                                  │
                        ┌─────────▼─────────┐
                        │ ORGANIZATION_MEMBER│
                        ├───────────────────┤
                        │ id: UUID          │
                        │ organizationId    │
                        │ userId            │
                        │ createdAt         │
                        │ updatedAt         │
                        │ createdBy         │
                        │ updatedBy         │
                        └───────────────────┘
```

### User Entity

```java
@Entity
@Table(name = "users")
public class User {
    @Id
    private UUID id;                    // Primary key
    private String email;               // Unique email address
    private String fullName;            // Letters and spaces only
    private String normalizedName;      // Auto-generated for search
    private UserRole role;              // ADMIN, MANAGER, USER
    private UserStatus status;          // ACTIVE, PENDING, DEACTIVATED, DELETED
    private LocalDateTime createdAt;    // Creation timestamp
    private LocalDateTime updatedAt;    // Last update timestamp
    private UUID createdBy;             // Creator user ID
    private UUID updatedBy;             // Last updater user ID
}
```

**Validation Rules:**
- Email must be unique across all users (except DELETED status)
- Full name can only contain letters and spaces
- Normalized name is auto-generated for search functionality

### Organization Entity

```java
@Entity
@Table(name = "organizations")
public class Organization {
    @Id
    private UUID id;                           // Primary key
    private String registryNumber;             // Unique identifier
    private String organizationName;           // Alphanumeric + spaces + hyphens
    private String normalizedOrganizationName; // Auto-generated for search
    private String contactEmail;               // Valid email format
    private Integer companySize;               // Member limit
    private Integer yearFounded;               // 1800-2100
    private LocalDateTime createdAt;           // Creation timestamp
    private LocalDateTime updatedAt;           // Last update timestamp
    private UUID createdBy;                    // Creator user ID
    private UUID updatedBy;                    // Last updater user ID
}
```

**Validation Rules:**
- Registry number must be unique
- Organization name: 2-100 characters, alphanumeric + spaces + hyphens
- Company size: positive integer (minimum 1)
- Year founded: integer between 1800-2100

### Invitation Entity

```java
@Entity
@Table(name = "invitations")
public class Invitation {
    @Id
    private UUID id;                    // Primary key
    private UUID userId;                // Reference to user
    private UUID organizationId;        // Reference to organization
    private String invitationMessage;   // Max 250 characters
    private InvitationStatus status;    // ACCEPTED, REJECTED, PENDING, EXPIRED
    private LocalDateTime expirationDate; // Auto-set to 7 days
    private LocalDateTime createdAt;    // Creation timestamp
    private LocalDateTime updatedAt;    // Last update timestamp
    private UUID createdBy;             // Creator user ID
    private UUID updatedBy;             // Last updater user ID
}
```

**Business Rules:**
- Only one pending invitation per user per organization
- Invitations expire after 7 days
- Users cannot be reinvited if their last invitation was rejected
- Users can be reinvited if their invitation expired

## API Endpoints

### User Service Endpoints (`/api/users`)

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| `GET` | `/api/users` | List all users | - | `List<User>` |
| `GET` | `/api/users/{id}` | Get user by ID | - | `User` |
| `POST` | `/api/users` | Create new user | `User` | `User` (201) |
| `PUT` | `/api/users/{id}` | Update user | `User` | `User` |
| `DELETE` | `/api/users/{id}` | Delete user | - | `204 No Content` |
| `GET` | `/api/users/by-email` | Get user by email | `?email=` | `User` |
| `GET` | `/api/users/search-by-normalized-name` | Search by name | `?name=&page=&size=` | `List<User>` |
| `GET` | `/api/users/{userId}/organizations` | Get user's organizations | - | `List<Organization>` |
| `GET` | `/api/users/status/{status}` | Get users by status | - | `List<User>` |
| `GET` | `/api/users/role/{role}` | Get users by role | - | `List<User>` |
| `PATCH` | `/api/users/{id}/status` | Update user status | `?status=` | `User` |

**Example Requests:**

```bash
# Create a new user
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -H "X-User-Id: admin-uuid" \
  -d '{
    "email": "john.doe@example.com",
    "fullName": "John Doe",
    "role": "USER",
    "status": "PENDING"
  }'

# Search users by normalized name
curl "http://localhost:8080/api/users/search-by-normalized-name?name=john&page=0&size=10"

# Get user's organizations
curl http://localhost:8080/api/users/user-uuid/organizations
```

### Organization Service Endpoints (`/api/organizations`)

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| `GET` | `/api/organizations` | List all organizations | - | `List<Organization>` |
| `GET` | `/api/organizations/{id}` | Get organization by ID | - | `Organization` |
| `POST` | `/api/organizations` | Create new organization | `Organization` | `Organization` (201) |
| `PUT` | `/api/organizations/{id}` | Update organization | `Organization` | `Organization` |
| `DELETE` | `/api/organizations/{id}` | Delete organization | - | `204 No Content` |
| `GET` | `/api/organizations/by-registry/{registryNumber}` | Get by registry number | - | `Organization` |
| `GET` | `/api/organizations/search-advanced` | Advanced search | `?name=&year=&companySize=` | `List<Organization>` |
| `GET` | `/api/organizations/{orgId}/members` | List organization members | - | `List<UUID>` |
| `POST` | `/api/organizations/{orgId}/members` | Add member | `?userId=` | `204 No Content` |
| `DELETE` | `/api/organizations/{orgId}/members/{userId}` | Remove member | - | `204 No Content` |
| `GET` | `/api/organizations/{orgId}/capacity-info` | Get capacity information | - | `CapacityInfo` |

**Example Requests:**

```bash
# Create a new organization
curl -X POST http://localhost:8080/api/organizations \
  -H "Content-Type: application/json" \
  -H "X-User-Id: admin-uuid" \
  -d '{
    "registryNumber": "ORG-2024-001",
    "organizationName": "Digitopia Solutions Inc",
    "contactEmail": "contact@digitopiasolutions.com",
    "companySize": 50,
    "yearFounded": 2020
  }'

# Advanced organization search
curl "http://localhost:8080/api/organizations/search-advanced?name=digitopia&year=2020&companySize=50"

# Get organization capacity info
curl http://localhost:8080/api/organizations/org-uuid/capacity-info
```

### Invitation Service Endpoints (`/api/invitations`)

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| `GET` | `/api/invitations` | List all invitations | - | `List<Invitation>` |
| `GET` | `/api/invitations/{id}` | Get invitation by ID | - | `Invitation` |
| `POST` | `/api/invitations` | Create new invitation | `Invitation` | `Invitation` (201) |
| `PUT` | `/api/invitations/{id}/status` | Update invitation status | `?status=` | `Invitation` |
| `DELETE` | `/api/invitations/{id}` | Delete invitation | - | `204 No Content` |
| `GET` | `/api/invitations/user/{userId}` | Get invitations for user | - | `List<Invitation>` |
| `GET` | `/api/invitations/organization/{orgId}` | Get invitations for organization | - | `List<Invitation>` |
| `POST` | `/api/invitations/expire` | Manually expire old invitations | - | `204 No Content` |

**Example Requests:**

```bash
# Create a new invitation
curl -X POST http://localhost:8080/api/invitations \
  -H "Content-Type: application/json" \
  -H "X-User-Id: admin-uuid" \
  -d '{
    "userId": "user-uuid",
    "organizationId": "org-uuid",
    "invitationMessage": "You are invited to join our amazing team!"
  }'

# Accept an invitation
curl -X PUT "http://localhost:8080/api/invitations/invitation-uuid/status?status=ACCEPTED"

# Get user's invitations
curl http://localhost:8080/api/invitations/user/user-uuid
```

## Business Rules

### User Management Rules

1. **Email Uniqueness**
   - Email addresses must be unique across all users
   - Exception: Users with DELETED status don't block email reuse
   - Email validation follows RFC standards

2. **Name Validation**
   - Full names can only contain letters and spaces
   - No numbers, special characters, or symbols allowed
   - Normalized names are auto-generated for search functionality

3. **Status Management**
   - Users start with PENDING status by default
   - Status transitions: PENDING → ACTIVE/DEACTIVATED/DELETED
   - Only ADMIN users can directly create ACTIVE users

4. **Multi-Organization Support**
   - Users can belong to multiple organizations
   - Organization membership is managed through invitations
   - Users can have different roles in different organizations

### Organization Management Rules

1. **Registry Number Uniqueness**
   - Registry numbers must be unique across all organizations
   - Format: Alphanumeric characters, uppercase, with hyphens allowed
   - Used for legal identification and compliance

2. **Capacity Management**
   - Company size defines the maximum number of members
   - New members cannot be added if capacity is reached
   - Capacity enforcement is transactional and consistent

3. **Name Normalization**
   - Organization names support alphanumeric characters, spaces, and hyphens
   - Normalized names are auto-generated for search functionality
   - Case-insensitive and accent-insensitive searching

### Invitation Management Rules

1. **Invitation Constraints**
   - Only one pending invitation per user per organization
   - Users cannot be reinvited if their last invitation was rejected
   - Users can be reinvited if their invitation expired

2. **Expiration Logic**
   - Invitations automatically expire after 7 days
   - Daily scheduled job updates expired invitation statuses
   - Manual expiration endpoint available for testing

3. **Acceptance Workflow**
   - When accepted, invitations automatically add users to organizations
   - Capacity validation occurs before status change
   - Failed acceptance keeps invitation in PENDING status

4. **Message Validation**
   - Invitation messages are limited to 250 characters
   - HTML/XML characters are sanitized for security
   - Messages are required for all invitations

## Design Decisions

### Architecture Decisions

#### 1. Microservices Architecture
**Decision**: Implemented separate microservices for each domain.

**Rationale**:
- Single responsibility principle
- Independent scaling and deployment
- Clear service boundaries
- Team autonomy support

#### 2. Database Per Service
**Decision**: Each microservice has its own PostgreSQL database.

**Rationale**:
- Data isolation and service independence
- Independent schema evolution
- Fault isolation
- Performance optimization

#### 3. Service Discovery with Eureka
**Decision**: Used Spring Cloud Eureka for service discovery.

**Rationale**:
- Dynamic service registration
- Health monitoring
- Load balancing support
- Simplified inter-service communication

### Data Model Decisions

#### 1. UUID Primary Keys
**Decision**: Used UUID for all entity primary keys.

**Rationale**:
- Globally unique across distributed systems
- No collision risk
- Offline entity generation support
- Eventual consistency compatibility

#### 2. Audit Fields Pattern
**Decision**: Added createdBy, updatedBy, createdAt, updatedAt to all entities.

**Rationale**:
- Complete audit trail for compliance
- Change tracking and debugging
- Accountability and security
- Enterprise data management best practices

#### 3. Normalized Search Fields
**Decision**: Added normalized versions of name fields.

**Rationale**:
- Case-insensitive searching
- Improved search performance
- Consistent search experience
- Internationalization support

### Technical Implementation Decisions

#### 1. Input Sanitization Strategy
**Decision**: Comprehensive input sanitization for all text fields.

**Rationale**:
- XSS attack prevention
- Data consistency
- Security enhancement
- Data quality maintenance

#### 2. Error Handling Strategy
**Decision**: Global exception handlers with consistent responses.

**Rationale**:
- Consistent API error responses
- Centralized error handling
- Improved debugging
- Enhanced user experience

#### 3. Inter-Service Communication
**Decision**: Synchronous HTTP REST calls between services.

**Rationale**:
- Simple implementation
- Easy debugging
- Request/response pattern support
- Developer familiarity

## Security Features

### Input Validation & Sanitization

1. **XSS Prevention**
   - HTML/XML character removal from all text inputs
   - Control character filtering
   - Comprehensive input sanitization utilities

2. **SQL Injection Prevention**
   - JPA/Hibernate for all database operations
   - Parameterized queries
   - No direct SQL construction

3. **Data Validation**
   - Bean validation annotations on entities
   - Custom business logic validation
   - Multi-layer validation approach

### Authentication & Authorization

1. **Request Header Authentication**
   - X-User-Id header for audit field population
   - Simple implementation for case study
   - Extensible to full JWT/OAuth2 implementation

2. **Audit Trail**
   - Complete change tracking with createdBy/updatedBy
   - Timestamp tracking with createdAt/updatedAt
   - User accountability for all operations

### Error Handling & Security

1. **Consistent Error Responses**
   - No sensitive information leakage
   - Proper HTTP status codes
   - Structured error message format

2. **Input Length Limits**
   - Invitation messages: 250 characters
   - Organization names: 2-100 characters
   - Email validation with proper format checking

## Testing

### Manual Testing via Swagger UI

The application provides comprehensive API testing through Swagger UI:

1. **Access Swagger UI**
   ```
   http://localhost:8082/swagger-ui.html
   ```

2. **Test Workflow**
   - Create users with different roles and statuses
   - Create organizations with various configurations
   - Send invitations and test acceptance/rejection flows
   - Verify business rule enforcement
   - Test search and pagination functionality

### Health Check Testing

All services provide health monitoring:

```bash
# Individual service health checks
curl http://localhost:8082/healthz  # User Service
curl http://localhost:8083/healthz  # Organization Service
curl http://localhost:8084/healthz  # Invitation Service
curl http://localhost:8080/actuator/health  # API Gateway

# Expected response format
{
  "status": "UP",
  "timestamp": "2024-01-01T10:00:00",
  "service": "OK"
}
```

### Business Logic Testing

**Test Scenarios:**

1. **User Management**
   - Email uniqueness validation
   - Name validation (letters only)
   - Status transitions
   - Multi-organization membership

2. **Organization Management**
   - Registry number uniqueness
   - Capacity enforcement
   - Member management
   - Search functionality

3. **Invitation Management**
   - Single pending invitation constraint
   - Rejection prevention logic
   - Expiration handling
   - Acceptance workflow

### Load Testing

For basic load testing:

```bash
# Test API Gateway load balancing
for i in {1..10}; do
  curl http://localhost:8080/api/users &
done
wait
```

## Documentation

### Available Documentation

1. **[API Documentation](API_DOCUMENTATION.md)** - Comprehensive API reference
2. **[Design Decisions](DESIGN_DECISIONS.md)** - Detailed design rationale
3. **Swagger UI** - Interactive API documentation at `/swagger-ui.html`
4. **Code Comments** - Extensive inline documentation

## License

This project is part of the Digitopia Case Study and is intended for educational and evaluation purposes.

**Built with using Spring Boot, Docker, and modern microservices architecture.**
