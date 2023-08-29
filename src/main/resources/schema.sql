-- Start from scratch
SET FOREIGN_KEY_CHECKS=0;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS book;
SET FOREIGN_KEY_CHECKS=1;

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
    FOREIGN KEY (categoryid) REFERENCES category(categoryid)
);

-- Add Categories
INSERT INTO category (name) VALUES ('Bad');
INSERT INTO category (name) VALUES ('Good');
INSERT INTO category (name) VALUES ('Ugly');

-- Add some Books
INSERT INTO book (title, author, publication_year, isbn, price, categoryid)
VALUES ('GoodBook', 'Me', 1985, '1234567890', 66.6, 2);
VALUES ('BadBook', 'Teacher', 2023, '0987654321', 6.66, 1);
VALUES ('UglyBook', 'SomeOne', 2024, '69', 666.0, 3);


