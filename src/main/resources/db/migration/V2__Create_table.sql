DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS readers;

CREATE TABLE readers(
   id BIGSERIAL PRIMARY KEY,
   name VARCHAR(50) NOT NULL,
   birth_date DATE
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
