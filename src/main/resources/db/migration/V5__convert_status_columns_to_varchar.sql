-- Convert legacy enum-typed status columns to VARCHAR so JPA string mapping works consistently
ALTER TABLE members MODIFY COLUMN status VARCHAR(20) NOT NULL;
ALTER TABLE transactions MODIFY COLUMN status VARCHAR(20) NOT NULL;

-- Normalize values after conversion
UPDATE members SET status = UPPER(status) WHERE status IS NOT NULL;
UPDATE transactions SET status = UPPER(status) WHERE status IS NOT NULL;
