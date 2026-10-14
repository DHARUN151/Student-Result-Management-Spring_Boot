# Student Result Management System

A role-based academic results application built with **Java 21, Spring
Boot, Spring Security, Spring Data JPA/Hibernate, and PostgreSQL**. The
backend is being developed incrementally; tested features include
student marks and result summaries, faculty marks operations, and
faculty assignment listing.

> **Status note:** React, the complete Admin module, automated test
> coverage, Docker, and Jenkins are roadmap items until implemented and
> verified. This README describes the current known setup and avoids
> claiming unfinished features are complete.

## Contents

-   [Overview](#overview)
-   [Features](#features)
-   [Technology stack](#technology-stack)
-   [Architecture diagrams](#architecture-diagrams)
-   [Roles and security](#roles-and-security)
-   [API reference](#api-reference)
-   [Project structure](#project-structure)
-   [Setup and run](#setup-and-run)
-   [Postman testing](#postman-testing)
-   [Marks and grading](#marks-and-grading)
-   [Database safety](#database-safety)
-   [Troubleshooting](#troubleshooting)
-   [Roadmap](#roadmap)

## Overview

The Student Result Management System manages student marks,
examinations, subject offerings, faculty assignments, grades, and result
summaries. Students should only see marks associated with their
authenticated account. Faculty can access marks for subject offerings
assigned to their faculty profile.

The project uses an existing PostgreSQL database named
`Student_Result_DB` and the `enterprise` schema. Preserve the current
schema and tested data.

## Features

### Student APIs

-   View the authenticated student's marks.
-   View totals for mark records, passed records, failed records, and
    pass percentage.
-   View grade-wise counts.

### Faculty APIs

-   List active subject assignments for the authenticated faculty
    member.
-   View marks by offering and examination.
-   Retrieve an individual mark record for an assigned offering.
-   Enter marks with service-level validation and duplicate checks.
-   Update an existing mark without changing its student, offering, or
    examination association.

### Security

-   Spring Security authentication and session cookies.
-   Role-based protection for `/api/student/**` and `/api/faculty/**`.
-   CSRF protection for state-changing requests.
-   Server-side identity lookup from the authenticated username.
-   Faculty assignment checks before accessing offering marks.

## Technology stack

  Layer                Technology
  -------------------- ---------------------------------
  Language             Java 21
  Backend              Spring Boot
  Security             Spring Security
  ORM / persistence    Spring Data JPA and Hibernate
  Database             PostgreSQL
  API                  REST / JSON
  API testing          Postman
  IDE                  IntelliJ IDEA
  Planned frontend     React + Vite
  Planned deployment   Docker, Docker Compose, Jenkins

Use the versions configured in the existing project build files as the
source of truth.

## Architecture diagrams

### System architecture

``` mermaid
flowchart TD
    C[Postman / Future React UI] -->|HTTP + JSON| S[Spring Security]
    S --> CT[REST Controllers]
    CT --> SV[Service Layer]
    SV --> R[Spring Data JPA Repositories]
    R --> H[Hibernate / JPA]
    H --> DB[(PostgreSQL<br/>Student_Result_DB<br/>enterprise schema)]
```

### Typical request flow

``` mermaid
sequenceDiagram
    participant Client as Postman / React
    participant Security as Spring Security
    participant API as Controller
    participant Service as Service
    participant Repo as Repository
    participant DB as PostgreSQL
    Client->>Security: Request + session cookie
    Security->>Security: Authentication, authorization, CSRF check
    Security->>API: Authorized request
    API->>Service: Business operation
    Service->>Repo: Read/write request
    Repo->>DB: SQL
    DB-->>Repo: Rows
    Repo-->>Service: Entities
    Service-->>API: DTO/result
    API-->>Client: JSON response
```

### Marks entry flow

``` mermaid
flowchart TD
    A[Faculty submits marks] --> B{Authenticated?}
    B -- No --> E1[401 / 403]
    B -- Yes --> C{Faculty profile linked?}
    C -- No --> E2[403 Forbidden]
    C -- Yes --> D{Active assignment?}
    D -- No --> E2
    D -- Yes --> F{Duplicate student + offering + exam?}
    F -- Yes --> E3[409 Conflict]
    F -- No --> G[Validate marks and exam]
    G --> H[Calculate total, grade, outcome]
    H --> I[Save mark]
    I --> J[Return response]
```

## Roles and security

The project has role names including `SUPER_ADMIN`, `ADMIN`, `HOD`,
`FACULTY`, `EXAM_CELL`, and `STUDENT`. Verify the actual permissions in
`SecurityConfig.java`.

  -----------------------------------------------------------------------
  Path                                Intended protection
  ----------------------------------- -----------------------------------
  `/api/student/**`                   Student access as configured in
                                      Spring Security

  `/api/faculty/**`                   Faculty and permitted
                                      administrative roles as configured

  `/api/csrf`                         CSRF token endpoint

  `/login`                            Configured login endpoint
  -----------------------------------------------------------------------

Authorization must be enforced by the backend; hiding frontend links is
not sufficient. Student endpoints should derive the student ID from the
authenticated user's database profile, never from a client-supplied
student ID.

## API reference

These endpoints have been exercised during development. Verify against
the current code before treating this table as a final contract.

  --------------------------------------------------------------------------------------------------
  Method                  Endpoint                                           Purpose
  ----------------------- -------------------------------------------------- -----------------------
  `GET`                   `/api/csrf`                                        Retrieve CSRF token
                                                                             details

  `GET`                   `/api/student/marks`                               Current student's marks

  `GET`                   `/api/student/result-summary`                      Pass/fail totals, pass
                                                                             percentage, grade
                                                                             counts

  `GET`                   `/api/faculty/assignments`                         Current faculty
                                                                             member's active
                                                                             assignments

  `GET`                   `/api/faculty/marks?offeringId={id}&examId={id}`   Marks for an assigned
                                                                             offering and exam

  `GET`                   `/api/faculty/marks/{markId}`                      One mark for an
                                                                             assigned offering

  `POST`                  `/api/faculty/marks-entry`                         Enter marks

  `PUT`                   `/api/faculty/marks/{markId}`                      Update existing marks
  --------------------------------------------------------------------------------------------------

A previous duplicate route conflict occurred when two controllers mapped
`POST /api/faculty/marks`. The entry controller was moved to
`/api/faculty/marks-entry`. Avoid duplicate controller mappings for the
same HTTP method and path.

### Example: student marks

``` json
[
  {
    "markId": 13,
    "offeringId": 1,
    "examId": 1,
    "examName": "Legacy Imported Results",
    "internalMark": 30,
    "externalMark": 40,
    "totalMark": 70,
    "grade": "B+",
    "resultOutcome": "PASS",
    "enteredAt": "2026-09-02T09:10:32.177149"
  }
]
```

Example only; actual results depend on the signed-in student's records.

### Example: result summary

``` json
{
  "totalSubjects": 2,
  "subjectsPassed": 2,
  "subjectsFailed": 0,
  "passPercentage": 100.0,
  "gradeO": 0,
  "gradeAPlus": 1,
  "gradeA": 0,
  "gradeBPlus": 1,
  "gradeB": 0,
  "gradeC": 0,
  "gradeF": 0
}
```

**Note:** The current summary counts marks records, not necessarily
distinct subjects. If a student has multiple records for one subject,
each record contributes to the totals.

### Example: faculty assignments

``` json
[
  {
    "assignmentId": 1,
    "offeringId": 1,
    "subjectCode": "EC107",
    "subjectName": "Wireless Communication",
    "semester": 7,
    "academicYear": "2026-2027",
    "status": "ACTIVE"
  }
]
```

### Enter marks

Request: `POST /api/faculty/marks-entry`

``` json
{
  "studentId": 1,
  "offeringId": 1,
  "examId": 1,
  "internalMark": 30,
  "externalMark": 50
}
```

Use valid database IDs and ensure the field names match `MarksEntryDTO`.
The service should validate the faculty assignment, marks limits,
exam/offering compatibility, and duplicates.

### Update marks

Request: `PUT /api/faculty/marks/1`

``` json
{
  "studentId": 1,
  "offeringId": 1,
  "examId": 1,
  "internalMark": 30,
  "externalMark": 50
}
```

Use an existing `markId`. The service is intended to update the mark
values without changing the associated student, offering, or
examination.

## Project structure

``` text
student-result-management/
└── src/main/
    ├── java/com/studentresult/management/
    │   ├── config/       # Security and app configuration
    │   ├── controller/   # REST controllers
    │   ├── dto/          # Request/response DTOs
    │   ├── entity/       # JPA entity mappings
    │   ├── repository/   # Spring Data repositories
    │   └── service/      # Business rules and validation
    └── resources/
        └── application.properties
README.md
```

This is a representative layout; retain your actual existing project
structure.

## Setup and run

### Prerequisites

1.  JDK 21.
2.  PostgreSQL with access to `Student_Result_DB`.
3.  IntelliJ IDEA or another Java IDE.
4.  Postman.
5.  The build tool already configured by your project.

Check Java:

``` bash
java -version
```

### Database configuration

Configure `src/main/resources/application.properties` or your IDE
environment variables. Example property values:

``` properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=validate
```

Example local URL:

``` text
jdbc:postgresql://localhost:5432/Student_Result_DB
```

Example local environment values:

``` dotenv
DB_URL=jdbc:postgresql://localhost:5432/Student_Result_DB
DB_USERNAME=your_database_username
DB_PASSWORD=your_local_password
```

Spring Boot does not automatically load every `.env` file. Make sure
your run configuration or chosen environment loader provides the
variables.

### Start the backend

1.  Open the existing project in IntelliJ.
2.  Select JDK 21.
3.  Configure database credentials as environment variables.
4.  Start PostgreSQL and verify database connectivity.
5.  Run the Spring Boot main application class.
6.  Wait for a `Started ...Application` log message.
7.  Test an endpoint using Postman.

Local base URL used during development: `http://localhost:8080`.

## Postman testing

### 1. Authenticate

Use the login mechanism configured in `SecurityConfig.java`. Preserve
the authenticated session cookies in Postman.

### 2. Get a CSRF token

``` http
GET http://localhost:8080/api/csrf
```

Read the returned `headerName` and `token`. Use the current token with
the matching cookie/session.

### 3. Send POST or PUT requests

-   Preserve the authenticated session cookie.
-   Send the current CSRF token in the configured header (commonly
    `X-XSRF-TOKEN`).
-   Set `Content-Type: application/json`.
-   In Postman, choose **Body → raw → JSON**.
-   Send JSON matching the request DTO.

### 4. Test student endpoints

``` http
GET http://localhost:8080/api/student/marks
GET http://localhost:8080/api/student/result-summary
```

Authenticate as a student. Confirm the response contains only that
student's records.

### 5. Test faculty assignments

``` http
GET http://localhost:8080/api/faculty/assignments
```

Authenticate as faculty and confirm that only their active assignments
are returned.

### 6. Test marks entry/update

Test a new valid combination, a duplicate combination, an invalid mark
value, an unassigned offering, and a valid update. Do not delete
existing data to bypass duplicate protection.

## Marks and grading

The grading scale specified for this project is:

    Total mark Grade
  ------------ -------
       90--100 O
        80--89 A+
        70--79 A
        60--69 B+
        50--59 B
        40--49 C
      Below 40 F

Result outcome: - Total mark `>= 40`: `PASS` - Total mark `< 40`: `FAIL`

Ensure the service validates internal and external marks against
subject-specific maximums and checks the exam's semester and academic
year against the offering. Review these rules before using results
officially.

## Database safety

-   Preserve the existing PostgreSQL schema and tested data.
-   Keep `spring.jpa.hibernate.ddl-auto=validate` for schema validation.
-   Do not use `create`, `create-drop`, or automatic schema updates
    against the existing database.
-   Back up the database before migration, bulk updates, or deployment
    changes.
-   Do not delete existing marks to get around duplicate checks.
-   Never commit database passwords, `.env` files, database dumps
    containing sensitive data, or private SAML material.

## Troubleshooting

  -------------------------------------------------------------------------
  Error                                 What to inspect first
  ------------------------------------- -----------------------------------
  `No property ... found`               Spring Data repository method name
                                        vs. entity property

  `Schema-validation: missing column`   Entity mapping vs. actual database
                                        schema

  Duplicate mapping error               Controllers sharing the same HTTP
                                        method and route

  `403 Forbidden`                       CSRF token/session or role
                                        authorization

  `Required request body is missing`    Postman Body → raw → JSON and
                                        `@RequestBody`

  `409 Conflict`                        Duplicate mark combination or
                                        service validation

  `500 Internal Server Error`           Root exception in application logs
  -------------------------------------------------------------------------

### Reading Spring Boot logs

1.  Find `APPLICATION FAILED TO START` or the request's exception.
2.  Read the deepest meaningful `Caused by:` message.
3.  Identify the named class, repository method, entity property, or
    endpoint.
4.  Fix the root cause first, then restart or repeat the request.
5.  Check the new response and logs.

HikariCP "thread starvation or clock leap" warnings can indicate a JVM
pause or delayed scheduling. They do not, by themselves, explain a
particular HTTP request failure.

## Roadmap

-   [x] Student marks endpoint
-   [x] Student result summary with grade counts
-   [x] Faculty marks list and single-record retrieval
-   [x] Faculty marks update
-   [x] Faculty active assignment listing
-   [x] Marks-entry endpoint and duplicate-check path
-   [ ] Faculty student list for an assigned offering
-   [ ] Admin APIs for students, faculty, departments, subjects, and
    examinations
-   [ ] Consistent validation and error response format
-   [ ] Automated service/controller tests and role test matrix
-   [ ] React frontend
-   [ ] Docker and Docker Compose
-   [ ] Jenkins CI/CD pipeline

Only mark an item complete after it is implemented and tested in the
current project.

## Development guidelines

-   Keep business rules in services rather than controllers.
-   Use DTOs instead of exposing JPA entities directly.
-   Resolve user identity from the authenticated security context.
-   Verify faculty assignment before accessing marks for an offering.
-   Add tests for new endpoints and error cases.
-   Keep secrets and private SAML material out of source control.

## License

Add the license selected for this project. Do not claim an open-source
license until one has been chosen.
