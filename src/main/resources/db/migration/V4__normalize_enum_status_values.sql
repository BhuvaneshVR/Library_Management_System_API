-- Normalize enum status values in existing rows so JPA enum mapping works consistently
UPDATE members SET status = UPPER(status) WHERE status IS NOT NULL;
UPDATE transactions SET status = UPPER(status) WHERE status IS NOT NULL;
