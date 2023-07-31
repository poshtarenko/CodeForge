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
    token      VARCHAR(128)                    NOT NULL,
    expiration TIMESTAMP                       NOT NULL,
    user_id    BIGSERIAL REFERENCES Users (id) NOT NULL
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
    user_id BIGSERIAL REFERENCES Users (id) NOT NULL,
    role_id BIGSERIAL REFERENCES Roles (id) NOT NULL
);


--changeset poshtarenko:7
create table Tests
(
    id           BIGSERIAL PRIMARY KEY,
    name         VARCHAR(256)                      NOT NULL,
    invite_code  VARCHAR(64)                       NOT NULL UNIQUE,
    max_duration INT                               NOT NULL,
    author_id    BIGSERIAL REFERENCES Authors (id) NOT NULL
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
    name          VARCHAR(256)                         NOT NULL,
    description   TEXT                                 NOT NULL,
    language_id   BIGSERIAL REFERENCES Languages (id)  NOT NULL,
    category_id   BIGSERIAL REFERENCES Categories (id) NOT NULL,
    testing_code  TEXT                                 NOT NULL,
    template_code TEXT                                 NOT NULL
);

--changeset poshtarenko:11
create table Tasks
(
    id         BIGSERIAL PRIMARY KEY,
    note       TEXT                                                 NOT NULL,
    max_score  INT                                                  NOT NULL,
    problem_id BIGSERIAL REFERENCES Problems (id) ON DELETE CASCADE NOT NULL,
    test_id    BIGSERIAL REFERENCES Tests (id) ON DELETE CASCADE    NOT NULL
);

--changeset poshtarenko:12
create table Answers
(
    id            BIGSERIAL PRIMARY KEY,
    score         INT,
    is_finished   BOOLEAN                                                 NOT NULL,
    respondent_id BIGSERIAL REFERENCES Respondents (id) ON DELETE CASCADE NOT NULL,
    test_id       BIGSERIAL REFERENCES Tests (id) ON DELETE CASCADE       NOT NULL,
    created_at    TIMESTAMP                                               NOT NULL
);

--changeset poshtarenko:13
create table Solutions
(
    id              BIGSERIAL PRIMARY KEY,
    code            TEXT                                                NOT NULL,
    is_completed    BOOLEAN                                             NOT NULL,
    evaluation_time BIGINT                                              NOT NULL,
    answer_id       BIGSERIAL REFERENCES Answers (id) ON DELETE CASCADE NOT NULL,
    task_id         BIGSERIAL REFERENCES Tasks (id) ON DELETE CASCADE   NOT NULL
);