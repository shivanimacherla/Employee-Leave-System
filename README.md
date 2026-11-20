# Employee Management & Leave Approval System

A layered Spring Boot REST API for managing Employees, Departments, and Leave Requests,
built as a practice project to demonstrate core backend patterns end-to-end.

## Architecture

```
Controller → Service → Repository / DAO → Entity
```

- **Controller layer** — REST endpoints, request validation (`@Valid`), delegates to services
- **Service layer** — business logic, constructor-injected dependencies throughout
- **Persistence layer** — deliberately implemented two ways:
  - `EmployeeRepository` / `DepartmentRepository` — standard Spring Data JPA repositories
  - `LeaveRequestDao` / `LeaveRequestDaoImpl` — hand-written DAO using `EntityManager`
    directly (`persist`, `merge`, `find`, JPQL queries) instead of Spring Data JPA,
    to demonstrate understanding of what the abstraction is doing underneath
- **Exception handling** — centralized via `@RestControllerAdvice`, returning consistent
  JSON error responses with correct HTTP status codes (404 for not-found, 400 for
  validation/business-rule failures)

## Key business logic

- **Enum-driven leave status workflow**: `PENDING → APPROVED/REJECTED/CANCELLED`,
  `APPROVED → CANCELLED`. Invalid transitions (e.g. approving an already-rejected
  request) are rejected with a 400.
- **Validation rules**: leave balance checks, date-range validation, and overlap
  detection against the employee's existing pending/approved requests.
- **Leave balance deduction**: approving a request atomically deducts the requested
  days from the employee's balance.

## Tech stack

Java 17, Spring Boot 3.2, Spring Data JPA, Spring Validation, MySQL

## Running locally

1. Create a MySQL database (or let `application.properties` auto-create it)
2. Update `src/main/resources/application.properties` with your MySQL credentials
3. `mvn spring-boot:run`
4. API available at `http://localhost:8080/api/employees`, `/api/departments`, `/api/leave-requests`

## Example flow

```
POST /api/departments          { "name": "Engineering" }
POST /api/employees            { firstName, lastName, email, dateOfJoining, departmentId }
POST /api/leave-requests       { employeeId, startDate, endDate, reason }
PATCH /api/leave-requests/1/status   { "status": "APPROVED" }
```
