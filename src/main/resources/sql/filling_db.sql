-- Инициализация тестовыми данными

-- Добавим пару пользователей
INSERT INTO users (username, password_hash, role)
VALUES 
('admin', 'hashed_password_admin', 'admin'),
('manager', 'hashed_password_manager', 'user');

-- Добавим несколько партнеров
INSERT INTO partners (name, contact_info, partner_type)
VALUES
('ООО Поставщик-1', '+7(900)111-11-11', 'supplier'),
('ООО Поставщик-2', 'email: supplier2@example.com', 'supplier'),
('ООО Потребитель-1', '+7(900)222-22-22', 'consumer'),
('ООО Компания-Б', 'companyb@example.com', 'both');

-- Добавим товары
INSERT INTO products (product_name, category, expiration_date, quantity)
VALUES
('Молоко', 'Продукты', '2025-05-01', 50),
('Яблоки', 'Фрукты', '2025-06-15', 30),
('Ноутбук', 'Техника', NULL, 10);

-- Операция 1: Поставка, поставщик-1, 10 Молока
INSERT INTO operations (operation_type, partner_id, operation_date, user_id)
VALUES ('in', 1, '2025-01-10', 1);
-- Детали операции 1: Молоко, 20 единиц
INSERT INTO operation_details (operation_id, product_id, quantity)
VALUES (LAST_INSERT_ID(), 1, 20);

-- Операция 2: Поставка, поставщик-2, 15 Яблок
INSERT INTO operations (operation_type, partner_id, operation_date, user_id)
VALUES ('in', 2, '2025-01-15', 1);
INSERT INTO operation_details (operation_id, product_id, quantity)
VALUES (LAST_INSERT_ID(), 2, 15);

-- Операция 3: Выдача, потребитель-1, 5 Молока
INSERT INTO operations (operation_type, partner_id, operation_date, user_id)
VALUES ('out', 3, '2025-01-20', 2);
INSERT INTO operation_details (operation_id, product_id, quantity)
VALUES (LAST_INSERT_ID(), 1, 5);

-- Операция 4: Поставка, Компания-Б, 2 Ноутбука
INSERT INTO operations (operation_type, partner_id, operation_date, user_id)
VALUES ('in', 4, '2025-01-22', 1);
INSERT INTO operation_details (operation_id, product_id, quantity)
VALUES (LAST_INSERT_ID(), 3, 2);

-- Операция 5: Выдача, Компания-Б, 5 Яблок
INSERT INTO operations (operation_type, partner_id, operation_date, user_id)
VALUES ('out', 4, '2025-01-25', 2);
INSERT INTO operation_details (operation_id, product_id, quantity)
VALUES (LAST_INSERT_ID(), 2, 5);
