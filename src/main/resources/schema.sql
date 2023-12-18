
CREATE TABLE IF NOT EXISTS ticker
(
    id INT auto_increment PRIMARY KEY NOT NULL,
    symbol VARCHAR(10) NOT NULL,
    name VARCHAR(255) NOT NULL,
    market VARCHAR(10) NOT NULL,
    currency_name VARCHAR(10) NOT NULL,
    primary_exchange VARCHAR(10) NOT NULL
);