# Resume2Site Backend — Project Context

## Product Summary
Resume2Site is a \*\*resume-first platform\*\* that converts a user’s uploaded resume into an editable, hosted portfolio website.

This backend repository powers the MVP APIs for:
- resume upload
- resume parsing
- draft profile creation
- profile editing
- template metadata
- slug validation
- publishing
- public profile retrieval
- user authentication

## Core Product Flow
1. User visits the frontend landing page
2. User uploads a resume (PDF or DOCX)
3. Backend accepts the file without requiring login
4. Backend extracts text from the resume
5. Backend parses the resume into a structured draft profile
6. Frontend allows the user to review and edit the draft
7. User selects a portfolio template and previews it
8. User clicks Publish
9. Only then does the app ask the user to sign up or log in
10. After authentication, the draft is attached to the user account
11. User chooses a custom slug
12. Backend validates slug and marks the profile as published
13. Public profile becomes available at `/u/{slug}`
14. User can later log in, edit, change template, and republish

## What This Product Is
Resume2Site is:
- a \*\*resume-to-portfolio publishing platform\*\*
- not just a resume parser
- not just a generic website builder
- not just a portfolio template marketplace

## Core Product Rules (Non-Negotiable)
- Login must \*\*NOT\*\* be required before preview
- Resume upload and parsing must work for anonymous users
- Parsing does \*\*NOT\*\* need to be perfect
- All parsed data must be editable later
- Use a shared structured profile schema
- Use path-based public routes: `/u/{slug}`
- Do \*\*NOT\*\* use subdomains in MVP
- Do \*\*NOT\*\* generate real static websites in MVP
- MVP uses dynamic rendering from stored structured data
- Keep the product focused and scoped tightly

## MVP Features (Backend-Relevant)
- Resume upload (PDF/DOCX)
- Resume parsing into structured profile draft
- Draft profile CRUD APIs
- Authentication (signup/login/me)
- Slug validation/check
- Publish / republish flow
- Templates listing API
- Public profile retrieval by slug

## Tech Stack (Backend)
- Java 21 (or Java 17 if needed)
- Spring Boot 3.x
- PostgreSQL
- Maven
- Spring Data JPA / Hibernate
- Flyway
- Spring Security
- JWT auth
- Apache Tika
- PDFBox / Apache POI as needed

## Architectural Decisions
- Monolithic Spring Boot app for MVP
- Clean layered architecture
- REST APIs only
- PostgreSQL as primary persistence
- Use Flyway from the start
- Keep code maintainable and production-appropriate for MVP

## What NOT To Build in This Repo (Unless Explicitly Asked)
- Analytics
- Custom domains
- Subdomain routing
- Static site generation/export
- AI content rewriting
- Drag-and-drop page builder logic
- Microservices
- Event-driven distributed architecture
- Admin dashboards
- Multi-tenant enterprise features

## Goal for This Repo
Build a stable, clean, interview-quality backend that supports the full MVP flow with practical production patterns and strong maintainability.
