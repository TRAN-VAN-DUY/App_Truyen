# App Truyện — Backend

Backend cho ứng dụng đọc truyện mobile, xây dựng bằng **Java Spring Boot 3** + **MySQL**.

---

## Cấu trúc thư mục

```
backend/
├── database.sql          # Schema cơ sở dữ liệu
├── seed_demo.sql         # Dữ liệu demo (users, favorites, reading_history)
├── story-api/            # Spring Boot API server
│   ├── pom.xml
│   ├── README.md         # Hướng dẫn test từng API
│   └── src/
└── crawler/              # Crawler lấy dữ liệu truyện
```

---

## Cơ sở dữ liệu

| Bảng | Mô tả |
|------|-------|
| `users` | Người dùng (hỗ trợ cả device_id và email/password) |
| `roles` | Phân quyền: `admin`, `user` |
| `stories` | Thông tin truyện (tiêu đề, tác giả, trạng thái, views, rating…) |
| `chapters` | Chương truyện (nội dung, số từ…) |
| `genres` | Thể loại |
| `story_genres` | Quan hệ nhiều-nhiều truyện ↔ thể loại |
| `favorites` | Truyện yêu thích của từng user |
| `reading_history` | Lịch sử đọc: chapter đang đọc + scroll position |

---


---

## Chức năng do dev khác phụ trách

- Đăng ký / Đăng nhập (email + password)
- Quản lý thông tin người dùng
- Generate JWT token

> JWT payload yêu cầu: `{ "sub": "<userId>", ... }` — phải dùng chung `jwt.secret` trong `story-api/src/main/resources/application.yml`

---

## Khởi động API server

```bash
cd story-api
mvn spring-boot:run
```

- **Base URL:** `http://localhost:8080`
- **Swagger UI:** `http://localhost:8080/swagger-ui.html`
- **Hướng dẫn test:** xem [`story-api/README.md`](./story-api/README.md)
