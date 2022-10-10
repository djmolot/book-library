CREATE DATABASE booklibrary
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    CONNECTION LIMIT = -1;
-- --------------------------------------------------------------
DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS readers;

CREATE TABLE readers(
   id BIGINT GENERATED ALWAYS AS IDENTITY,
   name VARCHAR(50) NOT NULL,
   PRIMARY KEY(id)
);

CREATE TABLE books(
   id BIGINT GENERATED ALWAYS AS IDENTITY,
   title VARCHAR(255) NOT NULL,
   author VARCHAR(50) NOT NULL,
   reader_id BIGINT,
   PRIMARY KEY(id),
   CONSTRAINT fk_reader_id
      FOREIGN KEY(reader_id)
	  REFERENCES readers(id)
	  ON DELETE SET NULL
);
-- --------------------------------------------------------------
