# Resume2Site Backend — Recommended Task Sequence

Use this sequence when working with coding agents.

---

# Task 1 — Project Foundation

## Goal
Create the backend foundation only.

## Includes
- Spring Boot setup
- Maven dependencies
- application.yml
- environment variable support
- package structure
- Flyway setup
- initial DB migrations
- entity skeletons / repositories
- global exception handling
- security skeleton
- health endpoint
- README

## Prompt to Agent
Use `AGENT\_PROMPT.md` as base context, then ask:

> Build the backend project foundation only. Do not implement feature modules yet. Create the project structure, dependencies, config, Flyway setup, initial migrations, security skeleton, global exception handling, and a concise README. Then stop and summarize what was created.

---

# Task 2 — Authentication Module

## Goal
Implement MVP auth.

## Includes
- signup
- login
- me endpoint
- JWT
- BCrypt
- auth DTOs
- user repository integration
- endpoint security rules

## Prompt to Agent
> Implement the MVP authentication module only: signup, login, and me. Keep it simple and production-appropriate for MVP. Public endpoints for resume upload/parse/templates/public slug access must remain public. Then stop and summarize.

---

# Task 3 — Resume Upload + Parsing Pipeline

## Goal
Support anonymous upload and draft creation.

## Includes
- POST /api/resumes/upload
- POST /api/resumes/{id}/parse
- file validation
- Tika-based extraction
- text normalization
- simple parser
- create draft profile
- create child records

## Prompt to Agent
> Implement anonymous resume upload and parsing for PDF/DOCX only. Use Apache Tika first. Create a practical modular parser and store a structured draft profile. Keep parsing useful, not perfect. Then stop and summarize.

---

# Task 4 — Draft Profile CRUD

## Goal
Allow the frontend to edit parsed profiles.

## Includes
- GET /api/profiles/{id}
- PUT /api/profiles/{id}
- section visibility update
- CRUD for skills/links/experiences/education/projects

## Prompt to Agent
> Implement draft profile retrieval and editing APIs for top-level fields and nested sections. Keep DTOs frontend-friendly. Support anonymous draft access in a simple practical MVP-safe way if needed. Then stop and summarize.

---

# Task 5 — Slug + Publish Flow

## Goal
Publish profiles to public routes.

## Includes
- GET /api/slugs/check
- POST /api/profiles/{id}/publish
- POST /api/profiles/{id}/republish
- slug rules
- reserved words
- attach anonymous draft to user at publish time

## Prompt to Agent
> Implement slug validation and publish/republish flow. Use path-based slugs only. Enforce uniqueness and reserved words. Attach anonymous drafts to authenticated users at publish time. Then stop and summarize.

---

# Task 6 — Templates + Public Profile APIs

## Goal
Support frontend rendering of templates and public pages.

## Includes
- GET /api/templates
- seed 3 templates
- GET /api/public/{slug}
- public-safe response DTOs

## Prompt to Agent
> Implement the templates listing API and public profile-by-slug API. Return only public-safe data. Do not render HTML on the backend. Seed 3 templates. Then stop and summarize.

---

# Task 7 — Stabilization / Cleanup

## Goal
Make backend MVP stronger.

## Includes
- improve validation
- tighten security rules
- add unit tests for parser utils
- add integration tests for auth/publish
- clean README
- clean API examples

## Prompt to Agent
> Review the backend MVP for production-appropriate cleanup: validation, tests for critical flows, security hardening for MVP, and README improvements. Avoid adding new features. Then stop and summarize.

---

# Agent Guardrail Reminder
At every step, remind the agent:
- stay in MVP scope
- do not overbuild
- stop after requested scope
- summarize what was done
- propose only the next logical step
