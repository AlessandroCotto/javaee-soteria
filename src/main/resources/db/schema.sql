CREATE TABLE account(
   id SERIAL PRIMARY KEY,
   username VARCHAR(100) UNIQUE NOT NULL,
   password VARCHAR(255) NOT NULL,
   email VARCHAR(100) UNIQUE NOT NULL,
   active BOOLEAN
);

CREATE TABLE token(
   id SERIAL PRIMARY KEY,
   account_id INTEGER REFERENCES account(id),
   token_hash VARCHAR(255) NOT NULL,
   token_TYPE VARCHAR(100),
   ip_address VARCHAR(100),
   description VARCHAR(255),
   created TIMESTAMP,
   expiration TIMESTAMP
)
