
CREATE TABLE company (
company_id SERIAL PRIMARY KEY,
company_name VARCHAR(30),
company_address VARCHAR(50),
company_tel_number VARCHAR(15)
);

CREATE TABLE discount_card_types (
type_id SERIAL PRIMARY KEY,
type_title VARCHAR(10),
discount_percent SMALLINT,
CHECK (discount_percent > 0 AND discount_percent < 100)
);

CREATE TABLE discount_cards (
card_id SERIAL PRIMARY KEY,
card_type INT,
FOREIGN KEY (card_type) REFERENCES discount_card_types (type_id)
);

CREATE TABLE product (
product_id SERIAL PRIMARY KEY,
title VARCHAR(30),
price_in_cent INT,
description VARCHAR(200),
company_id_ref INT,
barcode VARCHAR(12),
on_sale BOOLEAN,
FOREIGN KEY (company_id_ref) REFERENCES company (company_id)
);