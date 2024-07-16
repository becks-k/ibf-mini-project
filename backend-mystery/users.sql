DROP DATABASE IF EXISTS mysterygame;
CREATE DATABASE mysterygame;
USE mysterygame;

CREATE TABLE users (
    id VARCHAR(8) PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(200) NOT NULL,
    email VARCHAR(200) NOT NULL
);

CREATE TABLE game_history (
	id INT AUTO_INCREMENT,
    user_id VARCHAR(8) NOT NULL,
    mystery_id VARCHAR(8) NOT NULL,
    score INT NOT NULL DEFAULT 0,

    PRIMARY KEY(id),
    CONSTRAINT fk_user_id FOREIGN KEY(user_id)
        REFERENCES users(id)
);



