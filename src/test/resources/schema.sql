/*
CREATE TABLE person (
    id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS account (
    number VARCHAR(255) PRIMARY KEY,
    balance DECIMAL(15, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS transaction (
    id LONG PRIMARY KEY,
    account_number VARCHAR(255) REFERENCES account(number) NOT NULL,
    type VARCHAR(50) NOT NULL,
    amount DECIMAL(15, 2) NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    currency VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS account_user (
    account_number VARCHAR(255),
    user_id INT,
    PRIMARY KEY (account_number, user_id),
    FOREIGN KEY (account_number) REFERENCES account(number),
    FOREIGN KEY (user_id) REFERENCES person(id)
);*/
