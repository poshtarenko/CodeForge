--liquibase formatted sql

--changeset poshtarenko:1
CREATE TABLE Users
(
    id       BIGSERIAL PRIMARY KEY,
    email    VARCHAR(128) NOT NULL,
    username VARCHAR(128) NOT NULL,
    password VARCHAR(128) NOT NULL
);

--changeset poshtarenko:2
CREATE TABLE RefreshToken
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT REFERENCES Users (id) NOT NULL,
    token      VARCHAR(128)                 NOT NULL,
    expiration TIMESTAMP                    NOT NULL
);

--changeset poshtarenko:3
CREATE TABLE Respondents
(
    id BIGSERIAL REFERENCES Users (id) PRIMARY KEY
);

--changeset poshtarenko:4
CREATE TABLE Authors
(
    id BIGSERIAL REFERENCES Users (id) PRIMARY KEY
);

--changeset poshtarenko:5
CREATE table Roles
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(32) NOT NULL
);

--changeset poshtarenko:6
create table user_role
(
    user_id BIGINT REFERENCES Users (id) NOT NULL,
    role_id BIGINT REFERENCES Roles (id) NOT NULL
);


--changeset poshtarenko:7
create table Tests
(
    id           BIGSERIAL PRIMARY KEY,
    author_id    BIGINT REFERENCES Authors (id) NOT NULL,
    name         VARCHAR(256)                   NOT NULL,
    invite_code  VARCHAR(64)                    NOT NULL UNIQUE,
    max_duration INT                            NOT NULL
);


--changeset poshtarenko:8
create table Languages
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(256) NOT NULL
);


--changeset poshtarenko:9
create table Categories
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(256) NOT NULL
);


--changeset poshtarenko:10
create table Problems
(
    id            BIGSERIAL PRIMARY KEY,
    language_id   BIGINT REFERENCES Languages (id)  NOT NULL,
    category_id   BIGINT REFERENCES Categories (id) NOT NULL,
    name          VARCHAR(256)                      NOT NULL,
    description   TEXT                              NOT NULL,
    testing_code  TEXT                              NOT NULL,
    template_code TEXT                              NOT NULL
);

--changeset poshtarenko:11
create table Tasks
(
    id         BIGSERIAL PRIMARY KEY,
    test_id    BIGINT REFERENCES Tests (id) ON DELETE CASCADE    NOT NULL,
    problem_id BIGINT REFERENCES Problems (id) ON DELETE CASCADE NOT NULL,
    note       TEXT                                              NOT NULL,
    max_score  INT                                               NOT NULL
);

--changeset poshtarenko:12
create table Answers
(
    id            BIGSERIAL PRIMARY KEY,
    respondent_id BIGINT REFERENCES Respondents (id) ON DELETE CASCADE NOT NULL,
    test_id       BIGINT REFERENCES Tests (id) ON DELETE CASCADE       NOT NULL,
    score         INT,
    is_finished   BOOLEAN                                              NOT NULL,
    created_at    TIMESTAMP                                            NOT NULL
);

--changeset poshtarenko:13
create table Solutions
(
    id                     BIGSERIAL PRIMARY KEY,
    answer_id              BIGINT REFERENCES Answers (id) ON DELETE CASCADE NOT NULL,
    task_id                BIGINT REFERENCES Tasks (id) ON DELETE CASCADE   NOT NULL,
    code                   TEXT,
    task_completion_status TEXT

);

--changeset poshtarenko:14
create table Lessons
(
    id          BIGSERIAL PRIMARY KEY,
    author_id   BIGINT REFERENCES Authors (id) ON DELETE CASCADE NOT NULL,
    language_id BIGINT REFERENCES Languages (id) ON DELETE CASCADE,
    name        VARCHAR(128)                                     NOT NULL,
    invite_code VARCHAR(64)                                      NOT NULL UNIQUE,
    description TEXT
);

--changeset poshtarenko:15
create table Participations
(
    id        BIGSERIAL PRIMARY KEY,
    user_id   BIGINT REFERENCES Users (id) ON DELETE CASCADE   NOT NULL,
    lesson_id BIGINT REFERENCES Lessons (id) ON DELETE CASCADE NOT NULL,
    code      TEXT,
    output    TEXT,
    error     TEXT
);
