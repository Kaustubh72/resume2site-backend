# Resume2Site Backend

Spring Boot backend for the Resume2Site MVP, a resume-first platform that converts uploaded resumes into editable portfolio profiles and publishes them under path-based public URLs.

## What is included right now
- Spring Boot 3.3.x project with Java 21 and Maven.
- Feature-oriented package structure for auth, resume, profile, template, common, config, and security.
- PostgreSQL + JPA + Flyway setup from day one.
- Initial MVP schema for users, resume uploads, profiles, nested profile sections, and templates.
- MVP auth module with email/password signup, login, `/me`, BCrypt password hashing, and JWT-based stateless security.
- CORS, multipart upload limits, actuator health, and consistent API error handling.
- Seed data for the 3 default portfolio templates.

## Stack
- Java 21
- Spring Boot 3.x
- PostgreSQL
- Maven
- Spring Data JPA / Hibernate
- Flyway
- Spring Security
- JWT (jjwt)
- Bean Validation
- Apache Tika + PDFBox + Apache POI

## Local setup

### 1. Start PostgreSQL
Create a database and user locally, for example:

```sql
CREATE DATABASE resume2site;
CREATE USER resume2site WITH PASSWORD 'resume2site';
GRANT ALL PRIVILEGES ON DATABASE resume2site TO resume2site;
```

### 2. Configure environment variables
You can run with defaults for local development, or override them:

```bash
export DB_URL=jdbc:postgresql://localhost:5432/resume2site
export DB_USERNAME=resume2site
export DB_PASSWORD=resume2site
export APP_JWT_SECRET=change-me-change-me-change-me-change-me-please
export APP_CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:5173
```

### 3. Run the application
```bash
mvn spring-boot:run
```

### 4. Verify health
```bash
curl http://localhost:8080/api/health
curl http://localhost:8080/actuator/health
```

## Current API surface
- `GET /api/health`
- `POST /api/auth/signup`
- `POST /api/auth/login`
- `GET /api/auth/me`
- `GET /api/templates`
- `GET /api/resumes/foundation-status`
- `GET /api/profiles/foundation-status`

## Auth API examples

### Signup
```bash
curl -X POST http://localhost:8080/api/auth/signup \
  -H 'Content-Type: application/json' \
  -d '{
    "email": "alice@example.com",
    "password": "password123",
    "fullName": "Alice Johnson"
  }'
```

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{
    "email": "alice@example.com",
    "password": "password123"
  }'
```

### Me
```bash
curl http://localhost:8080/api/auth/me \
  -H 'Authorization: Bearer <access-token>'
```

## Security behavior
- `POST /api/auth/signup` and `POST /api/auth/login` are public.
- `GET /api/auth/me` requires a valid JWT bearer token.
- Template listing, resume upload/parsing endpoints, slug checks, and public profile fetches remain public for the MVP flow.
- Other future ownership-sensitive endpoints are protected by default unless explicitly opened.

## MVP assumptions in the current codebase
- Profiles are dynamically rendered by the frontend from structured API data.
- Anonymous draft support will be handled through `draft_token` in later steps.
- Resume uploads are modeled now, but storage and parsing pipeline are intentionally deferred.
- Publish/login flow will later attach anonymous drafts to authenticated users.

## Recommended next step
Implement the **resume upload + parsing pipeline** next:
- anonymous resume upload
- PDF/DOCX validation
- extraction with Apache Tika
- best-effort structured draft profile creation
- draft token based anonymous preview/edit support
