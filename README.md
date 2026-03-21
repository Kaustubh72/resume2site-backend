# Resume2Site Backend

Spring Boot backend for the Resume2Site MVP, a resume-first platform that converts uploaded resumes into editable portfolio profiles and publishes them under path-based public URLs.

## What is included right now
- Spring Boot 3.3.x project with Java 21 and Maven.
- Feature-oriented package structure for auth, resume, profile, template, common, config, and security.
- PostgreSQL + JPA + Flyway setup.
- Initial MVP schema for users, resume uploads, profiles, nested profile sections, and templates.
- MVP auth module with email/password signup, login, `/me`, BCrypt password hashing, and JWT-based stateless security.
- Resume upload + parsing pipeline for anonymous PDF/DOCX uploads.
- Apache Tika based text extraction and a modular best-effort parser that builds draft profile records.

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
./mvnw spring-boot:run
```

If Maven Wrapper is not present, use:

```bash
mvn spring-boot:run
```

### 4. Verify health
```bash
curl http://localhost:8081/api/health
curl http://localhost:8081/actuator/health
```

## Current API surface
- `GET /api/health`
- `POST /api/auth/signup`
- `POST /api/auth/login`
- `GET /api/auth/me`
- `GET /api/templates`
- `POST /api/resumes/upload`
- `POST /api/resumes/{resumeUploadId}/parse`

## Resume upload + parsing flow
The MVP keeps upload and parsing explicit:
1. Anonymous user uploads a PDF or DOCX.
2. Backend stores a temporary file and creates a `resume_uploads` record.
3. Client calls parse using the returned upload id.
4. Backend extracts text with Apache Tika, normalizes it, parses best-effort profile data, creates a draft `profiles` record, and stores child rows for links, skills, experience, education, and projects.
5. Temporary source file is deleted after parse completes.

### Upload example
```bash
curl -X POST http://localhost:8081/api/resumes/upload \
  -H 'Accept: application/json' \
  -F 'file=@/absolute/path/to/resume.pdf'
```

Example response:
```json
{
  "data": {
    "id": 1,
    "originalFileName": "resume.pdf",
    "contentType": "application/pdf",
    "fileSizeBytes": 245678,
    "parseStatus": "UPLOADED"
  }
}
```

### Parse example
```bash
curl -X POST http://localhost:8081/api/resumes/1/parse \
  -H 'Accept: application/json'
```

Example response:
```json
{
  "data": {
    "resumeUploadId": 1,
    "parseStatus": "PARSED",
    "profile": {
      "id": 10,
      "draftToken": "6f7d8b24-3d2a-43cf-9b08-9f34d6d418cb",
      "fullName": "Alice Johnson",
      "headline": null,
      "publicationStatus": "DRAFT",
      "slug": null,
      "templateId": null
    }
  }
}
```

## Auth API examples

### Signup
```bash
curl -X POST http://localhost:8081/api/auth/signup \
  -H 'Content-Type: application/json' \
  -d '{
    "email": "alice@example.com",
    "password": "password123",
    "fullName": "Alice Johnson"
  }'
```

### Login
```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{
    "email": "alice@example.com",
    "password": "password123"
  }'
```

### Me
```bash
curl http://localhost:8081/api/auth/me \
  -H 'Authorization: Bearer <access-token>'
```

## Security behavior
- `POST /api/auth/signup` and `POST /api/auth/login` are public.
- `POST /api/resumes/upload` and `POST /api/resumes/{resumeUploadId}/parse` are public for the anonymous MVP preview flow.
- `GET /api/auth/me` requires a valid JWT bearer token.
- Template listing, slug checks, and public profile fetches remain public for the MVP flow.
- Other ownership-sensitive endpoints are protected by default unless explicitly opened.

## MVP assumptions in the current codebase
- Profiles are dynamically rendered by the frontend from structured API data.
- Resume parsing is intentionally heuristic and all parsed data is expected to be editable later.
- Temporary uploaded files are deleted after parse and are not kept forever.
- Publish/login flow will later attach anonymous drafts to authenticated users.

## Recommended next step
Implement **profile CRUD + draft editing APIs** next:
- fetch anonymous draft by token
- update parsed profile fields and nested sections
- support template selection and live preview data retrieval
