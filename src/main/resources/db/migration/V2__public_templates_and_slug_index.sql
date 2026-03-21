ALTER TABLE templates
    ADD COLUMN category VARCHAR(100),
    ADD COLUMN accent_color VARCHAR(30),
    ADD COLUMN features TEXT;

UPDATE templates
SET preview_image_url = CASE code
        WHEN 'minimal-dev' THEN 'https://cdn.resume2site.dev/templates/minimal-dev.png'
        WHEN 'modern-stack' THEN 'https://cdn.resume2site.dev/templates/modern-stack.png'
        WHEN 'student-launch' THEN 'https://cdn.resume2site.dev/templates/student-launch.png'
    END,
    category = CASE code
        WHEN 'minimal-dev' THEN 'developer'
        WHEN 'modern-stack' THEN 'developer'
        WHEN 'student-launch' THEN 'student'
    END,
    accent_color = CASE code
        WHEN 'minimal-dev' THEN '#111827'
        WHEN 'modern-stack' THEN '#2563EB'
        WHEN 'student-launch' THEN '#7C3AED'
    END,
    features = CASE code
        WHEN 'minimal-dev' THEN 'Single-column layout|Strong summary section|Readable project blocks'
        WHEN 'modern-stack' THEN 'Project-first layout|Visual skill grouping|Compact experience timeline'
        WHEN 'student-launch' THEN 'Education-first layout|Beginner-friendly project emphasis|Simple contact header'
    END;

CREATE INDEX idx_profiles_published_slug_lookup
    ON profiles (publication_status, slug);
