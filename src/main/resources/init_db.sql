CREATE DATABASE booklibrary
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    CONNECTION LIMIT = -1;
-- --------------------------------------------------------------
DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS readers;

CREATE TABLE readers(
   id BIGSERIAL PRIMARY KEY,
   name VARCHAR(50) NOT NULL
);

CREATE TABLE books(
   id BIGSERIAL PRIMARY KEY,
   title VARCHAR(255) NOT NULL,
   author VARCHAR(50) NOT NULL,
   reader_id BIGINT,
   CONSTRAINT fk_reader_id
      FOREIGN KEY(reader_id)
	  REFERENCES readers(id)
	  ON DELETE SET NULL
);
-- --------------------------------------------------------------
INSERT INTO books (title, author) VALUES
('Java. The Complete Reference. Twelfth Edition', 'Herbert Schildt'),
('Java. An Introduction to Problem Solving & Programming', 'Walter Savitch'),
('Data Structures And Algorithms Made Easy In JAVA', 'Narasimha Karumanchi');
-- --------------------------------------------------------------
INSERT INTO readers (name) VALUES
('Zhirayr Hovik'),
('Voski Daniel'),
('Ruben Nazaret');
-- --------------------------------------------------------------
