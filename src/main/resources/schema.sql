-- Start from scratch
DROP DATABASE IF EXISTS library_db;
CREATE DATABASE library_db;
USE library_db;

SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS borrow;
DROP TABLE IF EXISTS book_like;
DROP TABLE IF EXISTS book;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS users;
SET FOREIGN_KEY_CHECKS = 1;

-- Add Categories table
CREATE TABLE category
(
    categoryid INT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(255) NOT NULL
);

-- Add Book table
CREATE TABLE book
(
    id               INT AUTO_INCREMENT PRIMARY KEY,
    title            VARCHAR(255) NOT NULL,
    author           VARCHAR(255),
    publication_year INT,
    isbn             VARCHAR(255) NOT NULL,
    price            DECIMAL(10, 2),
    categoryid       INT,
    status           VARCHAR(255) DEFAULT 'AVAILABLE',
    FOREIGN KEY (categoryid) REFERENCES category (categoryid)
);

-- Add Users table
CREATE TABLE users
(
    id       INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(255) NOT NULL
);

-- Add Borrow table
CREATE TABLE borrow
(
    id     INT AUTO_INCREMENT PRIMARY KEY,
    userid INT,
    bookid INT,
    FOREIGN KEY (userid) REFERENCES users (id),
    FOREIGN KEY (bookid) REFERENCES book (id)
);

-- Add Book Like table with many-to-many relationship
CREATE TABLE book_like
(
    id      INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    book_id INT,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (book_id) REFERENCES book (id)
);

-- Add the Admin users
INSERT INTO users (username, password, role)
VALUES ('admin', '$2b$10$o0liCdP9lzTgCHarj9JCdeoyWICDjRpQCYS4TP97jSnRc5tlriKPS', 'ADMIN');
INSERT INTO users (username, password, role)
VALUES ('admin2', '$2b$10$o0liCdP9lzTgCHarj9JCdeoyWICDjRpQCYS4TP97jSnRc5tlriKPS', 'ADMIN');
