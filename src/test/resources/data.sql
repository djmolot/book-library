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
UPDATE books SET reader_id = 2 WHERE id = 1;
UPDATE books SET reader_id = 2 WHERE id = 2;