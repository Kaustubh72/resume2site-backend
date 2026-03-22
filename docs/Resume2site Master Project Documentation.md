# <a name="resume2site-master-project-documentation"></a>Resume2Site — Master Project Documentation
**Product Concept:** Upload a resume → parse the content → generate an editable portfolio website → let users publish it under a personalized public URL.

-----
# <a name="executive-summary"></a>1. Executive Summary
Resume2Site is a resume-first portfolio generation platform for students, software developers, and early-career professionals. Users upload a resume (PDF/DOCX), the system extracts structured profile data, generates a professional website preview, allows template selection and editing, and publishes the profile under a personalized URL.

This document serves as the **master product, project, technical, and user documentation** for the MVP and future versions of the product.

-----
# <a name="table-of-contents"></a>2. Table of Contents
1. Executive Summary
1. Product Vision & Conceptualization
1. Problem Statement & Opportunity
1. Target Audience & Personas
1. Product Positioning & Value Proposition
1. Scope Definition (MVP / V2 / V3)
1. Product Requirements Document (PRD)
1. Functional Requirements Specification (FRS)
1. User Stories & Use Cases
1. UX Flows & Screen Planning
1. Information Architecture / Content Model
1. Feature Matrix & Prioritization
1. System Architecture
1. Technical Design
1. Resume Parsing Engine Design
1. Template System Design
1. Routing & Slug Strategy
1. Authentication & Security
1. Database Design
1. API Specification
1. Deployment Architecture (Render)
1. Project Management Plan
1. Roadmap / Milestones / Sprint Plan
1. Risk Register & Decision Log
1. QA / Test Strategy
1. User Documentation
1. Operational Readiness & Monitoring
1. Success Metrics / KPIs
1. Future Enhancements
1. Appendix
-----
# <a name="x0ad2a4a1393e7d986919efebb686250aca1eccd"></a>2A. Functional Product Overview & Differentiation
## <a name="xf9598f43bf1658be3f051f402b0242d02a9209d"></a>2A.1 What the Application Is (Functional Overview)
Resume2Site is a **resume-first portfolio generation platform**. It is not just a resume parser and not just a generic website builder.

At a functional level, the application does the following: - accepts a user’s resume as the primary input - converts the resume into structured professional profile data - lets the user review and correct the extracted information - transforms that structured data into one of several professional portfolio website templates - allows the user to preview the website before creating an account - allows the user to publish the portfolio under a personalized public URL - gives the user a dashboard to update content, switch templates, and republish later

In simple terms: > **It converts a static resume into a live, editable, hosted professional profile website.**
## <a name="a.2-functional-product-definition"></a>2A.2 Functional Product Definition
Resume2Site should be understood as a combination of **five functional systems**:

1. **Resume Intake System**
   - file upload
   - validation
   - resume text extraction
1. **Profile Structuring System**
   - parsing raw resume content
   - converting unstructured text into structured profile data
   - organizing information into sections like summary, skills, experience, education, and projects
1. **Portfolio Generation System**
   - mapping structured profile data into UI templates
   - generating a polished portfolio preview instantly
   - enabling template switching without data loss
1. **Publishing System**
   - assigning a custom slug
   - making the portfolio publicly accessible
   - managing public/private lifecycle
1. **Profile Management System**
   - authenticated dashboard
   - edit/update content
   - change template
   - republish changes
## <a name="xfb82d1d6b50cb3c812c03ec25409aca98b30448"></a>2A.3 End-to-End Functional Flow (Full Application Flow)
The application flow should be understood as a **resume-to-public-profile pipeline**.
### <a name="x4b8d8c3ec10b76640a148604e888064f6db56a8"></a>Phase 1 — Anonymous Discovery & Creation (No Login Required)
1. User lands on the homepage
1. User understands the value proposition: *Upload resume → get portfolio website instantly*
1. User uploads a resume (PDF/DOCX)
1. System validates the file
1. System extracts text from the resume
1. System parses the resume into structured draft profile data
1. System creates a temporary draft profile
1. User is taken to a **Draft Review Screen**
### <a name="phase-2-structured-review-correction"></a>Phase 2 — Structured Review & Correction
9. User reviews extracted fields:
   - name
   - headline
   - summary
   - skills
   - experience
   - education
   - projects
   - links
9. User corrects mistakes or fills missing fields manually
9. User can hide/show sections
9. User saves or continues with the draft
### <a name="phase-3-portfolio-generation-preview"></a>Phase 3 — Portfolio Generation & Preview
13. User opens template gallery
13. User selects one of the available templates
13. System renders a live preview using the structured profile schema
13. User can:
- switch templates
- preview on desktop/mobile
- adjust content
- reorder or hide sections (if supported)
17. User reaches the “magic moment”: a professional portfolio is visible before signup
### <a name="x62f0ec505dfbfdb378997514fc75bfd2bf80a7e"></a>Phase 4 — Conversion to Registered User (Only at Publish Time)
18. User clicks **Publish**
18. If not logged in, the app prompts signup/login
18. Temporary draft is linked to the newly created or existing account
18. User chooses a custom URL slug
18. System validates slug and checks availability
18. User confirms publishing
### <a name="phase-5-public-portfolio-lifecycle"></a>Phase 5 — Public Portfolio Lifecycle
24. System marks the profile as published
24. Public page becomes available at:
- /u/{slug}
26. User gets a shareable URL
26. User can copy and share with recruiters, employers, or clients
### <a name="phase-6-post-publish-management"></a>Phase 6 — Post-Publish Management
28. User logs in later
28. Opens dashboard
28. Views owned profile(s)
28. Edits content
28. Switches template if desired
28. Saves and republishes
28. Public page reflects updated content
## <a name="a.4-functional-lifecycle-states"></a>2A.4 Functional Lifecycle States
The application should treat a profile as moving through lifecycle states:

1. **Uploaded**
   - resume file received
1. **Parsed**
   - draft data extracted
1. **Draft**
   - editable structured profile exists
1. **Previewed**
   - template selected and preview generated
1. **Authenticated**
   - draft attached to user account
1. **Published**
   - public URL is active
1. **Updated / Republished**
   - changes reflected on public profile
1. **Unpublished / Archived** (optional later)

This lifecycle model is important because it clearly defines what the application is doing beyond just “uploading a resume.”
## <a name="x24f86f107345308d044014b969cb9c1440fa426"></a>2A.5 What Makes This Product Different from Others
This is one of the most important strategic sections.

Resume2Site is **not** trying to compete head-on with generic website builders. Its differentiation comes from a focused workflow.
### <a name="it-is-not-just-a-generic-website-builder"></a>It is NOT just a generic website builder
Unlike Wix / Webflow / WordPress: - users do not start from a blank canvas - users do not design pages manually - users do not need to understand layout decisions - users do not need to set up hosting separately
### <a name="it-is-not-just-a-resume-parser"></a>It is NOT just a resume parser
Unlike a pure parser or ATS utility: - the goal is not only extracting data - the extracted data is immediately turned into a useful public asset - the output is a live professional website, not just JSON/text
### <a name="it-is-not-just-a-template-marketplace"></a>It is NOT just a template marketplace
Unlike static HTML portfolio templates: - the user doesn’t manually copy/paste resume content into templates - the platform creates structured data once, then reuses it across templates - switching templates later does not require re-entering information
## <a name="xd3596bbaaaf17a8f7e8c5f791d2104784018fe6"></a>2A.6 Core Differentiators (Strategic Product Differentiation)
### <a name="resume-first-workflow"></a>1. Resume-First Workflow
This is the biggest differentiator.

Most site builders ask: > “What do you want to build?”

Resume2Site asks: > “Upload what you already have, and we’ll turn it into a site.”

This drastically reduces friction.
### <a name="instant-preview-before-signup"></a>2. Instant Preview Before Signup
Most tools gate value behind signup.

Resume2Site should show the result first.

This creates a strong **wow moment** and improves adoption.
### <a name="x618892383fa3543dd96f27c62b07aca3807439b"></a>3. Structured Profile Schema + Template Independence
The product stores professional data in a normalized format, not inside a single template.

This means: - change template anytime - edit content once, reflect everywhere - future extensibility is much better

This is a strong architectural and product differentiator.
### <a name="job-seeker-recruiter-oriented-templates"></a>4. Job-Seeker / Recruiter-Oriented Templates
Generic builders optimize for “websites.” Resume2Site optimizes for: - credibility - professional presentation - recruiter scanning - skill visibility - project visibility - contact clarity

This is a different use case than normal site builders.
### <a name="x28edd3367c731262bd3218a2f696eba480aa250"></a>5. Publishable Public Identity with Minimal Setup
Users get: - hosting included - public route included - shareable link included - no deployment knowledge required

That makes it much more accessible.
## <a name="a.7-competitive-positioning-summary"></a>2A.7 Competitive Positioning Summary
### <a name="against-wix-webflow-wordpress"></a>Against Wix / Webflow / WordPress
**Resume2Site wins on:** - lower friction - faster time to first result - job-seeker-specific workflow - simpler publish path
### <a name="x5872b9b17a31e9f3e271c6e21c730b7e5410dbb"></a>Against static portfolio templates / GitHub Pages
**Resume2Site wins on:** - no manual content migration - no deployment knowledge required - template switching later - integrated public URL flow
### <a name="against-resume-parsers-ats-tools"></a>Against resume parsers / ATS tools
**Resume2Site wins on:** - user-visible outcome - portfolio creation instead of raw extraction - long-term editable hosted asset
## <a name="xdb01756158584ebe014053aff6432fcf1356913"></a>2A.8 One-Line Functional Description (Use This Everywhere)
**Resume2Site is a resume-first platform that converts a user’s uploaded resume into an editable, hosted portfolio website with instant preview, template selection, and one-click publishing under a personalized public URL.**
## <a name="a.9-product-category-definition"></a>2A.9 Product Category Definition
If someone asks “What kind of product is this?”, the best answer is:

**A resume-to-portfolio publishing platform for job seekers.**

Not: - only a resume parser - only a portfolio builder - only a website generator

This framing is much stronger.
## <a name="x434ffe04a5542ac40df24c4e4a4edb198444e0e"></a>2A.10 Recommended Product Messaging (For README / Landing Page / Interview)
### <a name="short-version"></a>Short version
**Turn your resume into a professional portfolio website in minutes.**
### <a name="stronger-version"></a>Stronger version
**Upload your resume, review the extracted details, choose a template, and instantly publish a recruiter-friendly portfolio website under your own shareable URL.**
## <a name="a.11-why-this-section-matters"></a>2A.11 Why This Section Matters
This section should exist because it explains: - what the product really is - how the user experiences it end-to-end - what functional systems exist inside the app - why it is different from existing alternatives - why it is more than just a parser or a template picker

This is the section that makes the project feel like a real product instead of just a technical build.

-----
# <a name="product-vision-conceptualization"></a>3. Product Vision & Conceptualization
## <a name="product-name"></a>3.1 Product Name
**Resume2Site** (working title)

Alternative names: - CV2Portfolio - Portfolify - HirePage - ResumeWeb - QuickPortfolio
## <a name="vision-statement"></a>3.2 Vision Statement
To help job seekers instantly transform their resume into a professional, shareable online portfolio website without needing design, coding, or hosting knowledge.
## <a name="mission-statement"></a>3.3 Mission Statement
Build a fast, resume-first portfolio generation platform that allows users to upload a resume, preview a professional portfolio, customize it, and publish it under a personalized URL in minutes.
## <a name="core-product-promise"></a>3.4 Core Product Promise
- Upload resume
- Auto-extract profile data
- Preview a professional website instantly
- Edit and customize content
- Select a template
- Publish with a personalized public link
- Return later to update and republish
## <a name="long-term-product-direction"></a>3.5 Long-Term Product Direction
Resume2Site should evolve from a simple website generator into a **career profile platform** that helps users: - maintain a live professional profile - tailor versions for different job roles - track portfolio views - sync resume changes over time - optionally connect GitHub/LinkedIn - later use custom domains

-----
# <a name="problem-statement-opportunity"></a>4. Problem Statement & Opportunity
## <a name="problem-statement"></a>4.1 Problem Statement
Many job seekers already have a resume PDF but do not have a personal website or portfolio because: - building a website requires technical/design skills - choosing templates/layouts is time-consuming - hosting/domain setup is confusing - manually converting resume content into a website is tedious - updating a personal site is harder than updating a resume

As a result: - users rely only on static resumes and LinkedIn - they miss the chance to present projects and experience better - recruiters get less engaging professional profiles
## <a name="opportunity-statement"></a>4.2 Opportunity Statement
There is a strong opportunity to create a **resume-first portfolio builder** optimized specifically for job seekers, where users upload a resume and get an editable, hosted portfolio preview in minutes.

This is attractive because: - users already have the source data (resume) - the workflow aligns with job applications - time-to-value is extremely fast - the “wow factor” is high - the product is easier to adopt than generic website builders
## <a name="why-this-product-can-win"></a>4.3 Why This Product Can Win
Unlike generic site builders, Resume2Site is: - **resume-first** (starts from existing data) - **job-seeker focused** (not generic websites) - **fast** (preview before login) - **template-driven** (no drag/drop complexity) - **editable** (users can fix parsing mistakes) - **publishable** (hosted shareable link included)

-----
# <a name="target-audience-personas"></a>5. Target Audience & Personas
## <a name="primary-target-audience-mvp"></a>5.1 Primary Target Audience (MVP)
**Primary audience for MVP:** 1. Final-year students / freshers 2. Software developers (0–5 YOE) 3. Early-career tech professionals

**Reason for focusing here:** - strong need for portfolio presence - already have resumes + GitHub/LinkedIn - likely to understand the value of a shareable public profile - easier to design templates for a consistent audience
## <a name="secondary-audience-later"></a>5.2 Secondary Audience (Later)
- Freelancers
- Designers
- Product managers
- Analysts / consultants
## <a name="persona-1-final-year-cs-student"></a>5.3 Persona 1 — Final-Year CS Student
**Name:** Aakash (representative persona) - Age: 21 - Has: resume PDF, GitHub, LinkedIn - Goal: send a professional portfolio link in job applications - Pain points: - no time to build a website - no design skills - doesn’t know hosting - Success outcome: - publishes a portfolio in under 10 minutes
## <a name="persona-2-early-career-java-developer"></a>5.4 Persona 2 — Early-Career Java Developer
**Name:** Kaustubh-like developer persona - Age: 24–30 - Has: resume, project experience, tech stack, maybe GitHub - Goal: stronger professional presence while applying for jobs - Needs: - clean recruiter-friendly profile - project showcase - custom shareable link - ability to update later
## <a name="persona-3-freelancer-v2"></a>5.5 Persona 3 — Freelancer (V2)
- Wants a project-first page
- Needs service/contact CTA
- Wants a polished shareable link
-----
# <a name="product-positioning-value-proposition"></a>6. Product Positioning & Value Proposition
## <a name="positioning-statement"></a>6.1 Positioning Statement
For students, developers, and job seekers who want a professional online presence, Resume2Site is a resume-first portfolio builder that converts an uploaded resume into an editable, hosted personal website in minutes — unlike generic website builders, it is optimized specifically for career and recruiter use cases.
## <a name="value-proposition"></a>6.2 Value Proposition
### <a name="customer-jobs"></a>Customer Jobs
- Apply to jobs
- Share profile with recruiters
- Showcase skills/projects
- Build professional credibility
### <a name="customer-pains"></a>Customer Pains
- No portfolio website
- Building one manually is hard
- Generic builders are overwhelming
- Resume is static and limited
### <a name="customer-gains"></a>Customer Gains
- Instant professional online profile
- Faster publishing
- Better visual presentation than PDF
- Easy updates over time
- Personalized public URL
### <a name="product-pain-relievers"></a>Product Pain Relievers
- Resume upload
- Auto parsing
- Structured editable review
- Professional templates
- One-click publish
### <a name="product-gain-creators"></a>Product Gain Creators
- Custom slug
- Mobile-friendly design
- Template switching later
- Future analytics
- Future GitHub enrichment
-----
# <a name="scope-definition-mvp-v2-v3"></a>7. Scope Definition (MVP / V2 / V3)
## <a name="mvp-scope-must-have"></a>7.1 MVP Scope (Must Have)
- Resume upload (PDF/DOCX)
- Text extraction + rule-based parsing
- Structured draft profile creation
- Editable review form for parsed data
- 3 high-quality templates
- Live preview
- Login/signup only when publishing
- Custom slug selection
- Public portfolio page under app domain
- Dashboard for editing profile later
- Template switching after publish
- Republish after edits
## <a name="explicit-mvp-non-goals"></a>7.2 Explicit MVP Non-Goals
- Custom domains
- Drag-and-drop page builder
- AI chatbot / generative rewrite for all sections
- Advanced analytics
- Multi-language support
- Blog support
- Subdomain routing
- True static file export generation
- Perfect ATS-grade parsing
## <a name="v2-scope-high-value"></a>7.3 V2 Scope (High Value)
- GitHub link enrichment
- Better “About Me” auto-suggestion
- Basic page analytics
- Download HTML/ZIP export
- SEO metadata improvements
- Resume re-upload and compare changes
- Resume download link on public page (optional)
## <a name="v3-scope-advanced"></a>7.4 V3 Scope (Advanced)
- Custom domains
- Wildcard subdomain support
- Multiple portfolio variants for different job roles
- AI content enhancement
- Portfolio version history
- Admin template marketplace
- Team/collaboration support
-----
# <a name="product-requirements-document-prd"></a>8. Product Requirements Document (PRD)
## <a name="product-overview"></a>8.1 Product Overview
Resume2Site is a web application that converts resumes into editable, hosted portfolio websites.
### <a name="high-level-flow"></a>High-Level Flow
1. User uploads resume
1. System extracts structured data
1. User reviews and edits parsed data
1. User chooses a template
1. User previews portfolio
1. User signs up/logs in only when publishing
1. User selects a custom slug
1. Portfolio is published to a public route
1. User can later log in, edit, change template, and republish
## <a name="product-goals"></a>8.2 Product Goals
- Reduce time from resume to public portfolio to under 5 minutes for MVP users
- Provide a clear “magic moment” with instant preview
- Create a professional-looking shareable page with minimal effort
- Ensure parsing mistakes are recoverable via editing
- Build a technically strong, extensible platform
## <a name="product-non-goals"></a>8.3 Product Non-Goals
- Compete with full website builders (Wix/Webflow)
- Provide a full CMS/blog platform
- Offer perfect resume parsing across all formats initially
- Build a generic page builder
## <a name="primary-success-criteria"></a>8.4 Primary Success Criteria
- User reaches preview without account creation
- Upload → preview works reliably for supported files
- Published page accessible immediately after slug selection
- User can edit and republish later
- Templates are professional and mobile-friendly
## <a name="core-user-stories"></a>8.5 Core User Stories
- As a user, I want to upload my resume so the app can pre-fill my portfolio.
- As a user, I want to review and edit extracted data before publishing.
- As a user, I want to switch between templates and preview changes instantly.
- As a user, I want to sign up only when I decide to publish.
- As a user, I want a custom URL slug to share with recruiters.
- As a user, I want to log in later and update my portfolio.
- As a user, I want to change templates later without re-entering my data.
## <a name="constraints"></a>8.6 Constraints
- Tech stack: Java (Spring Boot), Angular, PostgreSQL
- Hosting target: Render
- Solo-developer friendly MVP
- Controlled scope
-----
# <a name="x073f6abf736256c2a78c933ff8d5d7e35267bd4"></a>9. Functional Requirements Specification (FRS)
## <a name="module-1-resume-upload"></a>9.1 Module 1 — Resume Upload
### <a name="requirements"></a>Requirements
- Accept PDF and DOCX files
- Max file size configurable (recommended 5 MB initially)
- Validate MIME type and extension
- Reject unsupported formats gracefully
- Persist upload metadata
- Support temporary draft flow for unauthenticated users
### <a name="validation-rules"></a>Validation Rules
- Allowed: .pdf, .docx
- Optional later: .doc
- Max size exceeded → user-friendly error
## <a name="module-2-resume-parsing"></a>9.2 Module 2 — Resume Parsing
### <a name="requirements-1"></a>Requirements
- Extract raw text from uploaded file
- Normalize formatting
- Detect sections (summary, skills, experience, education, projects)
- Extract structured fields:
  - name
  - email
  - phone
  - location (best effort)
  - links (LinkedIn, GitHub, portfolio)
  - summary
  - skills
  - experience blocks
  - education blocks
  - project blocks
- Create a draft profile
- Allow partial parsing (missing sections acceptable)
### <a name="important-product-rule"></a>Important Product Rule
Parsing does **not** need to be perfect. The system should aim for “useful pre-fill,” not full automation.
## <a name="module-3-draft-review-editing"></a>9.3 Module 3 — Draft Review & Editing
### <a name="requirements-2"></a>Requirements
- Show parsed data in editable structured form
- Allow users to add missing fields manually
- Allow section hide/show toggles
- Preserve draft before login
- Allow users to proceed even if parsing is incomplete
## <a name="module-4-template-selection"></a>9.4 Module 4 — Template Selection
### <a name="requirements-3"></a>Requirements
- Display at least 3 templates
- Provide template thumbnails and short descriptions
- Templates must consume the same shared profile schema
- Switching templates should not lose content
## <a name="module-5-live-preview"></a>9.5 Module 5 — Live Preview
### <a name="requirements-4"></a>Requirements
- Show real-time preview of selected template
- Reflect edits immediately or near real-time
- Support mobile and desktop preview layouts
- Fast load time
## <a name="module-6-authentication"></a>9.6 Module 6 — Authentication
### <a name="requirements-5"></a>Requirements
- Signup with email + password (MVP)
- Login with email + password
- Password hashing
- JWT or session-based auth
- Trigger auth only when user clicks Publish
- Draft should be attachable to account after login/signup
## <a name="module-7-publishing"></a>9.7 Module 7 — Publishing
### <a name="requirements-6"></a>Requirements
- User chooses slug
- Slug availability check required
- Slug validation rules enforced
- Profile becomes public on publish
- Save published profile + selected template + visibility state
- Return final public URL
## <a name="module-8-dashboard"></a>9.8 Module 8 — Dashboard
### <a name="requirements-7"></a>Requirements
- View owned profiles (MVP may support only one profile, but design should allow multiple)
- Edit profile content
- Change template
- Update slug (with rules)
- Republish
- Unpublish (optional MVP or V2)
## <a name="module-9-public-portfolio-rendering"></a>9.9 Module 9 — Public Portfolio Rendering
### <a name="requirements-8"></a>Requirements
- Public route accessible without login
- Page renders selected template using stored structured data
- SEO-friendly title/description best effort
- Responsive layout
- Safe rendering (no XSS)
-----
# <a name="user-stories-use-cases"></a>10. User Stories & Use Cases
## <a name="user-stories-by-epic"></a>10.1 User Stories by Epic
### <a name="epic-a-create-portfolio-from-resume"></a>Epic A — Create Portfolio from Resume
- As a user, I want to upload a resume so the system can create a draft profile.
- As a user, I want the system to parse my resume so I don’t need to type everything manually.
- As a user, I want to edit parsed data before publishing.
### <a name="epic-b-customize-portfolio"></a>Epic B — Customize Portfolio
- As a user, I want to choose a professional template.
- As a user, I want to preview different templates instantly.
- As a user, I want to hide sections I don’t want to display.
### <a name="epic-c-publish-share"></a>Epic C — Publish & Share
- As a user, I want a personalized public URL.
- As a user, I want to sign up only when I’m ready to publish.
- As a user, I want to come back later and update my profile.
## <a name="xb39b2dbb0621d45dfd97a43f7b242a73ad2c5a0"></a>10.2 Core Use Case — Upload Resume and Generate Draft
**Actor:** Visitor

**Preconditions:** - User is on landing page - File available in supported format

**Main Flow:** 1. User clicks Upload Resume 2. User selects PDF/DOCX file 3. System validates file 4. System stores file metadata / temp reference 5. System extracts text 6. System parses data into structured draft 7. System shows editable draft screen

**Alternate Flows:** - Invalid file type → show error - Parsing partial success → show what was extracted and let user continue

**Postconditions:** - Draft profile exists
## <a name="core-use-case-publish-portfolio"></a>10.3 Core Use Case — Publish Portfolio
**Actor:** Visitor / Authenticated User

**Preconditions:** - Draft exists - Template selected

**Main Flow:** 1. User clicks Publish 2. If unauthenticated, prompt login/signup 3. System attaches draft to user account 4. User enters desired slug 5. System validates slug and checks availability 6. User confirms publish 7. System marks profile published 8. System returns public URL

**Alternate Flows:** - Slug taken → suggest alternatives - Session expired → redirect to login

**Postconditions:** - Public page accessible
## <a name="core-use-case-edit-and-republish"></a>10.4 Core Use Case — Edit and Republish
**Actor:** Authenticated User

**Main Flow:** 1. User logs in 2. Opens dashboard 3. Selects profile 4. Edits content or changes template 5. Clicks Save / Republish 6. Public page reflects updated content

-----
# <a name="ux-flows-screen-planning"></a>11. UX Flows & Screen Planning
## <a name="core-ux-principle"></a>11.1 Core UX Principle
**Do not require login before preview.**

This is a key product differentiator and should remain a foundational UX rule.
## <a name="primary-user-flow"></a>11.2 Primary User Flow
Landing → Upload Resume → Parsing State → Draft Review/Edit → Template Gallery → Live Preview → Publish → Login/Signup (if needed) → Slug Selection → Publish Success → Public Portfolio
## <a name="returning-user-flow"></a>11.3 Returning User Flow
Login → Dashboard → Select Profile → Edit Data / Change Template → Preview → Republish
## <a name="screen-inventory"></a>11.4 Screen Inventory
1. Landing Page
1. Resume Upload Screen
1. Parsing / Loading Screen
1. Draft Review Screen
1. Template Gallery
1. Live Preview Screen
1. Login / Signup Modal or Page
1. Slug Selection Screen
1. Publish Success Screen
1. Dashboard
1. Profile Edit Screen
1. Public Portfolio Page
1. 404 / Invalid Slug Page
1. Error / Retry Screens
## <a name="screen-level-notes"></a>11.5 Screen-Level Notes
### <a name="landing-page"></a>Landing Page
- Headline: “Turn your resume into a portfolio website in minutes”
- CTA: Upload Resume
- Optional secondary CTA: View demo
### <a name="draft-review-screen"></a>Draft Review Screen
- Structured sections
- Editable inputs
- Add missing items manually
- Section toggles
- CTA: Continue to Templates
### <a name="template-gallery"></a>Template Gallery
- 3 strong templates only for MVP
- Show “Best for recruiters”, “Best for developers”, etc.
### <a name="live-preview"></a>Live Preview
- Template switcher
- Device toggle (desktop/mobile preview)
- CTA: Publish
### <a name="publish-success"></a>Publish Success
- Show public URL
- Copy link CTA
- Go to dashboard CTA
-----
# <a name="information-architecture-content-model"></a>12. Information Architecture / Content Model
## <a name="core-design-principle"></a>12.1 Core Design Principle
All templates must use a **shared structured profile schema**.

This is critical for: - template switching - data consistency - maintainability - API simplicity
## <a name="core-profile-schema"></a>12.2 Core Profile Schema
### <a name="profile"></a>Profile
- id
- userId
- resumeId (optional)
- fullName
- headline
- summary
- email
- phone
- location
- profileImageUrl (later)
- resumePublicUrl (optional later)
- templateId
- slug
- isPublished
- createdAt
- updatedAt
### <a name="links"></a>Links
- type (linkedin/github/portfolio/leetcode/other)
- url
- displayText
- sortOrder
### <a name="skills"></a>Skills
- skillName
- category (optional)
- sortOrder
### <a name="experience"></a>Experience
- company
- title
- startDate
- endDate
- isCurrent
- location (optional)
- description
- sortOrder
### <a name="education"></a>Education
- institution
- degree
- fieldOfStudy
- startYear
- endYear
- grade
- sortOrder
### <a name="projects"></a>Projects
- name
- description
- githubUrl
- demoUrl
- techStack
- sortOrder
### <a name="x511d1b6a5d4e9a21985a755ef1c48a51a750f69"></a>Certifications (optional MVP or schema-ready)
- name
- issuer
- issueDate
- credentialUrl
-----
# <a name="feature-matrix-prioritization"></a>13. Feature Matrix & Prioritization
## <a name="priority-definitions"></a>13.1 Priority Definitions
- **P0** = must-have for MVP
- **P1** = important, can be V2 if needed
- **P2** = optional / later
## <a name="feature-matrix"></a>13.2 Feature Matrix

|Feature|Description|Release|Priority|Complexity|Notes|
| :- | :- | -: | -: | -: | :- |
|Resume Upload|PDF/DOCX upload|MVP|P0|Medium|Core entry point|
|Resume Parsing|Extract structured draft|MVP|P0|High|Rule-based first|
|Editable Draft|Fix parsed data|MVP|P0|Medium|Mandatory for reliability|
|Template Gallery|3 templates|MVP|P0|Medium|Quality over quantity|
|Live Preview|Real-time preview|MVP|P0|Medium|Key wow factor|
|Login/Signup|Auth only at publish step|MVP|P0|Medium|Strong UX decision|
|Slug Selection|Personalized URL|MVP|P0|Medium|Must validate uniqueness|
|Public Portfolio|Shareable public page|MVP|P0|Medium|Public route|
|Dashboard|Edit later|MVP|P0|Medium|Important for retention|
|Template Switch Later|Change template post-publish|MVP|P0|Low|Enabled by shared schema|
|Analytics|Views/click tracking|V2|P1|Medium|Strong value add|
|GitHub Enrichment|Pull repos/links|V2|P1|Medium|Great for dev audience|
|HTML Export|Download site snapshot|V2|P1|Medium|Trust and portability|
|Resume Re-upload Diff|Compare updates|V2|P1|High|Nice differentiator|
|Multiple Variants|Tailored versions|V3|P2|High|Strong SaaS feature|
|Custom Domain|User domain mapping|V3|P2|High|Infra complexity|
|Subdomains|user.app.com|V3|P2|High|Avoid initially|

-----
# <a name="system-architecture"></a>14. System Architecture
## <a name="recommended-mvp-architecture"></a>14.1 Recommended MVP Architecture
**Frontend:** Angular **Backend:** Spring Boot (Java) **Database:** PostgreSQL **Hosting:** Render
## <a name="high-level-architecture"></a>14.2 High-Level Architecture
1. Angular SPA handles upload, draft editing, template preview, dashboard
1. Spring Boot API handles:
   - auth
   - uploads
   - parsing
   - draft/profile persistence
   - slug validation
   - publish lifecycle
   - public profile data APIs
1. PostgreSQL stores users, drafts, profiles, sections, templates metadata
1. Public pages are rendered using route-based profile data under the same app domain
## <a name="key-architectural-decision"></a>14.3 Key Architectural Decision
**MVP should use dynamic rendering of portfolio pages from stored structured data**, not actual static file generation.
### <a name="why-this-is-the-right-choice"></a>Why this is the right choice
- easier to implement
- easier to update and republish
- easier to manage on Render
- simpler routing
- no complex file build pipeline required

**Later:** add optional static HTML export if needed.
## <a name="recommended-public-url-pattern"></a>14.4 Recommended Public URL Pattern
Use path-based URLs first: - yourapp.com/u/kaustubh - yourapp.com/u/kaustubh-singh

Avoid wildcard subdomains initially.

-----
# <a name="technical-design"></a>15. Technical Design
## <a name="backend-modules"></a>15.1 Backend Modules
1. Auth Module
1. Resume Upload Module
1. File Extraction Module
1. Resume Parser Module
1. Draft/Profile Module
1. Template Metadata Module
1. Slug / Publish Module
1. Public Profile Module
1. Dashboard / Profile Management Module
1. Optional Analytics Module (V2)
## <a name="frontend-modules"></a>15.2 Frontend Modules
1. Landing / Marketing Module
1. Upload Module
1. Parsing State Module
1. Draft Editor Module
1. Template Gallery Module
1. Preview Module
1. Auth Module
1. Publish Flow Module
1. Dashboard Module
1. Public Profile Rendering Module
## <a name="suggested-backend-package-structure"></a>15.3 Suggested Backend Package Structure
com.resume2site\
` `├── auth\
` `├── resume\
` `│    ├── upload\
` `│    ├── extraction\
` `│    ├── parsing\
` `│    └── dto\
` `├── profile\
` `│    ├── draft\
` `│    ├── publish\
` `│    ├── publicview\
` `│    └── dto\
` `├── template\
` `├── slug\
` `├── common\
` `├── config\
` `└── infra
## <a name="x39caac991ef9e4b70951693ad82af331dbbc7f1"></a>15.4 Suggested Frontend Feature Structure (Angular)
src/app\
` `├── core\
` `├── shared\
` `├── features\
` `│    ├── landing\
` `│    ├── upload\
` `│    ├── draft-editor\
` `│    ├── templates\
` `│    ├── preview\
` `│    ├── auth\
` `│    ├── publish\
` `│    ├── dashboard\
` `│    └── public-profile\
` `└── layouts

-----
# <a name="resume-parsing-engine-design"></a>16. Resume Parsing Engine Design
## <a name="core-principle"></a>16.1 Core Principle
The parser should aim for **highly usable pre-fill**, not perfect automation.
## <a name="parsing-pipeline"></a>16.2 Parsing Pipeline
1. File validation
1. Type detection
1. Text extraction
1. Text normalization
1. Section detection
1. Field extraction
1. Draft profile assembly
1. Confidence tagging (optional)
1. Manual review/edit
## <a name="text-extraction-strategy"></a>16.3 Text Extraction Strategy
### <a name="pdf"></a>PDF
- Primary: Apache Tika
- Fallback/low-level support: Apache PDFBox
### <a name="docx"></a>DOCX
- Apache Tika
- Optional direct parsing: Apache POI
### <a name="future-ocr-support-v2"></a>Future OCR Support (V2+)
- For scanned PDFs/images: Tess4J / Tesseract
## <a name="normalization-rules"></a>16.4 Normalization Rules
- Normalize line breaks
- Collapse excessive spaces
- Normalize bullets
- Normalize dashes (-, –, —)
- Preserve line order as much as possible
- Remove obvious noise
## <a name="section-detection-strategy"></a>16.5 Section Detection Strategy
Look for headings like: - Summary / Objective / Profile - Skills / Technical Skills - Experience / Work Experience / Professional Experience - Education - Projects - Certifications

Use line-based scanning and assign content until next heading.
## <a name="field-extraction-strategy"></a>16.6 Field Extraction Strategy
### <a name="easy-fields-regex"></a>Easy Fields (Regex)
- email
- phone
- links
### <a name="semi-heuristic-fields"></a>Semi-Heuristic Fields
- name (top lines, filter out email/phone/link lines)
- location (best effort)
- headline (derived from top content or role lines)
### <a name="section-specific-extraction"></a>Section-Specific Extraction
- Skills: dictionary-based matching + section tokenization
- Experience: date-range-driven block detection
- Education: degree/institution/year heuristics
- Projects: title + link + bullet grouping
## <a name="parsing-failure-strategy"></a>16.7 Parsing Failure Strategy
- Partial parsing is acceptable
- Missing fields should not block progress
- User must always be able to add/edit manually
## <a name="x8c13db6993a0addae0c91e43ad236e821580ef2"></a>16.8 Parsing Confidence (Optional MVP, Strong V2)
Each extracted field can have: - HIGH - MEDIUM - LOW

This can help visually highlight fields that may need review.
## <a name="important-real-world-constraints"></a>16.9 Important Real-World Constraints
- Multi-column resumes may parse poorly
- Fancy PDF layouts can scramble text order
- Tables/icons/sidebar content may reduce quality
- OCR may be needed later

**Product response:** make review/edit the core safety net.

-----
# <a name="template-system-design"></a>17. Template System Design
## <a name="core-rule"></a>17.1 Core Rule
Templates must be **schema-driven** and consume the same shared profile model.
## <a name="why-this-matters"></a>17.2 Why This Matters
- template switching becomes easy
- lower maintenance
- no data duplication
- easier scaling to more templates
## <a name="mvp-template-strategy"></a>17.3 MVP Template Strategy
Only **3 strong templates**.
### <a name="template-1-recruiter-classic"></a>Template 1 — Recruiter Classic
Best for: - experienced professionals - recruiter sharing - simple, clean, text-first
### <a name="template-2-developer-showcase"></a>Template 2 — Developer Showcase
Best for: - students / developers - project-heavy profiles - GitHub-forward layout
### <a name="template-3-modern-compact"></a>Template 3 — Modern Compact
Best for: - mobile-friendly quick scan - strong headline + concise sections
## <a name="template-configuration-model"></a>17.4 Template Configuration Model
Each template should define: - templateKey - displayName - description - defaultSectionOrder - supportedSections - themeOptions - previewImageUrl - isActive
## <a name="section-visibility-rules"></a>17.5 Section Visibility Rules
User can toggle: - summary - skills - experience - education - projects - certifications - links
## <a name="important-design-constraint"></a>17.6 Important Design Constraint
**Do not build a drag-and-drop page builder in MVP.**

Templates should be controlled and structured.

-----
# <a name="routing-slug-strategy"></a>18. Routing & Slug Strategy
## <a name="recommended-mvp-routing"></a>18.1 Recommended MVP Routing
Path-based routes only: - /u/{slug}

Example: - /u/kaustubh - /u/kaustubh-singh
## <a name="why-path-based-is-best-for-mvp"></a>18.2 Why Path-Based is Best for MVP
- easiest on Render
- easier SSL / DNS
- easier frontend/backend routing
- fewer infra surprises
- easier debugging
## <a name="slug-rules"></a>18.3 Slug Rules
- lowercase only
- alphanumeric + hyphen
- length: 3–40 chars
- cannot start/end with hyphen
- no consecutive hyphens (optional normalization)
## <a name="reserved-words"></a>18.4 Reserved Words
Prevent these: - admin - api - login - signup - dashboard - public - help - templates - assets - static
## <a name="slug-collision-strategy"></a>18.5 Slug Collision Strategy
If taken, suggest: - {name}-dev - {name}-{lastname} - {name}-{year} - {name}-{random-short} (only if needed)
## <a name="slug-update-policy"></a>18.6 Slug Update Policy
Recommended MVP: - allow change - validate uniqueness again - old URL may break unless redirect table is introduced (optional later)

-----
# <a name="authentication-security"></a>19. Authentication & Security
## <a name="auth-strategy-mvp"></a>19.1 Auth Strategy (MVP)
- Email + password signup/login
- JWT-based auth or secure session cookies
## <a name="strong-product-rule"></a>19.2 Strong Product Rule
**Auth is required only at publish time**, not before preview.
## <a name="security-requirements"></a>19.3 Security Requirements
### <a name="passwords"></a>Passwords
- Hash with BCrypt/Argon2
- Never store plain text
### <a name="upload-security"></a>Upload Security
- Validate MIME type and extension
- Limit file size
- Store outside web root if applicable
- Do not execute uploaded content
### <a name="authorization"></a>Authorization
- Users can only edit their own profiles
- Slug changes restricted to owner
- Draft ownership transfer after signup/login
### <a name="xss-prevention"></a>XSS Prevention
Critical because users can edit content. - sanitize any rich content (if rich text exists) - if plain text only, escape output safely - never trust user input in rendered HTML
### <a name="csrf-cors"></a>CSRF / CORS
- If using cookies: protect against CSRF
- If SPA + JWT: configure CORS carefully
### <a name="rate-limiting-recommended"></a>Rate Limiting (Recommended)
- login attempts
- signup attempts
- slug availability endpoint (to avoid abuse)
## <a name="privacy-considerations"></a>19.4 Privacy Considerations
- Clarify whether original resume files are stored and for how long
- Consider deleting temp drafts/files after a TTL if user never signs up
- Public page data should only show fields the user explicitly leaves enabled
-----
# <a name="database-design"></a>20. Database Design
## <a name="core-principles"></a>20.1 Core Principles
- Store **normalized structured profile data**
- Avoid storing only raw resume text as the main source of truth
- Design for 1 profile per user initially, but keep schema capable of multiple profiles later
## <a name="tables"></a>20.2 Tables
## <a name="users"></a>20.2.1 users

|Column|Type|Notes|
| :- | :- | :- |
|id|UUID / BIGSERIAL|PK|
|email|VARCHAR|unique|
|password\_hash|VARCHAR|hashed|
|created\_at|TIMESTAMP||
|updated\_at|TIMESTAMP||
## <a name="resume_uploads"></a>20.2.2 resume\_uploads

|Column|Type|Notes|
| :- | :- | :- |
|id|UUID / BIGSERIAL|PK|
|user\_id|FK nullable|may be null before login|
|original\_file\_name|VARCHAR||
|file\_type|VARCHAR|pdf/docx|
|storage\_ref|VARCHAR|path/blob reference|
|upload\_status|VARCHAR|uploaded/processed/failed|
|created\_at|TIMESTAMP||
## <a name="profile_drafts"></a>20.2.3 profile\_drafts

|Column|Type|Notes|
| :- | :- | :- |
|id|UUID / BIGSERIAL|PK|
|user\_id|FK nullable|null before auth|
|resume\_upload\_id|FK nullable||
|full\_name|VARCHAR||
|headline|VARCHAR||
|summary|TEXT||
|email|VARCHAR||
|phone|VARCHAR||
|location|VARCHAR||
|template\_id|FK nullable|chosen template|
|is\_published|BOOLEAN|false until publish|
|created\_at|TIMESTAMP||
|updated\_at|TIMESTAMP||
## <a name="profiles"></a>20.2.4 profiles
(Recommended: either merge with profile\_drafts or separate publishable entity. For MVP, **one table can work** if simpler.)

**Simpler MVP recommendation:** use one main profiles table instead of separate draft/published tables, with status column.
### <a name="profiles-1"></a>profiles

|Column|Type|Notes|
| :- | :- | :- |
|id|UUID / BIGSERIAL|PK|
|user\_id|FK|owner|
|resume\_upload\_id|FK nullable|source resume|
|status|VARCHAR|draft/published|
|full\_name|VARCHAR||
|headline|VARCHAR||
|summary|TEXT||
|email|VARCHAR||
|phone|VARCHAR||
|location|VARCHAR||
|template\_id|FK||
|slug|VARCHAR|unique nullable until publish|
|is\_published|BOOLEAN||
|created\_at|TIMESTAMP||
|updated\_at|TIMESTAMP||
## <a name="profile_links"></a>20.2.5 profile\_links

|Column|Type|Notes|
| :- | :- | :- |
|id|UUID / BIGSERIAL|PK|
|profile\_id|FK||
|link\_type|VARCHAR|linkedin/github/other|
|url|TEXT||
|display\_text|VARCHAR|optional|
|sort\_order|INT||
## <a name="profile_skills"></a>20.2.6 profile\_skills

|Column|Type|Notes|
| :- | :- | :- |
|id|UUID / BIGSERIAL|PK|
|profile\_id|FK||
|skill\_name|VARCHAR||
|category|VARCHAR nullable|optional|
|sort\_order|INT||
## <a name="profile_experiences"></a>20.2.7 profile\_experiences

|Column|Type|Notes|
| :- | :- | :- |
|id|UUID / BIGSERIAL|PK|
|profile\_id|FK||
|company|VARCHAR||
|title|VARCHAR||
|start\_date|DATE nullable||
|end\_date|DATE nullable||
|is\_current|BOOLEAN||
|location|VARCHAR nullable||
|description|TEXT||
|sort\_order|INT||
## <a name="profile_education"></a>20.2.8 profile\_education

|Column|Type|Notes|
| :- | :- | :- |
|id|UUID / BIGSERIAL|PK|
|profile\_id|FK||
|institution|VARCHAR||
|degree|VARCHAR||
|field\_of\_study|VARCHAR nullable||
|start\_year|INT nullable||
|end\_year|INT nullable||
|grade|VARCHAR nullable||
|sort\_order|INT||
## <a name="profile_projects"></a>20.2.9 profile\_projects

|Column|Type|Notes|
| :- | :- | :- |
|id|UUID / BIGSERIAL|PK|
|profile\_id|FK||
|name|VARCHAR||
|description|TEXT||
|github\_url|TEXT nullable||
|demo\_url|TEXT nullable||
|tech\_stack|TEXT nullable|comma-separated or normalized later|
|sort\_order|INT||
## <a name="profile_sections"></a>20.2.10 profile\_sections
Optional helper table if you want section visibility/order to be dynamic.

|Column|Type|Notes|
| :- | :- | :- |
|id|UUID / BIGSERIAL|PK|
|profile\_id|FK||
|section\_key|VARCHAR|summary/skills/…|
|is\_visible|BOOLEAN||
|sort\_order|INT||
## <a name="templates"></a>20.2.11 templates

|Column|Type|Notes|
| :- | :- | :- |
|id|UUID / BIGSERIAL|PK|
|template\_key|VARCHAR|unique|
|name|VARCHAR||
|description|TEXT||
|preview\_image\_url|TEXT||
|config\_json|JSONB|section order/theme metadata|
|is\_active|BOOLEAN||
## <a name="slug_history-v2-optional"></a>20.2.12 slug\_history (V2+ optional)
For redirects if slug changes later.
## <a name="page_views-v2"></a>20.2.13 page\_views (V2)
For analytics.
## <a name="recommended-simplification-for-mvp"></a>20.3 Recommended Simplification for MVP
Use these as the **minimum solid set**: - users - resume\_uploads - profiles - profile\_links - profile\_skills - profile\_experiences - profile\_education - profile\_projects - profile\_sections - templates

-----
# <a name="api-specification-mvp-oriented"></a>21. API Specification (MVP-Oriented)
This is a practical API list for your Java Spring Boot backend.
## <a name="auth-apis"></a>21.1 Auth APIs
### <a name="post-apiauthsignup"></a>POST /api/auth/signup
**Request**

{\
`  `"email": "user@example.com",\
`  `"password": "StrongPassword123"\
}

**Response**

{\
`  `"userId": "uuid",\
`  `"token": "jwt-token",\
`  `"email": "user@example.com"\
}
### <a name="post-apiauthlogin"></a>POST /api/auth/login
### <a name="xb7d5f53134fc803cf12fbf2261c9d03db1f9e41"></a>POST /api/auth/logout (if session/cookie-based)
### <a name="get-apiauthme"></a>GET /api/auth/me
## <a name="resume-upload-apis"></a>21.2 Resume Upload APIs
### <a name="post-apiresumesupload"></a>POST /api/resumes/upload
Multipart upload.

**Response**

{\
`  `"resumeUploadId": "uuid",\
`  `"status": "UPLOADED"\
}
### <a name="post-apiresumesresumeuploadidparse"></a>POST /api/resumes/{resumeUploadId}/parse
Triggers parse (or parse can happen automatically after upload).

**Response**

{\
`  `"profileId": "uuid",\
`  `"status": "DRAFT\_CREATED"\
}
## <a name="draft-profile-apis"></a>21.3 Draft / Profile APIs
### <a name="get-apiprofilesprofileid"></a>GET /api/profiles/{profileId}
Returns structured profile draft.
### <a name="put-apiprofilesprofileid"></a>PUT /api/profiles/{profileId}
Updates top-level profile fields.
### <a name="put-apiprofilesprofileidsections"></a>PUT /api/profiles/{profileId}/sections
Updates section visibility/order.
### <a name="put-apiprofilesprofileidtemplate"></a>PUT /api/profiles/{profileId}/template
Changes template.
## <a name="nested-resource-apis-recommended"></a>21.4 Nested Resource APIs (Recommended)
### <a name="post-apiprofilesprofileidskills"></a>POST /api/profiles/{profileId}/skills
### <a name="put-apiprofilesprofileidskillsskillid"></a>PUT /api/profiles/{profileId}/skills/{skillId}
### <a name="delete-apiprofilesprofileidskillsskillid"></a>DELETE /api/profiles/{profileId}/skills/{skillId}
### <a name="post-apiprofilesprofileidexperiences"></a>POST /api/profiles/{profileId}/experiences
### <a name="x1059367562267a153d0608f5b047344d9d3503f"></a>PUT /api/profiles/{profileId}/experiences/{experienceId}
### <a name="xe5c6b1332060b5627ceac469a4a21ff38b1f63c"></a>DELETE /api/profiles/{profileId}/experiences/{experienceId}
### <a name="post-apiprofilesprofileideducation"></a>POST /api/profiles/{profileId}/education
### <a name="xabbb05b42055e7ec9f9dedbbb293fe0fad5860a"></a>PUT /api/profiles/{profileId}/education/{educationId}
### <a name="xf0f3c1ef3c1ccf53c3d8847d3d957fbd9ecc1a6"></a>DELETE /api/profiles/{profileId}/education/{educationId}
### <a name="post-apiprofilesprofileidprojects"></a>POST /api/profiles/{profileId}/projects
### <a name="xba658ee6ffd8868d702de6a4a72538ee1d47618"></a>PUT /api/profiles/{profileId}/projects/{projectId}
### <a name="x2f74b1f6553bde252853db8fa73dfeb9957c8ca"></a>DELETE /api/profiles/{profileId}/projects/{projectId}
## <a name="template-apis"></a>21.5 Template APIs
### <a name="get-apitemplates"></a>GET /api/templates
Returns active templates.

**Response**

[\
`  `{\
`    `"id": "uuid",\
`    `"templateKey": "recruiter-classic",\
`    `"name": "Recruiter Classic",\
`    `"description": "Clean and professional",\
`    `"previewImageUrl": "https://..."\
`  `}\
]
## <a name="slug-apis"></a>21.6 Slug APIs
### <a name="get-apislugscheckvaluekaustubh"></a>GET /api/slugs/check?value=kaustubh
**Response**

{\
`  `"value": "kaustubh",\
`  `"available": **false**,\
`  `"suggestions": ["kaustubh-dev", "kaustubh-singh"]\
}
## <a name="publish-apis"></a>21.7 Publish APIs
### <a name="post-apiprofilesprofileidpublish"></a>POST /api/profiles/{profileId}/publish
**Request**

{\
`  `"slug": "kaustubh",\
`  `"templateId": "uuid"\
}

**Response**

{\
`  `"profileId": "uuid",\
`  `"status": "PUBLISHED",\
`  `"publicUrl": "https://yourapp.com/u/kaustubh"\
}
### <a name="post-apiprofilesprofileidrepublish"></a>POST /api/profiles/{profileId}/republish
## <a name="dashboard-apis"></a>21.8 Dashboard APIs
### <a name="get-apimeprofiles"></a>GET /api/me/profiles
### <a name="get-apimeprofilesprofileid"></a>GET /api/me/profiles/{profileId}
### <a name="put-apimeprofilesprofileid"></a>PUT /api/me/profiles/{profileId}
## <a name="public-apis"></a>21.9 Public APIs
### <a name="get-apipublicslug"></a>GET /api/public/{slug}
Returns public profile data for rendering.

**Alternative architecture:** Angular route may call this behind the scenes.
## <a name="api-error-standards"></a>21.10 API Error Standards
Use consistent error response:

{\
`  `"timestamp": "2026-03-21T10:00:00Z",\
`  `"status": 400,\
`  `"error": "BAD\_REQUEST",\
`  `"message": "Slug is already taken",\
`  `"path": "/api/profiles/123/publish"\
}

-----
# <a name="deployment-architecture-render"></a>22. Deployment Architecture (Render)
## <a name="recommended-mvp-deployment-strategy"></a>22.1 Recommended MVP Deployment Strategy
### <a name="recommended-setup"></a>Recommended Setup
- **Frontend:** Angular deployed as Render Static Site
- **Backend:** Spring Boot deployed as Render Web Service
- **Database:** Render PostgreSQL Managed Database

This is the cleanest and easiest separation.
## <a name="xc0c1c6caaddca0d725b090bf8b8c51faa335f96"></a>22.2 Why Not Serve Angular from Spring Boot Initially?
Possible, but less clean for: - frontend deploy speed - caching - build separation - easier debugging
## <a name="environment-variables-backend"></a>22.3 Environment Variables (Backend)
- SPRING\_DATASOURCE\_URL
- SPRING\_DATASOURCE\_USERNAME
- SPRING\_DATASOURCE\_PASSWORD
- JWT\_SECRET
- CORS\_ALLOWED\_ORIGINS
- MAX\_UPLOAD\_SIZE\_MB
- APP\_PUBLIC\_BASE\_URL
- TEMP\_FILE\_STORAGE\_PATH (if needed)
## <a name="environment-variables-frontend"></a>22.4 Environment Variables (Frontend)
- API\_BASE\_URL
- PUBLIC\_BASE\_URL
## <a name="render-considerations"></a>22.5 Render Considerations
- Free tiers may sleep → cold starts
- Prefer paid or plan for cold start UX if demoing seriously
- Health checks matter
- Ensure backend startup time is optimized
## <a name="routing-strategy-on-render"></a>22.6 Routing Strategy on Render
### <a name="public-pages"></a>Public Pages
- Angular route: /u/:slug
- Angular app loads, then calls backend GET /api/public/{slug}

This is simple and works well.
### <a name="optional-alternative"></a>Optional Alternative
Use server-side rendering later if SEO becomes important.
## <a name="file-storage-strategy"></a>22.7 File Storage Strategy
### <a name="mvp-simplest"></a>MVP Simplest
- store uploads temporarily on disk or persistent volume (if available)
- or immediately parse and discard after structured data extraction
### <a name="better-long-term"></a>Better Long-Term
- move to object storage (S3-compatible) later

**Strong product suggestion:** If the original resume file is not essential after parsing, consider deleting it after a safe retention window to reduce storage/privacy complexity.

-----
# <a name="project-management-plan"></a>23. Project Management Plan
## <a name="project-charter"></a>23.1 Project Charter
**Project Name:** Resume2Site

**Objective:** Build an MVP of a resume-first portfolio generator for students and developers using Java, Angular, PostgreSQL, and Render.

**Success Definition:** A user can upload a resume, edit parsed data, select a template, publish a shareable portfolio URL, and later edit and republish.
## <a name="scope-guardrails"></a>23.2 Scope Guardrails
- Focus on MVP only
- No custom domain in MVP
- No subdomains in MVP
- No drag-and-drop builder
- No advanced AI features in MVP
- 3 templates only
## <a name="stakeholders"></a>23.3 Stakeholders
- Primary stakeholder: You (product + engineering)
- Future stakeholders: users, recruiters, potential collaborators
-----
# <a name="roadmap-milestones-sprint-plan"></a>24. Roadmap / Milestones / Sprint Plan
## <a name="suggested-8-week-solo-mvp-plan"></a>24.1 Suggested 8-Week Solo MVP Plan
## <a name="week-1-product-architecture-prep"></a>Week 1 — Product & Architecture Prep
- Finalize PRD
- Finalize DB schema
- Finalize API list
- Decide draft vs profile table strategy
- Define 3 templates
## <a name="week-2-project-setup"></a>Week 2 — Project Setup
- Create repos / branches
- Setup Spring Boot project
- Setup Angular project
- Setup Postgres locally
- Setup Render environments (or plan)
## <a name="week-3-resume-upload-parsing-foundation"></a>Week 3 — Resume Upload + Parsing Foundation
- File upload endpoint
- PDF/DOCX extraction
- Text normalization
- Basic parser for name/email/phone/links
- Create draft profile record
## <a name="week-4-structured-draft-editing"></a>Week 4 — Structured Draft Editing
- Draft editor UI
- Skills / experience / education forms
- Section toggles
- Save/update draft APIs
## <a name="week-5-template-system-preview"></a>Week 5 — Template System + Preview
- Build template metadata system
- Implement 3 templates
- Live preview screen
- Template switching
## <a name="week-6-auth-publish-flow"></a>Week 6 — Auth + Publish Flow
- Signup/login
- Attach draft to user
- Slug availability check
- Publish endpoint
- Public route rendering
## <a name="week-7-dashboard-republish"></a>Week 7 — Dashboard + Republish
- Dashboard page
- Edit later flow
- Change template later
- Republish flow
- Handle edge cases
## <a name="week-8-testing-deployment-polish"></a>Week 8 — Testing + Deployment + Polish
- Manual test pass
- Bug fixes
- Render deployment
- Final UI polish
- Add basic docs / screenshots
## <a name="milestones"></a>24.2 Milestones
- M1: Documentation complete
- M2: Resume parsing works
- M3: Draft editor works
- M4: Preview works
- M5: Publish works
- M6: Public profile route works
- M7: Dashboard works
- M8: MVP deployed on Render
-----
# <a name="work-breakdown-structure-wbs"></a>25. Work Breakdown Structure (WBS)
## <a name="major-work-packages"></a>25.1 Major Work Packages
1. Product Planning
1. Backend Foundation
1. Frontend Foundation
1. Resume Upload & Parsing
1. Draft Editor
1. Template System
1. Preview System
1. Auth & Publish
1. Public Page Rendering
1. Dashboard & Republish
1. Testing & QA
1. Deployment & Ops
1. Documentation & Portfolio Packaging
## <a name="sample-task-breakdown"></a>25.2 Sample Task Breakdown
### <a name="resume-upload-parsing"></a>4. Resume Upload & Parsing
- Define file validation rules
- Implement upload endpoint
- Integrate Tika/PDFBox/POI
- Normalize text
- Implement section detection
- Implement basic field extractors
- Save draft profile
### <a name="template-system"></a>6. Template System
- Create template metadata table
- Define shared profile rendering contract
- Build recruiter template
- Build developer template
- Build modern compact template
-----
# <a name="risk-register-decision-log"></a>26. Risk Register & Decision Log
## <a name="risk-register"></a>26.1 Risk Register

|Risk|Impact|Probability|Mitigation|
| :- | -: | -: | :- |
|Parsing accuracy lower than expected|High|High|Make all fields editable; use partial parse strategy|
|Scope creep from too many features|High|High|Lock MVP scope; use feature matrix|
|Template complexity grows too fast|Medium|Medium|Only 3 templates; shared schema|
|Render cold starts hurt demo UX|Medium|Medium|Optimize startup; use better plan if needed|
|Slug conflicts and routing bugs|Medium|Medium|Central slug rules + validation + reserved words|
|Fancy resumes parse poorly|Medium|High|Document supported quality; allow manual correction|
|Public page security issues|High|Medium|Escape/sanitize output; auth ownership checks|
## <a name="key-product-decisions-decision-log"></a>26.2 Key Product Decisions (Decision Log)
1. **Login is not required before preview**
   - Reason: lowers friction and improves perceived value
1. **MVP uses path-based slugs, not subdomains**
   - Reason: simpler infra and easier Render deployment
1. **MVP uses dynamic route rendering, not actual static file generation**
   - Reason: simpler implementation and easier republish lifecycle
1. **All parsed data must be editable**
   - Reason: parsing will not always be perfect
1. **Target audience is developers/students first**
   - Reason: clearer product fit and simpler templates
1. **Only 3 templates in MVP**
   - Reason: quality over quantity
-----
# <a name="qa-test-strategy"></a>27. QA / Test Strategy
## <a name="test-types"></a>27.1 Test Types
- Unit tests (backend parsing helpers, validators, slug rules)
- Integration tests (API + DB)
- Frontend component tests (if desired)
- Manual end-to-end flow testing
- Cross-device responsive checks
## <a name="critical-test-areas"></a>27.2 Critical Test Areas
1. Resume upload validation
1. Parsing flow
1. Draft save/update
1. Template switching
1. Auth flow at publish time
1. Slug validation and collisions
1. Public page rendering
1. Edit + republish
1. Unauthorized access protection
## <a name="sample-high-value-test-cases"></a>27.3 Sample High-Value Test Cases
### <a name="tc-01-upload-valid-pdf"></a>TC-01 Upload valid PDF
- Input: valid PDF under size limit
- Expected: upload accepted, parse creates draft
### <a name="tc-02-upload-invalid-file-type"></a>TC-02 Upload invalid file type
- Input: .png
- Expected: validation error
### <a name="x7f1f8a863e1474e1175bd8966333424a5aee0ae"></a>TC-03 Parse resume with missing projects section
- Expected: draft created successfully; projects empty
### <a name="tc-04-publish-with-taken-slug"></a>TC-04 Publish with taken slug
- Expected: reject and suggest alternatives
### <a name="tc-05-public-profile-loads-after-publish"></a>TC-05 Public profile loads after publish
- Expected: /u/{slug} returns valid profile rendering
### <a name="tc-06-edit-and-republish"></a>TC-06 Edit and republish
- Expected: changes visible on public page
### <a name="tc-07-unauthorized-profile-edit"></a>TC-07 Unauthorized profile edit
- Expected: 401/403
## <a name="manual-uat-checklist"></a>27.4 Manual UAT Checklist
- Can a first-time user preview without login?
- Is the draft understandable and editable?
- Are the templates professional enough to share?
- Is the publish flow intuitive?
- Does the public page look good on mobile?
-----
# <a name="user-documentation"></a>28. User Documentation
## <a name="end-user-guide"></a>28.1 End User Guide
### <a name="what-resume2site-does"></a>What Resume2Site Does
Resume2Site helps you convert your resume into a personal portfolio website that you can share with recruiters, employers, or clients.
### <a name="how-to-use-it"></a>How to Use It
1. Upload your resume (PDF/DOCX)
1. Review and edit extracted information
1. Choose a template
1. Preview your portfolio
1. Sign up/log in to publish
1. Pick your custom URL
1. Share your public page
## <a name="faq"></a>28.2 FAQ
### <a name="q-what-file-types-are-supported"></a>Q: What file types are supported?
A: PDF and DOCX in MVP.
### <a name="q-what-if-the-parser-misses-something"></a>Q: What if the parser misses something?
A: You can edit or add any section manually before publishing.
### <a name="q-can-i-change-the-template-later"></a>Q: Can I change the template later?
A: Yes, your data is stored separately from the template.
### <a name="q-can-i-change-my-url-later"></a>Q: Can I change my URL later?
A: Yes (subject to availability), but old links may stop working unless redirects are added later.
### <a name="q-is-my-portfolio-public"></a>Q: Is my portfolio public?
A: Yes, once you publish it.
### <a name="q-will-my-original-resume-be-stored"></a>Q: Will my original resume be stored?
A: Depends on platform policy. Recommended MVP policy: store only as needed for parsing, then remove after a retention window.
## <a name="troubleshooting"></a>28.3 Troubleshooting
### <a name="resume-upload-failed"></a>Resume upload failed
- Check file type
- Check file size
- Try a simpler PDF/DOCX
### <a name="some-sections-are-missing"></a>Some sections are missing
- Add them manually in the editor
- Resumes with unusual layouts may parse partially
### <a name="slug-unavailable"></a>Slug unavailable
- Try one of the suggested alternatives
### <a name="published-page-not-updating"></a>Published page not updating
- Ensure you saved and republished
- Refresh cache / hard reload if needed
-----
# <a name="operational-readiness-monitoring"></a>29. Operational Readiness & Monitoring
## <a name="logging"></a>29.1 Logging
Log these clearly: - upload failures - parse failures - parse partial success events - auth failures - publish failures - slug conflict attempts - public page not found requests
## <a name="monitoring-mvp-practical"></a>29.2 Monitoring (MVP Practical)
- basic uptime monitoring for backend
- Render health checks
- error tracking via logs initially
- optional later: Sentry-like frontend error monitoring
## <a name="backups"></a>29.3 Backups
- Ensure Postgres backups are configured
- Templates should be seedable
- Slug uniqueness must be DB-enforced
-----
# <a name="success-metrics-kpis"></a>30. Success Metrics / KPIs
## <a name="mvp-product-kpis"></a>30.1 MVP Product KPIs
- Upload → preview completion rate
- Publish conversion rate
- Average time to first publish
- Average number of edits before publish
- Template usage distribution
- Slug availability success rate
## <a name="quality-kpis"></a>30.2 Quality KPIs
- Parsing success rate (draft created)
- Parsing field completeness (e.g., email/phone/name extracted)
- Public page load success rate
- Republish success rate
## <a name="suggested-target-benchmarks-internal"></a>30.3 Suggested Target Benchmarks (Internal)
- 90%+ of uploads create a usable draft
- 80%+ of resumes extract at least name/email/phone/skills or experience
- Time to preview under 30 seconds
- Time to publish under 5 minutes
-----
# <a name="future-enhancements"></a>31. Future Enhancements
## <a name="v2-enhancements"></a>31.1 V2 Enhancements
- GitHub enrichment
- Better summary/headline suggestions
- Page analytics (views, clicks)
- HTML export
- Resume re-upload diff/merge
- SEO meta tags per profile
## <a name="v3-enhancements"></a>31.2 V3 Enhancements
- Multiple portfolio variants per user
- Custom domains
- Subdomain support
- AI-assisted content rewrite
- Template marketplace
- Portfolio version history
## <a name="strong-differentiator-to-consider-later"></a>31.3 Strong Differentiator to Consider Later
**Job-targeted profile variants** - Backend Engineer version - Full Stack version - Java Developer version

This is a strong feature that goes beyond a generic portfolio builder.

-----
# <a name="x8bd9b34b688c7fe5faba5b58e063db3fb380839"></a>32. Recommended MVP Build Rules (Very Important)
If you follow only these rules, your project will stay strong:

1. **No login before preview**
1. **Everything parsed must be editable**
1. **Use path-based slugs first**
1. **Use dynamic rendering, not actual static generation, in MVP**
1. **Target developers/students first**
1. **Only 3 strong templates**
1. **Use a shared profile schema across all templates**
1. **Keep parsing “useful”, not “perfect”**
1. **Store structured data, not just raw text**
1. **Avoid scope creep until MVP is deployed**
-----
# <a name="x1113147e316988244934f2c9f44930d83baa8bc"></a>33. Suggested Repository Documentation Structure
/docs\
`  `/01-prep\
`    `vision.md\
`    `problem-statement.md\
`    `opportunity-analysis.md\
`    `personas.md\
`    `scope-definition.md\
`    `success-criteria.md\
\
`  `/02-product\
`    `prd.md\
`    `functional-requirements.md\
`    `user-stories.md\
`    `use-cases.md\
`    `feature-matrix.md\
`    `information-architecture.md\
`    `ux-flow.md\
\
`  `/03-project-management\
`    `project-charter.md\
`    `roadmap.md\
`    `wbs.md\
`    `sprint-plan.md\
`    `risk-register.md\
`    `decision-log.md\
\
`  `/04-technical\
`    `system-architecture.md\
`    `technical-design.md\
`    `parser-design.md\
`    `template-system-design.md\
`    `routing-slug-design.md\
`    `database-design.md\
`    `api-spec.md\
`    `security-auth.md\
`    `deployment-architecture.md\
\
`  `/05-user-docs\
`    `user-guide.md\
`    `faq.md\
`    `troubleshooting.md\
\
`  `/06-testing\
`    `test-plan.md\
`    `test-cases.md\
`    `uat-checklist.md

-----
# <a name="xfe88031c16de87a263276af2566105facfa6efe"></a>34. Recommended Immediate Next Steps (Practical)
## <a name="best-next-order-of-execution"></a>Best next order of execution
1. Finalize this master doc
1. Convert it into smaller docs in /docs
1. Create **DB schema first**
1. Create **API contract second**
1. Build **upload + parse + draft editor** before templates
1. Build **template system + preview** next
1. Build **auth + publish + slug** after that
1. Deploy only after end-to-end flow works locally
## <a name="best-first-8-docs-to-split-out"></a>Best first 8 docs to split out
1. prd.md
1. feature-matrix.md
1. information-architecture.md
1. database-design.md
1. api-spec.md
1. parser-design.md
1. template-system-design.md
1. roadmap.md
-----
# <a name="final-product-assessment"></a>35. Final Product Assessment
## <a name="why-this-is-a-strong-project"></a>35.1 Why This Is a Strong Project
This is an excellent full-stack product idea because it combines: - real user value - meaningful backend logic - visible frontend impact - structured data modeling - public/private route management - deployment realism - future SaaS potential
## <a name="portfolio-interview-strength"></a>35.2 Portfolio / Interview Strength
This project demonstrates: - product thinking - scoped MVP discipline - system design awareness - backend API design - data modeling - UX flow thinking - deployment understanding - ability to build something actually useful
## <a name="final-recommendation"></a>35.3 Final Recommendation
Build the MVP as: - **resume-first** - **preview-first** - **template-driven** - **path-based** - **editable** - **dynamic render for MVP**

That is the smartest version of this product for your current stack and timeline.

-----
# <a name="x1341461c22ff217f7b740eb15fb369dda3f1728"></a>36. Appendix — Suggested MVP Acceptance Checklist
## <a name="core-product-acceptance"></a>Core Product Acceptance
- User can upload PDF/DOCX
- Parser creates a draft profile from supported files
- Draft data is editable
- User can manually add missing skills/experience/projects
- At least 3 templates are available
- Template switching works without data loss
- Live preview works
- User can sign up/login at publish time
- Slug validation and uniqueness check works
- Public page is accessible at /u/{slug}
- User can log in later and edit profile
- User can change template and republish
- Public page is mobile responsive
- Unauthorized users cannot edit another user’s profile
- App is deployed successfully on Render
-----
# <a name="final-note"></a>37. Final Note
This master documentation is intentionally optimized for: - **Java + Spring Boot** - **Angular** - **PostgreSQL** - **Render deployment** - **solo developer execution** - **strong interview/portfolio presentation**

The smartest next move is to split this into implementation-ready docs and then start with: 1. **DB schema** 2. **API contracts** 3. **upload + parse + draft editor**

-----
**End of Master Project Documentation**
