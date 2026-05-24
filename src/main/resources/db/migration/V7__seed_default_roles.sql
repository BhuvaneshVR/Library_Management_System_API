-- Safe idempotent migration: seed default role names if they do not already exist.
-- This script preserves existing data and does not alter or drop any objects.

INSERT INTO roles (role_name)
SELECT 'ADMIN'
WHERE NOT EXISTS (
    SELECT 1 FROM roles WHERE role_name = 'ADMIN'
);

INSERT INTO roles (role_name)
SELECT 'LIBRARIAN'
WHERE NOT EXISTS (
    SELECT 1 FROM roles WHERE role_name = 'LIBRARIAN'
);
