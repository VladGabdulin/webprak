-- Создание базы данных
CREATE DATABASE IF NOT EXISTS warehouse_db;
USE warehouse_db;

-- 1. Таблица пользователей
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'user'
);

-- 2. Таблица партнеров (поставщики / потребители)
CREATE TABLE IF NOT EXISTS partners (
    partner_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    contact_info VARCHAR(255),
    partner_type ENUM('supplier','consumer','both') NOT NULL
);

-- 3. Таблица товаров
CREATE TABLE IF NOT EXISTS products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(100) NOT NULL,
    category VARCHAR(50),
    expiration_date DATE,
    quantity INT NOT NULL DEFAULT 0
);

-- 4. Таблица операций (поставки и выдачи)
CREATE TABLE IF NOT EXISTS operations (
    operation_id INT AUTO_INCREMENT PRIMARY KEY,
    operation_type ENUM('in','out') NOT NULL,
    partner_id INT NOT NULL,
    operation_date DATE NOT NULL,
    user_id INT,
    CONSTRAINT fk_partner 
        FOREIGN KEY (partner_id) REFERENCES partners(partner_id) 
        ON DELETE RESTRICT,
    CONSTRAINT fk_user 
        FOREIGN KEY (user_id) REFERENCES users(user_id) 
        ON DELETE SET NULL
);

-- 5. Промежуточная таблица деталей операций (какие товары участвуют в операции)
CREATE TABLE IF NOT EXISTS operation_details (
    operation_detail_id INT AUTO_INCREMENT PRIMARY KEY,
    operation_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    CONSTRAINT fk_operation 
        FOREIGN KEY (operation_id) REFERENCES operations(operation_id) 
        ON DELETE CASCADE,
    CONSTRAINT fk_product 
        FOREIGN KEY (product_id) REFERENCES products(product_id) 
        ON DELETE RESTRICT
);
