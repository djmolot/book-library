ALTER TABLE books ADD COLUMN borrow_date DATE;
ALTER TABLE books ADD COLUMN max_borrow_time_in_days INT;
UPDATE books SET max_borrow_time_in_days = 21;
ALTER TABLE books ADD COLUMN restricted BOOLEAN NOT NULL DEFAULT FALSE;
UPDATE books SET restricted = FALSE;