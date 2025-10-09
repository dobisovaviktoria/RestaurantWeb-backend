CREATE USER "user" WITH PASSWORD 'password';
CREATE DATABASE kdg;
GRANT ALL PRIVILEGES ON DATABASE kdg TO "user";

CREATE SCHEMA kdg_restaurant;
CREATE SCHEMA kdg_orders;

CREATE TABLE IF NOT EXISTS kdg_restaurant.owners (
                        id VARCHAR(255) PRIMARY KEY,
                        email VARCHAR(255) NOT NULL UNIQUE,
                        name VARCHAR(255)
);