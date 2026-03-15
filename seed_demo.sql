USE story_db;

-- ============================================================
-- 1) USERS DEMO (chạy lại nhiều lần vẫn an toàn)
-- ============================================================
INSERT INTO users (device_id, username, email, password, avatar_url)
VALUES
    ('demo-device-001', 'demo_user_1', 'demo1@example.com', 'demo_hash_1', 'https://i.pravatar.cc/150?img=11'),
    ('demo-device-002', 'demo_user_2', 'demo2@example.com', 'demo_hash_2', 'https://i.pravatar.cc/150?img=12'),
    ('demo-device-003', 'demo_user_3', 'demo3@example.com', 'demo_hash_3', 'https://i.pravatar.cc/150?img=13')
ON DUPLICATE KEY UPDATE
    username = VALUES(username),
    avatar_url = VALUES(avatar_url);

-- ============================================================
-- 2) FAVORITES DEMO
-- user 1: 5 truyện mới nhất
-- user 2: 5 truyện tiếp theo
-- user 3: 5 truyện tiếp theo nữa
-- ============================================================
INSERT IGNORE INTO favorites (user_id, story_id)
SELECT u.id, s.id
FROM users u
JOIN (SELECT id FROM stories ORDER BY id DESC LIMIT 5 OFFSET 0) s
WHERE u.device_id = 'demo-device-001';

INSERT IGNORE INTO favorites (user_id, story_id)
SELECT u.id, s.id
FROM users u
JOIN (SELECT id FROM stories ORDER BY id DESC LIMIT 5 OFFSET 5) s
WHERE u.device_id = 'demo-device-002';

INSERT IGNORE INTO favorites (user_id, story_id)
SELECT u.id, s.id
FROM users u
JOIN (SELECT id FROM stories ORDER BY id DESC LIMIT 5 OFFSET 10) s
WHERE u.device_id = 'demo-device-003';

-- ============================================================
-- 3) READING_HISTORY DEMO
-- Lấy chương mới nhất của từng truyện để giả lập "đang đọc"
-- ============================================================
INSERT INTO reading_history (user_id, story_id, last_chapter_id, scroll_position)
SELECT u.id, c_last.story_id, c_last.chapter_id, 82
FROM users u
JOIN (
    SELECT c.story_id, c.id AS chapter_id
    FROM chapters c
    JOIN (
        SELECT story_id, MAX(chapter_number) AS max_ch
        FROM chapters
        GROUP BY story_id
    ) m ON m.story_id = c.story_id AND m.max_ch = c.chapter_number
    ORDER BY c.story_id DESC
    LIMIT 6 OFFSET 0
) c_last
WHERE u.device_id = 'demo-device-001'
ON DUPLICATE KEY UPDATE
    last_chapter_id = VALUES(last_chapter_id),
    scroll_position = VALUES(scroll_position),
    updated_at = CURRENT_TIMESTAMP;

INSERT INTO reading_history (user_id, story_id, last_chapter_id, scroll_position)
SELECT u.id, c_last.story_id, c_last.chapter_id, 45
FROM users u
JOIN (
    SELECT c.story_id, c.id AS chapter_id
    FROM chapters c
    JOIN (
        SELECT story_id, MAX(chapter_number) AS max_ch
        FROM chapters
        GROUP BY story_id
    ) m ON m.story_id = c.story_id AND m.max_ch = c.chapter_number
    ORDER BY c.story_id DESC
    LIMIT 6 OFFSET 6
) c_last
WHERE u.device_id = 'demo-device-002'
ON DUPLICATE KEY UPDATE
    last_chapter_id = VALUES(last_chapter_id),
    scroll_position = VALUES(scroll_position),
    updated_at = CURRENT_TIMESTAMP;

INSERT INTO reading_history (user_id, story_id, last_chapter_id, scroll_position)
SELECT u.id, c_last.story_id, c_last.chapter_id, 16
FROM users u
JOIN (
    SELECT c.story_id, c.id AS chapter_id
    FROM chapters c
    JOIN (
        SELECT story_id, MAX(chapter_number) AS max_ch
        FROM chapters
        GROUP BY story_id
    ) m ON m.story_id = c.story_id AND m.max_ch = c.chapter_number
    ORDER BY c.story_id DESC
    LIMIT 6 OFFSET 12
) c_last
WHERE u.device_id = 'demo-device-003'
ON DUPLICATE KEY UPDATE
    last_chapter_id = VALUES(last_chapter_id),
    scroll_position = VALUES(scroll_position),
    updated_at = CURRENT_TIMESTAMP;

-- ============================================================
-- 4) (Optional) cập nhật lượt xem/rating demo cho truyện chưa có dữ liệu
-- ============================================================
UPDATE stories
SET
    views = CASE WHEN views = 0 THEN FLOOR(1000 + RAND() * 90000) ELSE views END,
    rating = CASE WHEN rating = 0 THEN ROUND(6 + RAND() * 3.9, 1) ELSE rating END
WHERE id > 0;

-- ============================================================
-- 5) Kiểm tra nhanh kết quả seed
-- ============================================================
SELECT COUNT(*) AS total_users FROM users;
SELECT COUNT(*) AS total_favorites FROM favorites;
SELECT COUNT(*) AS total_reading_history FROM reading_history;
