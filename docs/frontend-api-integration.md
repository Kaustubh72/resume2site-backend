# Resume2Site Backend API Integration Guide

This document is intended for the frontend codebase agent so it can integrate against the current Resume2Site backend MVP without reverse-engineering the backend source.

## Base conventions
- Base path: `/api`
- Auth: JWT bearer token in `Authorization: Bearer <token>`
- Anonymous draft access: `X-Draft-Token: <draftToken>` header
- Success envelope:

```json
{
  "data": { }
}
```

- Error envelope:

```json
{
  "timestamp": "2026-03-22T10:15:30Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/profiles/10",
  "fieldErrors": [
    {
      "field": "slug",
      "message": "slug must contain only lowercase letters, numbers, and hyphens"
    }
  ]
}
```

## Core product flow mapping
1. Upload resume anonymously.
2. Parse upload into draft profile.
3. Store `profile.id` and `profile.draftToken` on the client.
4. Use `X-Draft-Token` for all anonymous draft fetch/edit requests.
5. Let user pick template and edit content before auth.
6. On publish click, authenticate user.
7. Call publish with bearer token and same `X-Draft-Token`.
8. After publish succeeds, switch to authenticated profile management.
9. Public profile is fetched by slug.

---

## 1. Health

### `GET /api/health`
Use for simple backend liveness checks.

---

## 2. Auth APIs

### `POST /api/auth/signup`
Create a user account.

Request:
```json
{
  "email": "alice@example.com",
  "password": "password123",
  "fullName": "Alice Johnson"
}
```

Response:
```json
{
  "data": {
    "accessToken": "jwt-token",
    "tokenType": "Bearer",
    "expiresInSeconds": 3600,
    "user": {
      "id": 1,
      "email": "alice@example.com",
      "fullName": "Alice Johnson"
    }
  }
}
```

### `POST /api/auth/login`
Same response shape as signup.

### `GET /api/auth/me`
Headers:
- `Authorization: Bearer <token>`

Response:
```json
{
  "data": {
    "id": 1,
    "email": "alice@example.com",
    "fullName": "Alice Johnson"
  }
}
```

---

## 3. Resume upload + parse

### `POST /api/resumes/upload`
Content type: `multipart/form-data`
Field name: `file`
Allowed files: PDF, DOCX

Response:
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

### `POST /api/resumes/{resumeUploadId}/parse`
Response:
```json
{
  "data": {
    "resumeUploadId": 1,
    "parseStatus": "PARSED",
    "profile": {
      "id": 10,
      "draftToken": "draft-token",
      "fullName": "Alice Johnson",
      "headline": null,
      "publicationStatus": "DRAFT",
      "slug": null,
      "templateId": null
    }
  }
}
```

Frontend notes:
- Save `profile.id` and `profile.draftToken` immediately.
- Anonymous draft edit APIs require `X-Draft-Token`.
- Parsing can still succeed with partial or mostly empty extracted data; frontend should tolerate empty arrays/nullable fields.

---

## 4. Templates

### `GET /api/templates`
Response:
```json
{
  "data": [
    {
      "id": 1,
      "code": "minimal-dev",
      "name": "Minimal Developer",
      "description": "Clean single-column template focused on early-career developers.",
      "previewImageUrl": null,
      "category": "developer",
      "accentColor": "#111827",
      "features": ["Single-column layout", "Readable project blocks"],
      "sortOrder": 1
    }
  ]
}
```

### `GET /api/templates/{templateId}`
Returns one active template in the same DTO shape.

---

## 5. Slug validation

### `GET /api/slugs/check?value={slug}`
Response:
```json
{
  "data": {
    "value": "alice-johnson",
    "valid": true,
    "available": true,
    "message": "Slug is available",
    "suggestions": []
  }
}
```

Slug rules:
- lowercase only
- letters, numbers, hyphens
- 3 to 40 chars
- reserved words blocked
- uniqueness enforced case-insensitively

---

## 6. Draft profile APIs

### Authorization model
A profile is accessible when either:
- request is authenticated as the owning user, or
- request includes valid `X-Draft-Token` for an anonymous draft profile

Once an anonymous draft is published and attached to a user, frontend should treat bearer auth as the primary access mechanism.

### `GET /api/profiles/{profileId}`
Headers for anonymous draft:
- `X-Draft-Token: <draftToken>`

Response:
```json
{
  "data": {
    "id": 10,
    "draftToken": "draft-token",
    "fullName": "Alice Johnson",
    "headline": "Software Engineer",
    "summary": "Backend-focused engineer...",
    "email": "alice@example.com",
    "phone": "+1-555-0100",
    "location": "New York, NY",
    "publicationStatus": "DRAFT",
    "slug": null,
    "templateId": 2,
    "sections": [],
    "links": [],
    "skills": [],
    "experiences": [],
    "education": [],
    "projects": []
  }
}
```

### `PUT /api/profiles/{profileId}`
Request:
```json
{
  "fullName": "Alice Johnson",
  "headline": "Software Engineer",
  "summary": "Backend-focused engineer...",
  "email": "alice@example.com",
  "phone": "+1-555-0100",
  "location": "New York, NY",
  "templateId": 2
}
```

Notes:
- `templateId` may be `null` while user is still editing.
- publish later requires a non-null active template.

### `PUT /api/profiles/{profileId}/sections`
Request:
```json
{
  "sections": [
    {
      "sectionKey": "summary",
      "displayName": "About",
      "visible": true,
      "sortOrder": 0
    }
  ]
}
```

### Nested item CRUD
All below use the same auth pattern as draft profile requests.

#### Links
- `POST /api/profiles/{profileId}/links`
- `PUT /api/profiles/{profileId}/links/{linkId}`
- `DELETE /api/profiles/{profileId}/links/{linkId}`

Create/update request:
```json
{
  "label": "GitHub",
  "url": "https://github.com/alice",
  "sortOrder": 0
}
```

#### Skills
- `POST /api/profiles/{profileId}/skills`
- `PUT /api/profiles/{profileId}/skills/{skillId}`
- `DELETE /api/profiles/{profileId}/skills/{skillId}`

Request:
```json
{
  "name": "Spring Boot",
  "category": "Frameworks",
  "sortOrder": 0
}
```

#### Experiences
- `POST /api/profiles/{profileId}/experiences`
- `PUT /api/profiles/{profileId}/experiences/{experienceId}`
- `DELETE /api/profiles/{profileId}/experiences/{experienceId}`

Request:
```json
{
  "company": "Acme Corp",
  "title": "Software Engineer",
  "location": "Remote",
  "startDate": "2024-01-01",
  "endDate": null,
  "isCurrent": true,
  "description": "Built internal APIs.",
  "sortOrder": 0
}
```

#### Education
- `POST /api/profiles/{profileId}/education`
- `PUT /api/profiles/{profileId}/education/{educationId}`
- `DELETE /api/profiles/{profileId}/education/{educationId}`

Request:
```json
{
  "institution": "State University",
  "degree": "B.S.",
  "fieldOfStudy": "Computer Science",
  "startDate": "2020-08-01",
  "endDate": "2024-05-01",
  "grade": "3.8 GPA",
  "description": "Relevant coursework...",
  "sortOrder": 0
}
```

#### Projects
- `POST /api/profiles/{profileId}/projects`
- `PUT /api/profiles/{profileId}/projects/{projectId}`
- `DELETE /api/profiles/{profileId}/projects/{projectId}`

Request:
```json
{
  "name": "Resume2Site",
  "description": "Resume-first portfolio platform",
  "projectUrl": "https://example.com",
  "repositoryUrl": "https://github.com/alice/resume2site",
  "techStack": "Java, Spring Boot, React",
  "sortOrder": 0
}
```

---

## 7. Publish and republish

### `POST /api/profiles/{profileId}/publish`
Headers:
- `Authorization: Bearer <token>`
- `X-Draft-Token: <draftToken>` when publishing an anonymous draft for the first time

Request:
```json
{
  "slug": "alice-johnson"
}
```

Response:
```json
{
  "data": {
    "profileId": 10,
    "slug": "alice-johnson",
    "publicationStatus": "PUBLISHED",
    "templateId": 2,
    "publicUrl": "/u/alice-johnson"
  }
}
```

### `POST /api/profiles/{profileId}/republish`
Same request/response shape as publish.

### `PUT /api/profiles/{profileId}/slug`
Authenticated only. Profile must already be published.

Request:
```json
{
  "slug": "alice-johnson-dev"
}
```

---

## 8. Public profile rendering API

### `GET /api/public/{slug}`
Returns only published public-safe data.

Response:
```json
{
  "data": {
    "slug": "alice-johnson",
    "publishedAt": "2026-03-22T10:15:30Z",
    "template": {
      "id": 2,
      "code": "modern-stack",
      "name": "Modern Stack"
    },
    "profile": {
      "fullName": "Alice Johnson",
      "headline": "Software Engineer",
      "summary": "Backend-focused engineer...",
      "email": "alice@example.com",
      "phone": "+1-555-0100",
      "location": "New York, NY",
      "sections": [],
      "links": [],
      "skills": [],
      "experiences": [],
      "education": [],
      "projects": []
    }
  }
}
```

Frontend notes:
- If this endpoint returns 404, treat slug as unpublished/not found.
- Frontend should render the selected template client-side from the shared structured response.

---

## 9. Validation + UX notes for frontend agent

- Always persist `draftToken` client-side after parse.
- Send `X-Draft-Token` on every anonymous draft profile request.
- During edit flow, tolerate empty arrays and nullable top-level fields.
- Run slug availability checks before publish, but still handle publish conflicts because DB uniqueness is authoritative.
- Treat `400` validation responses as field-level UI errors when `fieldErrors` is present.
- Treat `401` as login required / invalid session.
- Treat `403` as access denied.
- Treat `404` on public slug as not found/unpublished.
- Template must be selected before publish, but not necessarily before earlier edits.
