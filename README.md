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

## Các chức năng đã làm

### 📖 Đọc truyện *(Public — không cần đăng nhập)*

- Lấy danh sách truyện có **phân trang**, filter theo **thể loại**, **trạng thái** (`ongoing / completed / hiatus`), sắp xếp theo **views / rating / mới nhất / mới cập nhật**
- **Tìm kiếm** truyện theo tên hoặc tác giả
- Xem **chi tiết truyện** (theo slug hoặc ID)
- Xem **danh sách chương** của một truyện (theo slug hoặc ID)
- **Đọc nội dung chương** (theo slug + số chương, hoặc theo chapter ID trực tiếp) — tự động tăng lượt xem, trả về navigation chương trước/sau

### ❤️ Truyện yêu thích *(Cần JWT)*

- Xem danh sách truyện đã yêu thích (phân trang, mới nhất trước)
- Thêm / Xoá truyện khỏi danh sách yêu thích
- Kiểm tra một truyện đã được yêu thích chưa

### ▶️ Xem tiếp *(Cần JWT)*

- Xem danh sách truyện đang đọc dở, sắp xếp theo lần đọc gần nhất
- Lưu tiến trình đọc: chapter hiện tại + scroll position (0–100%) — **tự động upsert**
- Lấy tiến trình đọc của một truyện cụ thể

---

## Chức năng do dev khác phụ trách

- Đăng ký / Đăng nhập (email + password)
- Quản lý thông tin người dùng
- Generate JWT token

> JWT payload yêu cầu: `{ "sub": "<userId>", ... }` — phải dùng chung `jwt.secret` trong `story-api/src/main/resources/application.yml`

---

## Khởi động API server
```bash
import file data_apptruyen_2.sql vào MySQL
Thay đổi thông tin tài khoản MySQL trong story-api\src\main\resources\application.yml
```
```bash
cd story-api
mvn spring-boot:run
```

- **Base URL:** `http://localhost:8080`
- **Swagger UI:** `http://localhost:8080/swagger-ui.html`
- **Hướng dẫn test:** xem [`story-api/README.md`](./story-api/README.md)

mvn spring-boot:run 2>&1 | Tee-Object -FilePath "server.log"
