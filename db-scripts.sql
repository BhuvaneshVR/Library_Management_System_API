-- Database script reference for Library Management System
-- Includes schema definitions and sample insert queries.
-- Order:
--   1. Books
--   2. Members
--   3. Transactions
-- Note: These are reference scripts and do not replace Flyway migrations.

-- =============================
-- 1. Books schema
-- =============================
CREATE TABLE IF NOT EXISTS books (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    isbn VARCHAR(100) NOT NULL UNIQUE,
    category VARCHAR(100),
    total_copies INT NOT NULL,
    available_copies INT NOT NULL,
    shelf_location VARCHAR(100),
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_isbn (isbn),
    INDEX idx_title (title),
    INDEX idx_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================
-- 2. Members schema
-- =============================
CREATE TABLE IF NOT EXISTS members (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================
-- 3. Transactions schema
-- =============================
CREATE TABLE IF NOT EXISTS transactions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    book_id BIGINT NOT NULL,
    member_id BIGINT NOT NULL,
    issued_at TIMESTAMP NOT NULL,
    due_date TIMESTAMP NOT NULL,
    returned_at TIMESTAMP NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_transaction_book FOREIGN KEY(book_id) REFERENCES books(id),
    CONSTRAINT fk_transaction_member FOREIGN KEY(member_id) REFERENCES members(id),
    INDEX idx_book_member (book_id, member_id),
    INDEX idx_status (status),
    INDEX idx_issued_at (issued_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================
-- 4. Optional supporting schema (roles/users)
-- =============================
CREATE TABLE IF NOT EXISTS roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES roles(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================
-- 5. Sample insert queries
-- =============================
-- Books sample data
INSERT INTO books (title, author, isbn, category, total_copies, available_copies, shelf_location)
SELECT 'The Great Gatsby', 'F. Scott Fitzgerald', '9780743273565', 'Classics', 7, 7, 'A1'
WHERE NOT EXISTS (
    SELECT 1 FROM books WHERE isbn = '9780743273565'
);

INSERT INTO books (title, author, isbn, category, total_copies, available_copies, shelf_location)
SELECT '1984', 'George Orwell', '9780451524935', 'Dystopian', 10, 10, 'A2'
WHERE NOT EXISTS (
    SELECT 1 FROM books WHERE isbn = '9780451524935'
);

INSERT INTO books (title, author, isbn, category, total_copies, available_copies, shelf_location)
SELECT 'Clean Code', 'Robert C. Martin', '9780132350884', 'Programming', 5, 5, 'B3'
WHERE NOT EXISTS (
    SELECT 1 FROM books WHERE isbn = '9780132350884'
);

-- Members sample data
INSERT INTO members (name, email, status)
SELECT 'Emma Johnson', 'emma.johnson@example.com', 'ACTIVE'
WHERE NOT EXISTS (
    SELECT 1 FROM members WHERE email = 'emma.johnson@example.com'
);

INSERT INTO members (name, email, status)
SELECT 'Liam Smith', 'liam.smith@example.com', 'ACTIVE'
WHERE NOT EXISTS (
    SELECT 1 FROM members WHERE email = 'liam.smith@example.com'
);

INSERT INTO members (name, email, status)
SELECT 'Olivia Brown', 'olivia.brown@example.com', 'ACTIVE'
WHERE NOT EXISTS (
    SELECT 1 FROM members WHERE email = 'olivia.brown@example.com'
);

-- Transactions sample data
INSERT INTO transactions (book_id, member_id, issued_at, due_date, status)
SELECT b.id, m.id, '2026-05-10 09:00:00', '2026-05-24 09:00:00', 'ISSUED'
FROM books b
JOIN members m ON b.isbn = '9780743273565' AND m.email = 'emma.johnson@example.com'
WHERE NOT EXISTS (
    SELECT 1 FROM transactions t
    WHERE t.book_id = b.id
      AND t.member_id = m.id
      AND t.issued_at = '2026-05-10 09:00:00'
);

INSERT INTO transactions (book_id, member_id, issued_at, due_date, returned_at, status)
SELECT b.id, m.id, '2026-05-01 14:00:00', '2026-05-15 14:00:00', '2026-05-14 10:00:00', 'RETURNED'
FROM books b
JOIN members m ON b.isbn = '9780451524935' AND m.email = 'liam.smith@example.com'
WHERE NOT EXISTS (
    SELECT 1 FROM transactions t
    WHERE t.book_id = b.id
      AND t.member_id = m.id
      AND t.issued_at = '2026-05-01 14:00:00'
);

-- Optional user/role seed sample data
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

INSERT INTO users (username, password, full_name, email, enabled, created_at, updated_at)
SELECT 'admin', 'REPLACE_WITH_HASHED_PASSWORD', 'System Administrator', 'admin@library.com', TRUE, NOW(), NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE username = 'admin'
);

INSERT INTO users (username, password, full_name, email, enabled, created_at, updated_at)
SELECT 'librarian', 'REPLACE_WITH_HASHED_PASSWORD', 'Library Librarian', 'librarian@library.com', TRUE, NOW(), NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE username = 'librarian'
);

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
JOIN roles r ON u.username = 'admin' AND r.role_name = 'ADMIN'
WHERE NOT EXISTS (
    SELECT 1 FROM user_roles ur
    WHERE ur.user_id = u.id
      AND ur.role_id = r.id
);

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
JOIN roles r ON u.username = 'librarian' AND r.role_name = 'LIBRARIAN'
WHERE NOT EXISTS (
    SELECT 1 FROM user_roles ur
    WHERE ur.user_id = u.id
      AND ur.role_id = r.id
);
