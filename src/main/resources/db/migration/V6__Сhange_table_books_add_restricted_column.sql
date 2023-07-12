
ALTER TABLE books ADD restricted BOOLEAN;
ALTER TABLE books
  ADD CONSTRAINT check_restricted_values CHECK (restricted IN (TRUE, FALSE));
UPDATE books SET restricted = FALSE;