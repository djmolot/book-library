DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS readers;

CREATE SEQUENCE readers_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE books_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE readers(
   id BIGINT DEFAULT nextval('readers_id_seq') PRIMARY KEY,
   name VARCHAR(50) NOT NULL
);

CREATE TABLE books(
   id BIGINT DEFAULT nextval('books_id_seq') PRIMARY KEY,
   title VARCHAR(255) NOT NULL,
   author VARCHAR(50) NOT NULL,
   reader_id BIGINT,
   CONSTRAINT fk_reader_id
      FOREIGN KEY(reader_id)
      REFERENCES readers(id)
      ON DELETE SET NULL
);
