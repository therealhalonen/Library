-- Start from scratch
SET FOREIGN_KEY_CHECKS=0;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS category_seq;
DROP TABLE IF EXISTS book;
DROP TABLE IF EXISTS book_seq;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS loan;
SET FOREIGN_KEY_CHECKS=1;

-- Add sequences
CREATE SEQUENCE book_seq START WITH 4 INCREMENT BY 1;
CREATE SEQUENCE category_seq START WITH 4 INCREMENT BY 1;

-- Add Category table
CREATE TABLE category (
    categoryid INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255)
);

-- Add Book table
CREATE TABLE book (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    author VARCHAR(255),
    publication_year INT,
    isbn VARCHAR(255),
    price DECIMAL(10, 2),
    categoryid INT,
    status VARCHAR(255) DEFAULT 'AVAILABLE',
    FOREIGN KEY (categoryid) REFERENCES category(categoryid)
);

-- Add Users table
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL
);

-- Add Categories
INSERT INTO category (name) VALUES ('Bad');
INSERT INTO category (name) VALUES ('Good');
INSERT INTO category (name) VALUES ('Ugly');

-- Add Loan table
CREATE TABLE loan (
    id INT AUTO_INCREMENT PRIMARY KEY,
    userid INT,
    bookid INT,
    FOREIGN KEY (userid) REFERENCES users(id),
    FOREIGN KEY (bookid) REFERENCES book(id)
);
-- Add some Books
INSERT INTO book (id, title, author, publication_year, isbn, price, categoryid)
VALUES (1, 'GoodBook', 'Me', 1985, '1234567890', 66.6, 2);
INSERT INTO book (id, title, author, publication_year, isbn, price, categoryid)
VALUES (2, 'BadBook', 'Teacher', 2023, '0987654321', 6.66, 1);
INSERT INTO book (id, title, author, publication_year, isbn, price, categoryid)
VALUES (3, 'UglyBook', 'SomeOne', 2024, '69', 666.0, 3);

-- Add the Admin user
INSERT INTO users (username, password, role) VALUES ('admin', '$2b$10$o0liCdP9lzTgCHarj9JCdeoyWICDjRpQCYS4TP97jSnRc5tlriKPS', 'ADMIN');
INSERT INTO users (username, password, role) VALUES ('admin2', '$2b$10$o0liCdP9lzTgCHarj9JCdeoyWICDjRpQCYS4TP97jSnRc5tlriKPS', 'ADMIN');
