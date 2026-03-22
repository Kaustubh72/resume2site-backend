# Resume2Site Backend

Spring Boot backend for the Resume2Site MVP: a resume-first platform that turns an uploaded resume into an editable portfolio draft, lets the user preview before login, and publishes the profile under a path-based public URL such as `/u/{slug}`.

## What the backend does
- Accepts anonymous resume uploads for PDF and DOCX files.
- Extracts and parses resume text into a structured draft profile.
- Protects anonymous drafts with a high-entropy draft token.
- Supports draft profile editing, nested section CRUD, template selection, slug checks, publish, and republish.
- Supports email/password signup, login, and authenticated ownership after publish.
- Exposes public-safe published profile data for frontend rendering.

## Current MVP scope
Included in this repository:
- auth: signup, login, me
- anonymous resume upload + parse
- draft profile retrieval and editing
- nested CRUD for links, skills, experiences, education, and projects
- template listing and template detail
- slug validation/checking
- publish + republish
- public profile retrieval by slug
- Flyway migrations, validation, and consistent API error responses

Explicitly out of scope for MVP:
- custom domains or subdomains
- static site generation/export
- analytics or billing
- admin tooling
- AI rewriting features
- microservices / queue-based redesigns

## Stack
- Java 21+
- Spring Boot 3.3.x
- Maven
- PostgreSQL
- Spring Data JPA / Hibernate
- Flyway
- Spring Security + JWT
- Bean Validation
- Apache Tika + PDFBox + Apache POI

## Local setup

### 1. Create the database
Example PostgreSQL bootstrap:

```sql
CREATE DATABASE resume2site;
CREATE USER resume2site WITH PASSWORD 'resume2site';
GRANT ALL PRIVILEGES ON DATABASE resume2site TO resume2site;
```

### 2. Configure environment variables
The app has local defaults, but these are the main overrides:

```bash
export DB_URL=jdbc:postgresql://localhost:5432/resume2site?currentSchema=resume2siteschema
export DB_USERNAME=resume2site
export DB_PASSWORD=resume2site
export APP_JWT_SECRET=change-me-change-me-change-me-change-me-please
export APP_JWT_ISSUER=resume2site
export APP_JWT_ACCESS_TTL_MINUTES=60
export APP_CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:5173
export APP_UPLOAD_MAX_FILE_SIZE=10MB
export APP_UPLOAD_MAX_REQUEST_SIZE=10MB
export APP_UPLOAD_MAX_FILE_SIZE_BYTES=10485760
export SERVER_PORT=8081
```

### 3. Run the backend
```bash
chmod +x mvnw
./mvnw spring-boot:run
```

If you already have Maven installed:

```bash
mvn spring-boot:run
```

### 4. Run migrations
Flyway runs automatically on application startup.

If you want an explicit migration-oriented startup check, run:

```bash
./mvnw spring-boot:run
```

### 5. Run tests
```bash
./mvnw test
```

If the Maven wrapper cannot download dependencies in your environment, use a preinstalled Maven with network access to your dependency mirror:

```bash
mvn test
```

## High-level API overview

### Health
- `GET /api/health`
- `GET /actuator/health`

### Auth
- `POST /api/auth/signup`
- `POST /api/auth/login`
- `GET /api/auth/me`

### Resume upload + parse
- `POST /api/resumes/upload`
- `POST /api/resumes/{resumeUploadId}/parse`

### Templates
- `GET /api/templates`
- `GET /api/templates/{templateId}`

### Slugs
- `GET /api/slugs/check?value={slug}`

### Draft profile APIs
Anonymous draft access uses the `X-Draft-Token` header until the profile is attached to a logged-in user.

- `GET /api/profiles/{profileId}`
- `PUT /api/profiles/{profileId}`
- `PUT /api/profiles/{profileId}/sections`
- `POST /api/profiles/{profileId}/links`
- `PUT /api/profiles/{profileId}/links/{linkId}`
- `DELETE /api/profiles/{profileId}/links/{linkId}`
- `POST /api/profiles/{profileId}/skills`
- `PUT /api/profiles/{profileId}/skills/{skillId}`
- `DELETE /api/profiles/{profileId}/skills/{skillId}`
- `POST /api/profiles/{profileId}/experiences`
- `PUT /api/profiles/{profileId}/experiences/{experienceId}`
- `DELETE /api/profiles/{profileId}/experiences/{experienceId}`
- `POST /api/profiles/{profileId}/education`
- `PUT /api/profiles/{profileId}/education/{educationId}`
- `DELETE /api/profiles/{profileId}/education/{educationId}`
- `POST /api/profiles/{profileId}/projects`
- `PUT /api/profiles/{profileId}/projects/{projectId}`
- `DELETE /api/profiles/{profileId}/projects/{projectId}`
- `POST /api/profiles/{profileId}/publish`
- `POST /api/profiles/{profileId}/republish`
- `PUT /api/profiles/{profileId}/slug`

### Public profile API
- `GET /api/public/{slug}`

## Resume upload flow
1. Anonymous user uploads a PDF or DOCX file.
2. Backend stores the file temporarily and creates a `resume_uploads` row.
3. Client calls parse with the returned upload id.
4. Backend extracts text, parses it best-effort, creates a draft profile, and returns the draft token.
5. Temporary upload storage is deleted after parse completes or fails.
6. When the user publishes while authenticated, the profile is attached to that user account.

## Validation and safety notes
- Uploads are server-side validated for presence, size, extension, and content type.
- Slugs are centrally validated for lowercase format, length, reserved words, and uniqueness.
- Publish requires an authenticated user and an active template selection.
- Public profile responses intentionally exclude draft tokens, ownership metadata, and upload internals.
- Error responses share one shape with timestamp, status, message, path, and optional field errors.

## Example responses

### Upload response
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

### Parse response
```json
{
  "data": {
    "resumeUploadId": 1,
    "parseStatus": "PARSED",
    "profile": {
      "id": 10,
      "draftToken": "high-entropy-draft-token",
      "fullName": "Alice Johnson",
      "headline": null,
      "publicationStatus": "DRAFT",
      "slug": null,
      "templateId": null
    }
  }
}
```

### Standard error response
```json
{
  "timestamp": "2026-03-22T10:15:30Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/profiles/10/publish",
  "fieldErrors": [
    {
      "field": "slug",
      "message": "slug must contain only lowercase letters, numbers, and hyphens"
    }
  ]
}
```


## Frontend integration handoff
Share `docs/frontend-api-integration.md` with the frontend agent for request/response shapes, auth expectations, draft-token handling, and publish/public-profile integration notes.
