# API Documentation - Digitopia Case Study

## Overview
This document provides comprehensive API documentation for the Digitopia microservices case study.

## Base URLs
- **API Gateway**: `http://localhost:8080`
- **User Service**: `http://localhost:8082`
- **Organization Service**: `http://localhost:8083`
- **Invitation Service**: `http://localhost:8084`

## Authentication
Currently, the system uses request headers for audit purposes:
- `X-User-Id`: UUID of the current user (for createdBy/updatedBy fields)

## User Service API

### Create User
**POST** `/api/users`

**Request Body:**
```json
{
  "email": "user@example.com",
  "fullName": "John Doe",
  "role": "USER",
  "status": "PENDING"
}
```

**Response:** 201 Created
```json
{
  "id": "uuid",
  "email": "user@example.com",
  "fullName": "John Doe",
  "normalizedName": "johndoe",
  "role": "USER",
  "status": "PENDING",
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-01T10:00:00",
  "createdBy": "uuid",
  "updatedBy": "uuid"
}
```

### Get User by Email
**GET** `/api/users/by-email?email=user@example.com`

**Response:** 200 OK
```json
{
  "id": "uuid",
  "email": "user@example.com",
  "fullName": "John Doe",
  "normalizedName": "johndoe",
  "role": "USER",
  "status": "PENDING",
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-01T10:00:00"
}
```

### Search Users by Normalized Name
**GET** `/api/users/search-by-normalized-name?name=john&page=0&size=20`

**Response:** 200 OK
```json
[
  {
    "id": "uuid",
    "email": "user@example.com",
    "fullName": "John Doe",
    "normalizedName": "johndoe",
    "role": "USER",
    "status": "PENDING"
  }
]
```

### Get User Organizations
**GET** `/api/users/{userId}/organizations`

**Response:** 200 OK
```json
[
  {
    "id": "uuid",
    "organizationName": "Example Corp",
    "registryNumber": "REG-001",
    "contactEmail": "contact@example.com"
  }
]
```

## Organization Service API

### Create Organization
**POST** `/api/organizations`

**Request Body:**
```json
{
  "registryNumber": "ORG-2024-001",
  "organizationName": "Digitopia Solutions Inc",
  "contactEmail": "contact@digitopiasolutions.com",
  "companySize": 50,
  "yearFounded": 2020
}
```

**Response:** 201 Created
```json
{
  "id": "uuid",
  "registryNumber": "ORG-2024-001",
  "organizationName": "Digitopia Solutions Inc",
  "normalizedOrganizationName": "digitopiasolutionsinc",
  "contactEmail": "contact@digitopiasolutions.com",
  "companySize": 50,
  "yearFounded": 2020,
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-01T10:00:00"
}
```

### Get Organization by Registry Number
**GET** `/api/organizations/by-registry/{registryNumber}`

**Response:** 200 OK
```json
{
  "id": "uuid",
  "registryNumber": "ORG-2024-001",
  "organizationName": "Digitopia Solutions Inc",
  "normalizedOrganizationName": "digitopiasolutionsinc",
  "contactEmail": "contact@digitopiasolutions.com",
  "companySize": 50,
  "yearFounded": 2020
}
```

### Advanced Organization Search
**GET** `/api/organizations/search-advanced?name=digitopia&year=2020&companySize=50`

**Response:** 200 OK
```json
[
  {
    "id": "uuid",
    "registryNumber": "ORG-2024-001",
    "organizationName": "Digitopia Solutions Inc",
    "normalizedOrganizationName": "digitopiasolutionsinc",
    "contactEmail": "contact@digitopiasolutions.com",
    "companySize": 50,
    "yearFounded": 2020
  }
]
```

### Get Organization Members
**GET** `/api/organizations/{orgId}/members`

**Response:** 200 OK
```json
[
  "user-uuid-1",
  "user-uuid-2"
]
```

### Add Member to Organization
**POST** `/api/organizations/{orgId}/members?userId={userId}`

**Response:** 204 No Content

### Get Organization Capacity Info
**GET** `/api/organizations/{orgId}/capacity-info`

**Response:** 200 OK
```json
{
  "organizationId": "uuid",
  "currentMembers": 2,
  "memberList": ["user-uuid-1", "user-uuid-2"]
}
```

## Invitation Service API

### Create Invitation
**POST** `/api/invitations`

**Request Body:**
```json
{
  "userId": "user-uuid",
  "organizationId": "org-uuid",
  "invitationMessage": "You are invited to join our amazing team!"
}
```

**Response:** 201 Created
```json
{
  "id": "uuid",
  "userId": "user-uuid",
  "organizationId": "org-uuid",
  "invitationMessage": "You are invited to join our amazing team!",
  "status": "PENDING",
  "expirationDate": "2024-01-08T10:00:00",
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-01T10:00:00"
}
```

### Update Invitation Status
**PUT** `/api/invitations/{id}/status?status=ACCEPTED`

**Response:** 200 OK
```json
{
  "id": "uuid",
  "userId": "user-uuid",
  "organizationId": "org-uuid",
  "invitationMessage": "You are invited to join our amazing team!",
  "status": "ACCEPTED",
  "expirationDate": "2024-01-08T10:00:00",
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-01T10:00:00"
}
```

### Get Invitations by User
**GET** `/api/invitations/user/{userId}`

**Response:** 200 OK
```json
[
  {
    "id": "uuid",
    "userId": "user-uuid",
    "organizationId": "org-uuid",
    "invitationMessage": "You are invited to join our amazing team!",
    "status": "PENDING",
    "expirationDate": "2024-01-08T10:00:00"
  }
]
```

## Error Responses

### Validation Error (400 Bad Request)
```json
{
  "email": "Invalid email format",
  "fullName": "Full name cannot be blank"
}
```

### Business Logic Error (400 Bad Request)
```json
{
  "error": "Email already exists: user@example.com"
}
```

### Not Found Error (404 Not Found)
```json
{
  "error": "User not found with ID: uuid"
}
```

### Capacity Exceeded Error (400 Bad Request)
```json
{
  "error": "Cannot accept invitation - organization capacity exceeded or service unavailable"
}
```

## Health Check Endpoints

All services provide health check endpoints:

- **User Service**: `GET /healthz`
- **Organization Service**: `GET /healthz`
- **Invitation Service**: `GET /healthz`
- **API Gateway**: `GET /actuator/health`

**Response:** 200 OK
```json
{
  "status": "UP",
  "timestamp": "2024-01-01T10:00:00",
  "service": "OK"
}
```

## Data Validation Rules

### User Entity
- **Email**: Valid email format, unique across all users (except DELETED)
- **Full Name**: Letters and spaces only, no numbers or special characters
- **Role**: ADMIN, MANAGER, or USER
- **Status**: ACTIVE, PENDING, DEACTIVATED, or DELETED

### Organization Entity
- **Registry Number**: Alphanumeric characters, uppercase, unique
- **Organization Name**: Alphanumeric characters, spaces, hyphens (2-100 chars)
- **Contact Email**: Valid email format
- **Company Size**: Positive integer (minimum 1)
- **Year Founded**: Integer between 1800-2100

### Invitation Entity
- **User ID**: Valid UUID, must exist in user service
- **Organization ID**: Valid UUID, must exist in organization service
- **Invitation Message**: Required, maximum 250 characters
- **Status**: ACCEPTED, REJECTED, PENDING, or EXPIRED

## Business Rules

1. **Email Uniqueness**: Email addresses must be unique across all users except those with DELETED status
2. **Registry Number Uniqueness**: Organization registry numbers must be unique
3. **Invitation Constraints**: Only one pending invitation per user per organization
4. **Capacity Limits**: Organization membership cannot exceed company size
5. **Rejection Prevention**: Users cannot be reinvited if their last invitation was rejected
6. **Expiration**: Invitations automatically expire after 7 days
7. **Auto-Expiry**: Daily scheduled job updates expired invitations
