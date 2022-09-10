
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
