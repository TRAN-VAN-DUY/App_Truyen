CREATE DATABASE IF NOT EXISTS story_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
USE story_db;

-- ============================================================
-- Bảng người dùng
-- ============================================================
CREATE TABLE users (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    device_id   VARCHAR(255) UNIQUE,                  -- Dùng cho app không cần đăng nhập
    username    VARCHAR(100),
    email       VARCHAR(255) UNIQUE,
    password    VARCHAR(255),                          -- hash bcrypt
    avatar_url  VARCHAR(500),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- Bảng thể loại
-- ============================================================
CREATE TABLE genres (
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    slug VARCHAR(100) NOT NULL UNIQUE
);

-- ============================================================
-- Bảng truyện
-- ============================================================
CREATE TABLE stories (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    title           VARCHAR(255) NOT NULL,
    slug            VARCHAR(255) NOT NULL UNIQUE,     -- URL thân thiện: "truyen-kieu"
    author          VARCHAR(255),
    description     TEXT,
    cover_url       VARCHAR(500),
    status          ENUM('ongoing', 'completed', 'hiatus') DEFAULT 'ongoing',
    total_chapters  INT DEFAULT 0,
    views           BIGINT DEFAULT 0,
    rating          DECIMAL(3,1) DEFAULT 0.0,         -- 0.0 - 10.0
    source_url      VARCHAR(500),                     -- URL gốc khi crawl
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_slug      (slug),
    INDEX idx_status    (status),
    INDEX idx_updated   (updated_at),
    INDEX idx_views     (views)
);

-- ============================================================
-- Bảng quan hệ truyện - thể loại (nhiều-nhiều)
-- ============================================================
CREATE TABLE story_genres (
    story_id INT NOT NULL,
    genre_id INT NOT NULL,
    PRIMARY KEY (story_id, genre_id),
    FOREIGN KEY (story_id) REFERENCES stories(id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES genres(id)   ON DELETE CASCADE
);

-- ============================================================
-- Bảng chương
-- ============================================================
CREATE TABLE chapters (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    story_id        INT NOT NULL,
    chapter_number  INT NOT NULL,
    title           VARCHAR(255),
    content         LONGTEXT,
    word_count      INT DEFAULT 0,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uq_story_chapter (story_id, chapter_number),
    FOREIGN KEY (story_id) REFERENCES stories(id) ON DELETE CASCADE,
    INDEX idx_story_chapter (story_id, chapter_number)
);

-- ============================================================
-- Bảng yêu thích
-- ============================================================
CREATE TABLE favorites (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    user_id     INT NOT NULL,
    story_id    INT NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uq_user_story (user_id, story_id),
    FOREIGN KEY (user_id)  REFERENCES users(id)   ON DELETE CASCADE,
    FOREIGN KEY (story_id) REFERENCES stories(id) ON DELETE CASCADE
);

-- ============================================================
-- Bảng lịch sử / đang đọc
-- ============================================================
CREATE TABLE reading_history (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    user_id          INT NOT NULL,
    story_id         INT NOT NULL,
    last_chapter_id  INT NOT NULL,
    scroll_position  INT DEFAULT 0,                   -- % đã đọc trong chương (0-100)
    updated_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uq_user_story (user_id, story_id),
    FOREIGN KEY (user_id)          REFERENCES users(id)    ON DELETE CASCADE,
    FOREIGN KEY (story_id)         REFERENCES stories(id)  ON DELETE CASCADE,
    FOREIGN KEY (last_chapter_id)  REFERENCES chapters(id) ON DELETE CASCADE,
    INDEX idx_user_updated (user_id, updated_at)
);
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