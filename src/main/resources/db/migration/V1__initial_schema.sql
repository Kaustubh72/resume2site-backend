CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(255),
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE templates (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(120) NOT NULL,
    description VARCHAR(500),
    preview_image_url VARCHAR(500),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    sort_order INTEGER NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE resume_uploads (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id),
    original_file_name VARCHAR(255) NOT NULL,
    content_type VARCHAR(150) NOT NULL,
    storage_path VARCHAR(500) NOT NULL,
    file_size_bytes BIGINT NOT NULL,
    parse_status VARCHAR(30) NOT NULL,
    extracted_text TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE profiles (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id),
    resume_upload_id BIGINT REFERENCES resume_uploads(id),
    template_id BIGINT REFERENCES templates(id),
    draft_token VARCHAR(100) UNIQUE,
    full_name VARCHAR(255),
    headline VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(50),
    location VARCHAR(255),
    professional_summary TEXT,
    slug VARCHAR(100),
    publication_status VARCHAR(30) NOT NULL,
    published_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX uk_profiles_slug_lower ON profiles (LOWER(slug)) WHERE slug IS NOT NULL;
CREATE INDEX idx_profiles_user_id ON profiles (user_id);
CREATE INDEX idx_profiles_resume_upload_id ON profiles (resume_upload_id);
CREATE INDEX idx_resume_uploads_user_id ON resume_uploads (user_id);

CREATE TABLE profile_links (
    id BIGSERIAL PRIMARY KEY,
    profile_id BIGINT NOT NULL REFERENCES profiles(id) ON DELETE CASCADE,
    label VARCHAR(100) NOT NULL,
    url VARCHAR(500) NOT NULL,
    sort_order INTEGER NOT NULL
);

CREATE TABLE profile_skills (
    id BIGSERIAL PRIMARY KEY,
    profile_id BIGINT NOT NULL REFERENCES profiles(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(100),
    sort_order INTEGER NOT NULL
);

CREATE TABLE profile_experiences (
    id BIGSERIAL PRIMARY KEY,
    profile_id BIGINT NOT NULL REFERENCES profiles(id) ON DELETE CASCADE,
    company VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    location VARCHAR(255),
    start_date DATE,
    end_date DATE,
    is_current BOOLEAN NOT NULL DEFAULT FALSE,
    description TEXT,
    sort_order INTEGER NOT NULL
);

CREATE TABLE profile_education (
    id BIGSERIAL PRIMARY KEY,
    profile_id BIGINT NOT NULL REFERENCES profiles(id) ON DELETE CASCADE,
    institution VARCHAR(255) NOT NULL,
    degree VARCHAR(255),
    field_of_study VARCHAR(255),
    start_date DATE,
    end_date DATE,
    grade VARCHAR(100),
    description TEXT,
    sort_order INTEGER NOT NULL
);

CREATE TABLE profile_projects (
    id BIGSERIAL PRIMARY KEY,
    profile_id BIGINT NOT NULL REFERENCES profiles(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    project_url VARCHAR(500),
    repository_url VARCHAR(500),
    tech_stack TEXT,
    sort_order INTEGER NOT NULL
);

CREATE TABLE profile_sections (
    id BIGSERIAL PRIMARY KEY,
    profile_id BIGINT NOT NULL REFERENCES profiles(id) ON DELETE CASCADE,
    section_key VARCHAR(100) NOT NULL,
    display_name VARCHAR(100) NOT NULL,
    is_visible BOOLEAN NOT NULL DEFAULT TRUE,
    sort_order INTEGER NOT NULL,
    CONSTRAINT uk_profile_section_key UNIQUE (profile_id, section_key)
);

INSERT INTO templates (code, name, description, preview_image_url, active, sort_order)
VALUES
    ('minimal-dev', 'Minimal Developer', 'Clean single-column template focused on early-career developers.', NULL, TRUE, 1),
    ('modern-stack', 'Modern Stack', 'Modern card-based layout for projects, skills, and experience.', NULL, TRUE, 2),
    ('student-launch', 'Student Launch', 'Simple portfolio layout optimized for students and freshers.', NULL, TRUE, 3);
