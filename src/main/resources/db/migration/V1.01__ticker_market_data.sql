

ALTER TABLE ticker_detail
DROP market_cap,
DROP share_class_shares_outstanding,
DROP  weighted_shares_outstanding;

CREATE TABLE IF NOT EXISTS ticker_market_data (
  id INT AUTO_INCREMENT PRIMARY KEY,
  updated_timestamp TIMESTAMP,
  market_cap BIGINT,
  share_class_shares_outstanding BIGINT,
  weighted_shares_outstanding BIGINT,
  ticker_detail_id INT,
  FOREIGN KEY (ticker_detail_id) REFERENCES ticker_detail(id) ON DELETE CASCADE
);
