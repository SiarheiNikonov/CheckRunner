
\! cls
\c postgres;
DROP DATABASE products;
CREATE DATABASE products WITH ENCODING 'UTF8';
\c products;
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

INSERT INTO discount_card_types (type_title, discount_percent)
VALUES
('WOODEN', 1),
('SILVER', 3),
('GOLD', 5),
('PLATINUM', 10);

INSERT INTO discount_cards (card_type)
VALUES
(1),
(2),
(3),
(4),
(1),
(2),
(3),
(4),
(1),
(2),
(3),
(4);


INSERT INTO company (company_name, company_address, company_tel_number)
VALUES
('SAMSUNG', 'someaddress', '23526246'),
('TOSHIBA', 'someaddress','765432'),
('HP', 'someaddress','1236'),
('LENOVO', 'someaddress','61237278'),
('APPLE', 'someaddress','2343443434');


INSERT INTO product (title, price_in_cent, description, company_id_ref, barcode, on_sale)
VALUES
('Банан', 123, 'вкусный, зрелый, применять по назначению', 4, '234234234234', false),
('Апэлсын', 323, 'круглый, катится', 1, '111134234234', false),
('Киви', 143, 'большое и лохматое', 2, '234444234234', false),
('Арбуз', 125, 'для полётов с большой высоты', 3, '134234234234', true),
('Пистолет Макарова', 13223, 'для детей от трёх лет', 5, '534234234234', false),
('Хвост бобра', 1263, 'почему бы и нет', 1, '734234234234', false),
('Цветочек аленький', 8123, 'гербарий из цветочка', 2, '244234234234', true),
('random title1', 666, 'random description1', 1, '234234234294', false),
('random title2', 777, 'random description1', 2, '111134234284', false),
('random title3', 18888, 'random description2', 3, '234444274234', false),
('random title4', 4444, 'random description3', 1, '234234234634', false),
('random title5', 325553, 'random description4', 4, '111134235234', true),
('random title6', 146663, 'random description5', 5, '234444334234', false);

SELECT * FROM product
WHERE title LIKE 'А%';

SELECT company_name FROM company
WHERE company_id IN(
SELECT company_id_ref FROM product
WHERE LENGTH(title) < 6
);

SELECT company_name AS Company,SUM(ROUND(price_in_cent/100, 2)) AS "Price for all products", COUNT(*) AS "Products count"
FROM product
JOIN company ON product.company_id_ref = company.company_id
GROUP BY company_name
HAVING COUNT(*) > 2;

SELECT title, price_in_cent, description, company_name FROM product
JOIN company ON product.company_id_ref = company.company_id;