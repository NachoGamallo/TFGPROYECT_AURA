-- =========================
--DATABASE STRUCTURE.
-- =========================

CREATE DATABASE "AuraDatabase"
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LOCALE_PROVIDER = 'libc'
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

-- =========================
-- EXTENSIONS
-- =========================
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- =========================
-- ENUMS
-- =========================
CREATE TYPE user_status AS ENUM ('ACTIVE', 'BLOCKED');
CREATE TYPE user_level AS ENUM ('BEGINNER', 'INTERMEDIATE', 'ADVANCED');
CREATE TYPE user_gender AS ENUM ('MALE', 'FEMALE', 'OTHER');
CREATE TYPE exercise_type AS ENUM ('STRENGTH', 'CALISTHENICS',' CARDIO');
CREATE TYPE activity_level as ENUM('NOT ACTIVE', 'LIGHT ACTIVITY', 'ACTIVE', 'SO ACTIVE')

-- =========================
-- USERS & SECURITY
-- =========================
CREATE TABLE app_user (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(50) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    status user_status NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE TABLE role (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE user_role (
    user_id UUID REFERENCES app_user(id) ON DELETE CASCADE,
    role_id INT REFERENCES role(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

-- =========================
-- PHYSICAL PROFILE
-- =========================
CREATE TABLE physical_profile (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID UNIQUE REFERENCES app_user(id) ON DELETE CASCADE,
    height_cm INT NOT NULL,
    initial_weight INT NOT NULL,
    gender user_gender NOT NULL,
    level user_level NOT NULL DEFAULT 'BEGINNER',
    main_goal VARCHAR(100),
    age INT NOT NULL

);

CREATE TABLE body_record (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES app_user(id) ON DELETE CASCADE,
    weight_kg DECIMAL(5,2) NOT NULL CHECK (weight_kg > 0),
    body_fat DECIMAL(5,2),
    record_date DATE NOT NULL
);

-- =========================
-- TRAINING CATALOG
-- =========================
CREATE TABLE muscle_group (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE exercise (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(150) NOT NULL,
    description TEXT,
    type exercise_type NOT NULL,
    muscle_group_id INT REFERENCES muscle_group(id),
    deleted_at TIMESTAMP
);

-- =========================
-- ROUTINES
-- =========================
CREATE TABLE routine (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    creator_id UUID REFERENCES app_user(id),
    name VARCHAR(150) NOT NULL,
    goal VARCHAR(100),
    level user_level,
    deleted_at TIMESTAMP
);

CREATE TABLE routine_exercise (
    routine_id UUID REFERENCES routine(id) ON DELETE CASCADE,
    exercise_id UUID REFERENCES exercise(id),
    position INT,
    sets INT,
    repetitions INT,
    deleted_at TIMESTAMP,
    PRIMARY KEY (routine_id, exercise_id)
);

-- =========================
-- TRAINING SESSIONS
-- =========================
CREATE TABLE training_session (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES app_user(id) ON DELETE CASCADE,
    routine_id UUID REFERENCES routine(id),
    session_date DATE NOT NULL,
    duration_minutes INT CHECK (duration_minutes > 0),
    deleted_at TIMESTAMP
);

CREATE TABLE exercise_record (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    session_id UUID REFERENCES training_session(id) ON DELETE CASCADE,
    exercise_id UUID REFERENCES exercise(id),
    weight DECIMAL(6,2),
    repetitions INT,
    time_seconds INT
);

-- =========================
-- NUTRITION
-- =========================
CREATE TABLE food (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(150) NOT NULL,
    calories DECIMAL(6,2) NOT NULL,
    protein DECIMAL(6,2),
    carbohydrates DECIMAL(6,2),
    fat DECIMAL(6,2),
    deleted_at TIMESTAMP
);

CREATE TABLE food_record (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES app_user(id) ON DELETE CASCADE,
    food_id UUID REFERENCES food(id),
    amount_grams DECIMAL(6,2) NOT NULL CHECK (amount_grams > 0),
    record_date DATE NOT NULL
);

-- =========================
-- GAMIFICATION
-- =========================
CREATE TABLE achievement (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(150) NOT NULL,
    description TEXT,
    criteria TEXT,
    deleted_at TIMESTAMP
);

CREATE TABLE user_achievement (
    user_id UUID REFERENCES app_user(id) ON DELETE CASCADE,
    achievement_id UUID REFERENCES achievement(id),
    achieved_at DATE NOT NULL,
    PRIMARY KEY (user_id, achievement_id)
);

-- =========================
-- CRM / COACH MODE (SGE)
-- =========================
CREATE TABLE coach_client (
    coach_id UUID REFERENCES app_user(id) ON DELETE CASCADE,
    client_id UUID REFERENCES app_user(id) ON DELETE CASCADE,
    start_date DATE NOT NULL,
    status VARCHAR(50),
    PRIMARY KEY (coach_id, client_id)
);

-- =========================
-- INITIAL DATA
-- =========================
INSERT INTO role (name) VALUES ('USER'), ('COACH'), ('ADMIN');

INSERT INTO muscle_group (name) VALUES
('Chest'),
('Back'),
('Legs'),
('Shoulders'),
('Biceps'),
('Triceps'),
('Core');
