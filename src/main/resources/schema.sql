
CREATE TABLE IF NOT EXISTS ticker_detail
(
    id INT auto_increment PRIMARY KEY NOT NULL,
    symbol VARCHAR(10) NOT NULL,
    name VARCHAR(255) NOT NULL,
    market VARCHAR(10),
    primary_exchange VARCHAR(10),
    currency_name VARCHAR(10),
    type VARCHAR(10),
    description VARCHAR(500),
    market_cap BIGINT,
    home_page_url VARCHAR(255),
    total_employees INT,
    list_date VARCHAR(50),
    share_class_shares_outstanding BIGINT,
    weighted_shares_outstanding BIGINT

    );