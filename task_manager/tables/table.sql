CREATE TABLE users
(
    userId       SERIAL PRIMARY KEY,
    email         VARCHAR(100) UNIQUE NOT NULL,
    hashPassword VARCHAR(255)        NOT NULL,
    role          VARCHAR(50)         NOT NULL
);
CREATE TABLE sessions
(
    session_id SERIAL PRIMARY KEY,
    userId int NOT NULL,
    access_token TEXT,
    refresh_token TEXT,
    expired_refresh TIMESTAMP,
    FOREIGN KEY (userId) REFERENCES users (userId)
);