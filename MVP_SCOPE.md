# Resume2Site Backend — MVP Scope

## Objective
Build the backend API for the Resume2Site MVP only.

The backend must support the complete end-to-end user journey for:
- anonymous resume upload
- resume parsing
- editable draft profile
- authentication at publish time
- slug-based publishing
- public profile retrieval
- edit and republish later

---

# In Scope (MVP)

## 1. Project Foundation
- Spring Boot project setup
- Maven build
- application configuration
- environment variable support
- global exception handling
- validation framework
- CORS configuration
- health endpoint
- Flyway migrations

## 2. Database Schema (MVP)
Create and maintain migrations for:
- `users`
- `resume\_uploads`
- `profiles`
- `profile\_links`
- `profile\_skills`
- `profile\_experiences`
- `profile\_education`
- `profile\_projects`
- `profile\_sections`
- `templates`

## 3. Authentication
- Email/password signup
- Email/password login
- Password hashing (BCrypt)
- JWT auth
- Authenticated user context (`/me`)

## 4. Resume Upload
- Anonymous upload allowed
- Support PDF and DOCX only
- Validate file type
- Validate file size
- Create upload record

## 5. Resume Parsing (Practical MVP)
- Extract text using Apache Tika
- Use PDFBox / POI only if needed
- Normalize text
- Parse into structured profile draft
- Best-effort extraction for:
  - full name
  - email
  - phone
  - links
  - summary
  - skills
  - experience
  - education
  - projects
- Missing sections must not fail the flow
- Store results in `profiles` + child tables

## 6. Draft Profile APIs
- Fetch draft profile by ID
- Update top-level fields
- CRUD for nested sections:
  - links
  - skills
  - experiences
  - education
  - projects
- Update section visibility/order

## 7. Templates API
- Return active templates
- Seed 3 default templates

## 8. Slug \& Publish Flow
- Check slug availability
- Enforce slug rules
- Reserved words blocklist
- Publish profile
- Republish profile
- Associate anonymous draft with authenticated user at publish time

## 9. Public Profile API
- Fetch published profile by slug
- Return only public-safe data
- Never expose unpublished/private profile data

---

# Out of Scope (Do NOT Build in MVP)

## Infrastructure / Routing
- Custom domains
- Wildcard subdomains
- Multi-host domain routing
- CDN-specific advanced routing logic

## Product Features
- Analytics
- Portfolio view tracking
- Click tracking
- AI-generated summaries
- AI rewrite assistant
- Job-role variants
- Resume diffing / re-upload compare
- Multi-language support
- Team collaboration
- Admin panel
- Billing / subscriptions

## Architecture / Engineering Overbuild
- Microservices
- Kafka / async event systems
- Distributed caching unless explicitly needed
- Search engine integration (Elasticsearch, etc.)
- Hexagonal over-abstraction if it slows delivery

## Parsing Overbuild
- OCR in MVP
- ML/NLP models in MVP
- ATS-grade perfect extraction
- Overly complex parsing heuristics before end-to-end flow works

## Output Generation
- Static site generation
- ZIP export
- HTML export
- Server-side HTML rendering for templates

---

# Quality Bar

The backend should be:
- production-appropriate for MVP
- cleanly structured
- easy to extend
- not over-engineered
- well validated
- secure enough for public demo / real MVP

---

# Definition of Done for MVP Backend

Backend MVP is considered complete when:
- \[ ] Anonymous resume upload works
- \[ ] Resume parsing creates a usable draft profile
- \[ ] Draft profile can be edited
- \[ ] Auth works (signup/login/me)
- \[ ] Slug validation works
- \[ ] Publish works
- \[ ] Public profile retrieval by slug works
- \[ ] User can edit and republish later
- \[ ] Templates API returns 3 active templates
- \[ ] DB migrations are stable and reproducible
- \[ ] Error responses are consistent
- \[ ] README is usable for local setup
