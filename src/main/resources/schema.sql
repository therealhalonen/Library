-- Start from scratch
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS loan;
DROP TABLE IF EXISTS book_like;
DROP TABLE IF EXISTS book;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS category_seq;
DROP TABLE IF EXISTS book_seq;
DROP TABLE IF EXISTS user_book_like;
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

-- Add Loan table
CREATE TABLE loan
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

-- Add sequences
CREATE
SEQUENCE book_seq START
WITH 4 INCREMENT BY 1;
CREATE
SEQUENCE category_seq START
WITH 4 INCREMENT BY 1;

-- Add Categories
INSERT INTO category (name)
VALUES ('Category 1');
INSERT INTO category (name)
VALUES ('Category 2');
INSERT INTO category (name)
VALUES ('Category 3');

-- Add some Books
INSERT INTO book (title, author, publication_year, isbn, price, categoryid)
VALUES ('Book 1', 'Author 1', 2000, 'ISBN-1111', 10.99, 1);

INSERT INTO book (title, author, publication_year, isbn, price, categoryid)
VALUES ('Book 2', 'Author 2', 2010, 'ISBN-2222', 15.99, 2);

-- Add the Admin user
INSERT INTO users (username, password, role)
VALUES ('admin', '$2b$10$o0liCdP9lzTgCHarj9JCdeoyWICDjRpQCYS4TP97jSnRc5tlriKPS', 'ADMIN');
INSERT INTO users (username, password, role)
VALUES ('admin2', '$2b$10$o0liCdP9lzTgCHarj9JCdeoyWICDjRpQCYS4TP97jSnRc5tlriKPS', 'ADMIN');
