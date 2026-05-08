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
CREATE TYPE user_gender AS ENUM ('MALE', 'FEMALE');
CREATE TYPE exercise_type AS ENUM ('STRENGTH', 'CALISTHENICS',' CARDIO');
CREATE TYPE activity_level as ENUM('SEDENTARY', 'LIGHT', 'MODERATE' , 'HIGH' , 'ATHLETE')
CREATE TYPE user_goal as ENUM('FAT_LOSS' , 'MUSCLE_GAIN' , 'RECOMPOSITION')
CREATE TYPE user_phase as ENUM ( 'CUTTING' , 'BULKING' , 'MAINTENANCE')


-- =========================
-- USERS & SECURITY
-- =========================
CREATE TABLE app_user ( --HECHO
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name_user VARCHAR(50) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    status user_status NOT NULL DEFAULT 'ACTIVE',
    profile_image_url VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE TABLE role ( --HECHO
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE user_role ( --HECHO PERO SIN ENTITY
    user_id UUID REFERENCES app_user(id) ON DELETE CASCADE,
    role_id UUID REFERENCES role(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

-- =========================
-- PHYSICAL PROFILE
-- =========================
CREATE TABLE physical_profile ( --HECHO
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID UNIQUE REFERENCES app_user(id) ON DELETE CASCADE,
    height_cm INT NOT NULL,
    initial_weight DECIMAL(5,2) NOT NULL CHECK (initial_weight > 0),
    gender user_gender NOT NULL,
    level user_level NOT NULL DEFAULT 'BEGINNER',
    activity_level activity_level NOT NULL DEFAULT 'MODERATE',
    goal user_goal NOT NULL,
    phase user_phase NOT NULL,
    age INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP

);

CREATE TABLE nutrition_plan ( --HECHO
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES app_user(id) ON DELETE CASCADE,
    target_calories INT NOT NULL,
    target_protein DECIMAL(5,2) NOT NULL, -- en gramos
    target_carbs DECIMAL(5,2) NOT NULL,   -- en gramos
    target_fats DECIMAL(5,2) NOT NULL,    -- en gramos
    is_active BOOLEAN DEFAULT TRUE,       -- Para saber cuál es el plan actual
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    end_date TIMESTAMP                    -- Fecha en la que cambió el plan
);

CREATE TABLE body_record ( --TODAVIA NO IMPLEMENTADO
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES app_user(id) ON DELETE CASCADE,
    weight_kg DECIMAL(5,2) NOT NULL CHECK (weight_kg > 0),
    body_fat DECIMAL(5,2),
    record_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =========================
-- TRAINING CATALOG
-- =========================
CREATE TABLE muscle_group ( --HECHO
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) UNIQUE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE TABLE exercise ( --HECHO
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(150) NOT NULL,
    description TEXT,
    type exercise_type NOT NULL,
    equipment VARCHAR(100),
    image_url VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE TABLE exercise_muscle ( --HECHO

    exercise_id UUID REFERENCES exercise(id) ON DELETE CASCADE,
    muscle_group_id UUID REFERENCES muscle_group(id) ON DELETE CASCADE,
    is_primary BOOLEAN DEFAULT False, --True si es principal (ej: Dorsales) , False si es secundario (ej: Biceps)
    PRIMARY KEY (exercise_id, muscle_group_id)
);

CREATE TABLE exercise_instruction( --HECHO
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    exercise_id UUID REFERENCES exercise(id) ON DELETE CASCADE,
    step_number INT NOT NULL,
    instruction_text TEXT NOT NULL,
    UNIQUE (exercise_id , step_number), --Para que no hayan pasos repetidos.
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP
);

-- =========================
-- ROUTINES
-- =========================
CREATE TABLE routine ( -- HECHO
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    creator_id UUID REFERENCES app_user(id) NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT, --Para que puedas poner el foco de la rutina.
    level user_level DEFAULT 'BEGINNER', --Para que puedas saber el nivel de la rutina
    goal user_goal, -- Para saber el objetivo de la rutina como tal.
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE TABLE routine_exercise ( -- HECHO    
    routine_id UUID REFERENCES routine(id) ON DELETE CASCADE,
    exercise_id UUID REFERENCES exercise(id) ON DELETE CASCADE,
    position INT NOT NULL, --Orden del ejercicio
    sets INT DEFAULT 3, --Series default
    repetitions INT DEFAULT 10, --Repeticiones default
    rest_secods INT DEFAULT 60,
    PRIMARY KEY (routine_id, exercise_id)
);

-- =========================
-- TRAINING SESSIONS
-- =========================
CREATE TABLE training_session ( --HECHO 
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES app_user(id) ON DELETE CASCADE,
    routine_id UUID REFERENCES routine(id), --Nulo si es un entreno improvisado.
    session_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    duration_minutes INT CHECK (duration_minutes > 0),
    deleted_at TIMESTAMP
);

CREATE TABLE exercise_record ( --HECHO
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    session_id UUID REFERENCES training_session(id) ON DELETE CASCADE,
    exercise_id UUID REFERENCES exercise(id),
    set_number INT NOT NULL, --Numero de la serie.
    weight DECIMAL(6,2), --En KG
    repetitions INT, 
    time_seconds INT, --Para los ejercicios que sean en segundos.
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
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
('Pecho'), ('Espalda'), ('Hombros'), ('Bíceps'), 
('Tríceps'), ('Cuádriceps'), ('Isquiotibiales'), 
('Glúteos'), ('Gemelos'), ('Core'), ('Antebrazo'), 
('Trapecio'), ('Lumbar');

INSERT INTO exercise (id, name, description, type, equipment) VALUES 
(gen_random_uuid(), 'Press de Banca', 'Empuje horizontal con barra para pectoral.', 'STRENGTH', 'Barra'),
(gen_random_uuid(), 'Dominadas', 'Tracción vertical con peso corporal.', 'CALISTHENICS', 'Barra de Dominadas'),
(gen_random_uuid(), 'Sentadilla con Barra', 'Ejercicio multiarticular de pierna.', 'STRENGTH', 'Rack y Barra'),
(gen_random_uuid(), 'Flexiones de Brazos', 'Empuje de pecho con peso corporal.', 'CALISTHENICS', 'Ninguno'),
(gen_random_uuid(), 'Burpees', 'Ejercicio de alta intensidad metabólica.', ' CARDIO', 'Ninguno'); -- Nota: Espacio incluido según tu ENUM

--CONEXIÓN GRUPO MUSCULAR x EJERCICIO
-- 1. Press de Banca (Principal: Pecho | Secundario: Tríceps)
INSERT INTO exercise_muscle (exercise_id, muscle_group_id, is_primary) VALUES 
((SELECT id FROM exercise WHERE name = 'Press de Banca'), (SELECT id FROM muscle_group WHERE name = 'Pecho'), TRUE),
((SELECT id FROM exercise WHERE name = 'Press de Banca'), (SELECT id FROM muscle_group WHERE name = 'Tríceps'), FALSE);

-- 2. Dominadas (Principal: Espalda | Secundario: Bíceps)
INSERT INTO exercise_muscle (exercise_id, muscle_group_id, is_primary) VALUES 
((SELECT id FROM exercise WHERE name = 'Dominadas'), (SELECT id FROM muscle_group WHERE name = 'Espalda'), TRUE),
((SELECT id FROM exercise WHERE name = 'Dominadas'), (SELECT id FROM muscle_group WHERE name = 'Bíceps'), FALSE);

-- 3. Sentadilla (Principal: Cuádriceps | Secundario: Glúteos)
INSERT INTO exercise_muscle (exercise_id, muscle_group_id, is_primary) VALUES 
((SELECT id FROM exercise WHERE name = 'Sentadilla con Barra'), (SELECT id FROM muscle_group WHERE name = 'Cuádriceps'), TRUE),
((SELECT id FROM exercise WHERE name = 'Sentadilla con Barra'), (SELECT id FROM muscle_group WHERE name = 'Glúteos'), FALSE);

-- 4. Flexiones (Principal: Pecho | Secundario: Hombros)
INSERT INTO exercise_muscle (exercise_id, muscle_group_id, is_primary) VALUES 
((SELECT id FROM exercise WHERE name = 'Flexiones de Brazos'), (SELECT id FROM muscle_group WHERE name = 'Pecho'), TRUE),
((SELECT id FROM exercise WHERE name = 'Flexiones de Brazos'), (SELECT id FROM muscle_group WHERE name = 'Hombros'), FALSE);

-- 5. Burpees (Principal: Core | Secundario: Cuádriceps)
INSERT INTO exercise_muscle (exercise_id, muscle_group_id, is_primary) VALUES 
((SELECT id FROM exercise WHERE name = 'Burpees'), (SELECT id FROM muscle_group WHERE name = 'Core'), TRUE),
((SELECT id FROM exercise WHERE name = 'Burpees'), (SELECT id FROM muscle_group WHERE name = 'Cuádriceps'), FALSE);

--PASOS EJERCICIOS
-- Instrucciones para Press de Banca
INSERT INTO exercise_instruction (exercise_id, step_number, instruction_text) VALUES 
((SELECT id FROM exercise WHERE name = 'Press de Banca'), 1, 'Túmbate en el banco y agarra la barra con una anchura superior a los hombros.'),
((SELECT id FROM exercise WHERE name = 'Press de Banca'), 2, 'Baja la barra al pecho de forma controlada.'),
((SELECT id FROM exercise WHERE name = 'Press de Banca'), 3, 'Extiende los codos para subir la barra.');

-- Instrucciones para Dominadas
INSERT INTO exercise_instruction (exercise_id, step_number, instruction_text) VALUES 
((SELECT id FROM exercise WHERE name = 'Dominadas'), 1, 'Sujétate a la barra con las palmas hacia adelante.'),
((SELECT id FROM exercise WHERE name = 'Dominadas'), 2, 'Tira de tu cuerpo hacia arriba hasta que la barbilla pase la barra.'),
((SELECT id FROM exercise WHERE name = 'Dominadas'), 3, 'Baja lentamente hasta la posición inicial.');

-- Instrucciones para Sentadilla
INSERT INTO exercise_instruction (exercise_id, step_number, instruction_text) VALUES 
((SELECT id FROM exercise WHERE name = 'Sentadilla con Barra'), 1, 'Coloca la barra tras la nuca sobre los trapecios.'),
((SELECT id FROM exercise WHERE name = 'Sentadilla con Barra'), 2, 'Baja la cadera rompiendo el paralelo de las rodillas.'),
((SELECT id FROM exercise WHERE name = 'Sentadilla con Barra'), 3, 'Sube con fuerza empujando desde el talón.');

-- Instrucciones para Flexiones
INSERT INTO exercise_instruction (exercise_id, step_number, instruction_text) VALUES 
((SELECT id FROM exercise WHERE name = 'Flexiones de Brazos'), 1, 'Coloca las manos en el suelo a la anchura de hombros.'),
((SELECT id FROM exercise WHERE name = 'Flexiones de Brazos'), 2, 'Baja el pecho casi hasta tocar el suelo.'),
((SELECT id FROM exercise WHERE name = 'Flexiones de Brazos'), 3, 'Empuja para volver a la posición de plancha.');

-- Instrucciones para Burpees
INSERT INTO exercise_instruction (exercise_id, step_number, instruction_text) VALUES 
((SELECT id FROM exercise WHERE name = 'Burpees'), 1, 'Desde posición de pie, baja a hacer una flexión.'),
((SELECT id FROM exercise WHERE name = 'Burpees'), 2, 'Recoge las piernas explosivamente hacia el pecho.'),
((SELECT id FROM exercise WHERE name = 'Burpees'), 3, 'Salta verticalmente con las manos hacia arriba.');
