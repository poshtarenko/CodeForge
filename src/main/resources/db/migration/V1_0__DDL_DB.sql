CREATE TABLE Users
(
    id       BIGSERIAL PRIMARY KEY,
    email    VARCHAR(128) NOT NULL,
    username VARCHAR(128) NOT NULL,
    password VARCHAR(128) NOT NULL
);

CREATE TABLE RefreshToken
(
    id         BIGSERIAL PRIMARY KEY,
    token      VARCHAR(128)                    NOT NULL,
    expiration TIMESTAMP                       NOT NULL,
    user_id    BIGSERIAL REFERENCES Users (id) NOT NULL
);

CREATE TABLE Respondents
(
    id BIGSERIAL REFERENCES Users (id) PRIMARY KEY
);

CREATE TABLE Authors
(
    id BIGSERIAL REFERENCES Users (id) PRIMARY KEY
);

CREATE table Roles
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(32) NOT NULL
);

create table user_role
(
    user_id BIGSERIAL REFERENCES Users (id) NOT NULL,
    role_id BIGSERIAL REFERENCES Roles (id) NOT NULL
);

create table Tests
(
    id           BIGSERIAL PRIMARY KEY,
    name         VARCHAR(256)                      NOT NULL,
    invite_code  VARCHAR(64)                       NOT NULL UNIQUE,
    max_duration INT                               NOT NULL,
    author_id    BIGSERIAL REFERENCES Authors (id) NOT NULL
);

create table Languages
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(256) NOT NULL
);

create table Categories
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(256) NOT NULL
);

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

create table Tasks
(
    id         BIGSERIAL PRIMARY KEY,
    note       TEXT                                                 NOT NULL,
    max_score  INT                                                  NOT NULL,
    problem_id BIGSERIAL REFERENCES Problems (id) ON DELETE CASCADE NOT NULL,
    test_id    BIGSERIAL REFERENCES Tests (id) ON DELETE CASCADE    NOT NULL
);

create table Answers
(
    id            BIGSERIAL PRIMARY KEY,
    score         INT,
    is_finished   BOOLEAN                                                 NOT NULL,
    respondent_id BIGSERIAL REFERENCES Respondents (id) ON DELETE CASCADE NOT NULL,
    test_id       BIGSERIAL REFERENCES Tests (id) ON DELETE CASCADE       NOT NULL,
    created_at    TIMESTAMP                                               NOT NULL
);

create table Solutions
(
    id              BIGSERIAL PRIMARY KEY,
    code            TEXT                                                NOT NULL,
    is_completed    BOOLEAN                                             NOT NULL,
    evaluation_time BIGINT                                              NOT NULL,
    answer_id       BIGSERIAL REFERENCES Answers (id) ON DELETE CASCADE NOT NULL,
    task_id         BIGSERIAL REFERENCES Tasks (id) ON DELETE CASCADE   NOT NULL
);