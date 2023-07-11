ALTER TABLE books
ADD COLUMN max_borrow_time_in_days INT;
UPDATE books SET max_borrow_time_in_days = ${defaultMaxBorrowTimeInDays};

