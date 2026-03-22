
-- Xem 1 chương mẫu
SELECT story_id, chapter_number, title, LEFT(content, 5000000) AS preview
FROM chapters
ORDER BY id DESC
LIMIT 1;

-- xem toan bo noi dung 1 chapter
SELECT 
    story_id,
    chapter_number,
    title,
    content
FROM chapters
WHERE story_id = 27 AND chapter_number = 10;

-- 1) Tạo bảng roles
use story_db;
CREATE TABLE IF NOT EXISTS roles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL UNIQUE
);

-- 2) Seed role cơ bản
INSERT INTO roles (role_name) VALUES ('admin'), ('user')
ON DUPLICATE KEY UPDATE role_name = VALUES(role_name);

-- 3) Thêm role_id vào users (nếu chưa có)
ALTER TABLE users
ADD COLUMN role_id INT NULL AFTER avatar_url;

-- 4) Backfill user cũ -> role 'user'
UPDATE users u
JOIN roles r ON r.role_name = 'user'
SET u.role_id = r.id
WHERE u.role_id IS NULL;

-- 5) Set NOT NULL + default + index + FK
ALTER TABLE users
MODIFY role_id INT NOT NULL,
ADD INDEX idx_users_role_id (role_id),
ADD CONSTRAINT fk_users_role
    FOREIGN KEY (role_id) REFERENCES roles(id)
    ON UPDATE CASCADE
    ON DELETE RESTRICT;
select * from roles