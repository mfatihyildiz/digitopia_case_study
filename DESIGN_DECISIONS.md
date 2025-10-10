# Design Decisions - Digitopia Case Study

## Overview
This document outlines the key design decisions made during the implementation of the Digitopia microservices case study.

## Architecture Decisions

### 1. Microservices Architecture
**Decision**: Implemented separate microservices for User, Organization, and Invitation management.

**Rationale**:
- Follows single responsibility principle
- Enables independent scaling and deployment
- Provides clear service boundaries
- Supports team autonomy

**Implementation**:
- User Service: Manages user entities and authentication data
- Organization Service: Manages organization entities and memberships
- Invitation Service: Manages invitation workflow and business rules

### 2. Database Per Service
**Decision**: Each microservice has its own PostgreSQL database.

**Rationale**:
- Ensures data isolation and service independence
- Prevents tight coupling between services
- Enables independent database schema evolution
- Improves fault isolation

**Implementation**:
- `user_db`: User service database
- `organization_db`: Organization service database  
- `invitation_db`: Invitation service database

### 3. Service Discovery with Eureka
**Decision**: Used Spring Cloud Eureka for service discovery.

**Rationale**:
- Simplifies inter-service communication
- Enables dynamic service registration
- Provides service health monitoring
- Supports load balancing

### 4. API Gateway Pattern
**Decision**: Implemented Spring Cloud Gateway as the entry point.

**Rationale**:
- Provides single entry point for external clients
- Enables cross-cutting concerns (logging, security, rate limiting)
- Simplifies client-side service discovery
- Supports request routing and load balancing

## Data Model Decisions

### 1. UUID as Primary Keys
**Decision**: Used UUID for all entity primary keys.

**Rationale**:
- Globally unique identifiers across distributed systems
- No collision risk in distributed environments
- Enables offline entity generation
- Supports eventual consistency patterns

### 2. Audit Fields Pattern
**Decision**: Added createdBy, updatedBy, createdAt, updatedAt to all entities.

**Rationale**:
- Provides complete audit trail for compliance
- Enables change tracking and debugging
- Supports accountability and security requirements
- Follows enterprise data management best practices

**Implementation**:
- `createdAt`: Auto-populated on entity creation
- `updatedAt`: Auto-updated on entity modification
- `createdBy`: Populated from X-User-Id request header
- `updatedBy`: Populated from X-User-Id request header

### 3. Normalized Name Fields
**Decision**: Added normalized versions of name fields for search functionality.

**Rationale**:
- Enables case-insensitive and accent-insensitive searching
- Improves search performance with indexed fields
- Provides consistent search experience
- Supports internationalization requirements

**Implementation**:
- User.normalizedName: Lowercase, alphanumeric only
- Organization.normalizedOrganizationName: Lowercase, alphanumeric only

### 4. Enum Usage for Status Fields
**Decision**: Used enums for status and role fields.

**Rationale**:
- Type safety at compile time
- Prevents invalid status values
- Improves code readability
- Enables easy status validation

## Business Logic Decisions

### 1. Email Uniqueness Constraint
**Decision**: Email addresses must be unique across all users except DELETED status.

**Rationale**:
- Prevents duplicate user accounts
- Allows soft deletion without losing email uniqueness
- Supports user reactivation scenarios
- Maintains data integrity

### 2. Invitation Expiration Strategy
**Decision**: Invitations expire after 7 days with daily cleanup job.

**Rationale**:
- Prevents indefinite pending invitations
- Reduces database clutter
- Enforces timely user responses
- Follows business requirements

**Implementation**:
- Automatic expiration date setting on invitation creation
- Daily scheduled job to mark expired invitations
- Manual expiration endpoint for testing

### 3. Organization Capacity Enforcement
**Decision**: Strict enforcement of organization member limits.

**Rationale**:
- Prevents over-subscription
- Maintains business rule compliance
- Ensures data consistency
- Supports billing and resource planning

**Implementation**:
- Validation in OrganizationMemberService.addMember()
- Transactional consistency between invitation acceptance and member addition
- Proper error handling for capacity exceeded scenarios

### 4. Invitation Rejection Prevention
**Decision**: Users cannot be reinvited if their last invitation was rejected.

**Rationale**:
- Respects user's explicit rejection decision
- Prevents spam invitations
- Maintains user privacy preferences
- Follows business requirements

## Technical Implementation Decisions

### 1. Input Sanitization Strategy
**Decision**: Comprehensive input sanitization for all text fields.

**Rationale**:
- Prevents XSS attacks
- Ensures data consistency
- Removes potentially harmful characters
- Maintains data quality

**Implementation**:
- HTML/XML character removal
- Control character filtering
- Whitespace normalization
- Service-specific sanitization rules

### 2. Error Handling Strategy
**Decision**: Global exception handlers with consistent error responses.

**Rationale**:
- Provides consistent API error responses
- Centralizes error handling logic
- Improves debugging and monitoring
- Enhances user experience

**Implementation**:
- @RestControllerAdvice for global exception handling
- Consistent JSON error response format
- Proper HTTP status codes
- Detailed error messages for debugging

### 3. Inter-Service Communication
**Decision**: Synchronous HTTP REST calls between services.

**Rationale**:
- Simple and straightforward implementation
- Easy to debug and monitor
- Supports request/response patterns
- Familiar to most developers

**Implementation**:
- Spring RestClient for HTTP calls
- Proper error handling and logging
- Circuit breaker patterns for resilience
- Service discovery integration

### 4. Validation Strategy
**Decision**: Multi-layer validation approach.

**Rationale**:
- Defense in depth security
- Early validation prevents unnecessary processing
- Consistent validation across all layers
- Clear separation of concerns

**Implementation**:
- Bean validation annotations on entities
- Custom validation in service layer
- Business logic validation
- Input sanitization as first layer

## Security Decisions

### 1. Request Header Authentication
**Decision**: Use X-User-Id header for audit field population.

**Rationale**:
- Simple implementation for case study
- Enables audit trail functionality
- Supports distributed authentication
- Easy to test and validate

### 2. SQL Injection Prevention
**Decision**: Use JPA/Hibernate for all database operations.

**Rationale**:
- Built-in SQL injection prevention
- Type-safe database operations
- Reduces manual SQL coding
- Leverages framework security features

### 3. Input Validation and Sanitization
**Decision**: Comprehensive input validation and sanitization.

**Rationale**:
- Prevents common web vulnerabilities
- Ensures data quality and consistency
- Protects against malicious input
- Maintains system integrity

## Performance Decisions

### 1. Database Indexing Strategy
**Decision**: Create indexes on all searchable fields.

**Rationale**:
- Improves query performance
- Supports efficient search operations
- Reduces database load
- Enables scalable search functionality

**Implementation**:
- User.email index
- User.normalizedName index
- Organization.registryNumber index
- Organization.normalizedOrganizationName index
- Invitation composite indexes

### 2. Pagination Support
**Decision**: Implement basic pagination for search endpoints.

**Rationale**:
- Prevents large result sets
- Improves response times
- Reduces memory usage
- Supports scalable APIs

## Testing and Monitoring Decisions

### 1. Health Check Endpoints
**Decision**: Implement /healthz endpoints for all services.

**Rationale**:
- Enables service health monitoring
- Supports load balancer health checks
- Facilitates deployment validation
- Improves operational visibility

### 2. Comprehensive Logging
**Decision**: Implement structured logging throughout the application.

**Rationale**:
- Improves debugging capabilities
- Supports monitoring and alerting
- Enables audit trail functionality
- Facilitates operational support

## Assumptions Made

1. **Single Tenant System**: Assumed single-tenant architecture for simplicity
2. **Eventual Consistency**: Acceptable for invitation workflow
3. **Manual Testing**: Sufficient for case study evaluation
4. **Basic Security**: Header-based authentication adequate for demo purposes
5. **Local Development**: Docker Compose setup sufficient for demonstration
