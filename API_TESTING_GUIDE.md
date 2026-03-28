# 📖 Hướng Dẫn Kiểm Tra API Đầy Đủ

## 🎯 Các Luồng Kiểm Tra Chính

### **Luồng 1: Đăng Ký & Đăng Nhập (Authentication)**
### **Luồng 2: Duyệt Truyện (Browse Stories)**
### **Luồng 3: Đọc Nội Dung & Theo Dõi Tiến Trình**
### **Luồng 4: Quản Lý Truyện Yêu Thích**
### **Luồng 5: Admin - Quản Lý Users & Roles**

---

## 📋 Chuẩn Bị

### URL Base
```
http://localhost:8080
```

### Headers Chung
```yaml
Content-Type: application/json
```

### Headers Cho API Secured (với token)
```yaml
Content-Type: application/json
Authorization: Bearer {token}
```

---

## ✅ LUỒNG 1: ĐĂNG KÝ & ĐĂNG NHẬP

### 1.1 Đăng ký bằng Email & Password

**Request:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "nguyenviet",
    "email": "nguyenviet@example.com",
    "password": "Password@123"
  }'
```

**Expected Response (201/200):**
```json
{
  "success": true,
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjc2NDU2NzUwfQ.u...",
    "userId": 1,
    "username": "nguyenviet"
  },
  "message": "Đăng ký thành công"
}
```

**Lưu lại Token:** `TOKEN_USER_1=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...`

---

### 1.2 Đăng ký bằng Device ID (Android/iOS)

**Request:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "deviceId": "device_android_12345"
  }'
```

**Expected Response:**
```json
{
  "success": true,
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "userId": 2,
    "username": "guest_device_12345"
  },
  "message": "Đăng ký device thành công"
}
```

**Lưu lại Token:** `TOKEN_DEVICE=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...`

---

### 1.3 Đăng Nhập bằng Email & Password

**Request:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "nguyenviet@example.com",
    "password": "Password@123"
  }'
```

**Expected Response:**
```json
{
  "success": true,
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "userId": 1,
    "username": "nguyenviet"
  },
  "message": "Đăng nhập thành công"
}
```

---

### 1.4 Đăng Nhập bằng Device ID

**Request:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "deviceId": "device_android_12345"
  }'
```

**Expected Response:**
```json
{
  "success": true,
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "userId": 2,
    "username": "guest_device_12345"
  },
  "message": "Đăng nhập thành công"
}
```

---

## 📚 LUỒNG 2: DUYỆT TRUYỆN (PUBLIC)

### 2.1 Lấy Danh Sách Truyện (Phân Trang & Filter)

**Request - Trang 1, mỗi trang 10 truyện:**
```bash
curl -X GET "http://localhost:8080/api/v1/stories?page=0&size=10&sort=updated" \
  -H "Content-Type: application/json"
```

**Expected Response:**
```json
{
  "success": true,
  "data": {
    "content": [
      {
        "id": 1,
        "slug": "tieu-su-thien-khong",
        "title": "Tiêu Sử Thiên Không",
        "author": "Tác Giả A",
        "thumbnail": "https://example.com/images/1.jpg",
        "description": "Một bộ truyện hành động...",
        "status": "ongoing",
        "views": 1000,
        "rating": 4.5,
        "totalChapters": 250
      },
      ...
    ],
    "currentPage": 0,
    "totalPages": 5,
    "totalElements": 45,
    "hasNext": true,
    "hasPrevious": false
  }
}
```

---

### 2.2 Lọc Truyện theo Thể Loại

**Request - Thể loại "hành động":**
```bash
curl -X GET "http://localhost:8080/api/v1/stories?page=0&size=10&genre=action&sort=views" \
  -H "Content-Type: application/json"
```

**Expected Response:** (Danh sách chỉ chứa truyện thể loại hành động)

---

### 2.3 Lọc Truyện theo Trạng Thái

**Request - Chỉ truyện hoàn thành:**
```bash
curl -X GET "http://localhost:8080/api/v1/stories?page=0&size=10&status=completed" \
  -H "Content-Type: application/json"
```

**Các giá trị status:**
- `ongoing` - Đang cập nhật
- `completed` - Hoàn thành
- `hiatus` - Tạm dừng

---

### 2.4 Tìm Kiếm Truyện theo Tên/Tác Giả

**Request - Tìm "Tiêu Sử":**
```bash
curl -X GET "http://localhost:8080/api/v1/stories/search?keyword=Tiêu Sử&page=0&size=10" \
  -H "Content-Type: application/json"
```

**Expected Response:**
```json
{
  "success": true,
  "data": {
    "content": [
      {
        "id": 1,
        "title": "Tiêu Sử Thiên Không",
        "author": "Tác Giả A",
        "slug": "tieu-su-thien-khong",
        "thumbnail": "...",
        "status": "ongoing",
        "views": 1000
      }
    ],
    "currentPage": 0,
    "totalPages": 1,
    "totalElements": 1
  }
}
```

---

### 2.5 Lấy Chi Tiết Truyện

**Request - By Slug:**
```bash
curl -X GET "http://localhost:8080/api/v1/stories/tieu-su-thien-khong" \
  -H "Content-Type: application/json"
```

**Request - By ID:**
```bash
curl -X GET "http://localhost:8080/api/v1/stories/id/1" \
  -H "Content-Type: application/json"
```

**Expected Response:**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "title": "Tiêu Sử Thiên Không",
    "slug": "tieu-su-thien-khong",
    "author": "Tác Giả A",
    "description": "Mô tả chi tiết...",
    "thumbnail": "https://example.com/images/1.jpg",
    "status": "ongoing",
    "views": 1000,
    "rating": 4.5,
    "totalChapters": 250,
    "genres": [
      {
        "id": 1,
        "name": "Hành Động",
        "slug": "action"
      },
      {
        "id": 2,
        "name": "Phiêu Lưu",
        "slug": "adventure"
      }
    ]
  }
}
```

---

### 2.6 Lấy Danh Sách Chương

**Request - By Slug:**
```bash
curl -X GET "http://localhost:8080/api/v1/stories/tieu-su-thien-khong/chapters" \
  -H "Content-Type: application/json"
```

**Request - By Story ID:**
```bash
curl -X GET "http://localhost:8080/api/v1/stories/id/1/chapters" \
  -H "Content-Type: application/json"
```

**Expected Response:**
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "chapterNumber": 1,
      "title": "Chương 1: Khởi Đầu",
      "wordCount": 3500,
      "createdAt": "2025-01-01T10:00:00"
    },
    {
      "id": 2,
      "chapterNumber": 2,
      "title": "Chương 2: Phát Triển",
      "wordCount": 3800,
      "createdAt": "2025-01-02T10:00:00"
    },
    ...
  ]
}
```

---

## 📖 LUỒNG 3: ĐỌC NỘI DUNG & THEO DÕI TIẾN TRÌNH

### 3.1 Đọc Nội Dung Chương (Tự động tăng views)

**Request - By Slug + Chapter Number:**
```bash
curl -X GET "http://localhost:8080/api/v1/stories/tieu-su-thien-khong/chapters/1" \
  -H "Content-Type: application/json"
```

**Request - By Chapter ID:**
```bash
curl -X GET "http://localhost:8080/api/v1/stories/chapters/1" \
  -H "Content-Type: application/json"
```

**Expected Response:**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "storyId": 1,
    "storySlug": "tieu-su-thien-khong",
    "chapterNumber": 1,
    "title": "Chương 1: Khởi Đầu",
    "content": "<p>Nội dung chương 1...</p><p>Đây là phần nội dung chính của chương...</p>",
    "wordCount": 3500,
    "createdAt": "2025-01-01T10:00:00",
    "previousChapterNumber": null,
    "nextChapterNumber": 2
  }
}
```

---

### 3.2 Lưu Tiến Trình Đọc (Với Authentication - **TOKEN_USER_1**)

**Request - Lưu chapter đang đọc & vị trí scroll:**
```bash
curl -X POST "http://localhost:8080/api/v1/reading-history" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN_USER_1" \
  -d '{
    "storyId": 1,
    "lastChapterId": 5,
    "scrollPosition": 0.45
  }'
```

**Expected Response (201/200):**
```json
{
  "success": true,
  "data": {
    "storyId": 1,
    "storyTitle": "Tiêu Sử Thiên Không",
    "lastChapterId": 5,
    "lastChapterNumber": 5,
    "lastChapterTitle": "Chương 5: Tiếp Tục",
    "scrollPosition": 0.45,
    "updatedAt": "2025-03-27T15:30:00"
  },
  "message": "Đã lưu tiến trình"
}
```

---

### 3.3 Lấy Danh Sách "Xem Tiếp" (Continue Reading)

**Request - Danh sách truyện đang đọc dở:**
```bash
curl -X GET "http://localhost:8080/api/v1/reading-history?page=0&size=20" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN_USER_1"
```

**Expected Response:**
```json
{
  "success": true,
  "data": {
    "content": [
      {
        "storyId": 1,
        "storySlug": "tieu-su-thien-khong",
        "storyTitle": "Tiêu Sử Thiên Không",
        "storyThumbnail": "https://example.com/images/1.jpg",
        "lastChapterne": 5,
        "lastChapterNumber": 5,
        "lastChapterTitle": "Chương 5: Tiếp Tục",
        "scrollPosition": 0.45,
        "updatedAt": "2025-03-27T15:30:00"
      },
      {
        "storyId": 3,
        "storyTitle": "Tuyệt Đỉnh Cao Thủ",
        "lastChapterNumber": 10,
        "lastChapterTitle": "Chương 10: Tiến Hoá",
        "scrollPosition": 0.0,
        "updatedAt": "2025-03-26T12:00:00"
      }
    ],
    "currentPage": 0,
    "totalPages": 1,
    "totalElements": 2
  }
}
```

---

### 3.4 Lấy Tiến Trình Đọc Của 1 Truyện

**Request:**
```bash
curl -X GET "http://localhost:8080/api/v1/reading-history/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN_USER_1"
```

**Expected Response:**
```json
{
  "success": true,
  "data": {
    "storyId": 1,
    "storyTitle": "Tiêu Sử Thiên Không",
    "lastChapterId": 5,
    "lastChapterNumber": 5,
    "lastChapterTitle": "Chương 5: Tiếp Tục",
    "scrollPosition": 0.45,
    "updatedAt": "2025-03-27T15:30:00"
  }
}
```

---

## ❤️ LUỒNG 4: QUẢN LÝ TRUYỆN YÊU THÍCH

### 4.1 Thêm Truyện Vào Yêu Thích

**Request - Thêm story ID 1 vào yêu thích:**
```bash
curl -X POST "http://localhost:8080/api/v1/favorites" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN_USER_1" \
  -d '{
    "storyId": 1
  }'
```

**Expected Response (201):**
```json
{
  "success": true,
  "data": {
    "userId": 1,
    "storyId": 1,
    "storyTitle": "Tiêu Sử Thiên Không",
    "storyThumbnail": "https://example.com/images/1.jpg",
    "addedAt": "2025-03-27T15:45:00"
  },
  "message": "Đã thêm vào yêu thích"
}
```

---

### 4.2 Lấy Danh Sách Truyện Yêu Thích

**Request:**
```bash
curl -X GET "http://localhost:8080/api/v1/favorites?page=0&size=20" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN_USER_1"
```

**Expected Response:**
```json
{
  "success": true,
  "data": {
    "content": [
      {
        "userId": 1,
        "storyId": 1,
        "storyTitle": "Tiêu Sử Thiên Không",
        "storySlug": "tieu-su-thien-khong",
        "storyThumbnail": "https://example.com/images/1.jpg",
        "author": "Tác Giả A",
        "status": "ongoing",
        "addedAt": "2025-03-27T15:45:00"
      },
      {
        "userId": 1,
        "storyId": 3,
        "storyTitle": "Tuyệt Đỉnh Cao Thủ",
        "storySlug": "tuyet-dinh-cao-thu",
        "storyThumbnail": "https://example.com/images/3.jpg",
        "author": "Tác Giả C",
        "status": "completed",
        "addedAt": "2025-03-26T10:00:00"
      }
    ],
    "currentPage": 0,
    "totalPages": 1,
    "totalElements": 2
  }
}
```

---

### 4.3 Kiểm Tra Truyện Đã Yêu Thích Chưa

**Request:**
```bash
curl -X GET "http://localhost:8080/api/v1/favorites/1/check" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN_USER_1"
```

**Expected Response:**
```json
{
  "success": true,
  "data": {
    "favorited": true
  }
}
```

---

### 4.4 Xoá Truyện Khỏi Yêu Thích

**Request - Xoá story ID 1:**
```bash
curl -X DELETE "http://localhost:8080/api/v1/favorites/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN_USER_1"
```

**Expected Response (200):**
```json
{
  "success": true,
  "data": null,
  "message": "Đã xoá khỏi yêu thích"
}
```

---

## 👥 LUỒNG 5: ADMIN - QUẢN LÝ USERS & ROLES

### ⚠️ Yêu Cầu: Token Admin (ROLE_ADMIN)

Đầu tiên, cần tạo admin account trong database hoặc thông qua API.

**Ví dụ:**
```bash
TOKEN_ADMIN="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxIiwicm9sZSI6IlJPTEVfQURNSU4iLCJpYXQiOjE2NzY0NTY3NTB9.u..."
```

---

### 5.1 Lấy Danh Sách Tất Cả Users

**Request:**
```bash
curl -X GET "http://localhost:8080/api/admin/users" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN_ADMIN"
```

**Expected Response:**
```json
[
  {
    "id": 1,
    "username": "nguyenviet",
    "email": "nguyenviet@example.com",
    "deviceId": null,
    "avatarUrl": "https://example.com/avatar1.jpg",
    "role": {
      "id": 1,
      "roleName": "ROLE_USER"
    },
    "createdAt": "2025-03-27T10:00:00"
  },
  {
    "id": 2,
    "username": "admin_user",
    "email": "admin@example.com",
    "deviceId": null,
    "avatarUrl": null,
    "role": {
      "id": 2,
      "roleName": "ROLE_ADMIN"
    },
    "createdAt": "2025-03-01T09:00:00"
  }
]
```

---

### 5.2 Lấy Chi Tiết User

**Request:**
```bash
curl -X GET "http://localhost:8080/api/admin/users/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN_ADMIN"
```

**Expected Response:**
```json
{
  "id": 1,
  "username": "nguyenviet",
  "email": "nguyenviet@example.com",
  "deviceId": null,
  "avatarUrl": "https://example.com/avatar1.jpg",
  "role": {
    "id": 1,
    "roleName": "ROLE_USER"
  },
  "createdAt": "2025-03-27T10:00:00"
}
```

---

### 5.3 Tạo User Mới

**Request:**
```bash
curl -X POST "http://localhost:8080/api/admin/users" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN_ADMIN" \
  -d '{
    "username": "user_new",
    "email": "user_new@example.com",
    "password": "Password@456",
    "avatarUrl": "https://example.com/avatar_new.jpg",
    "role": {
      "id": 1,
      "roleName": "ROLE_USER"
    }
  }'
```

**Expected Response (201):**
```json
{
  "id": 10,
  "username": "user_new",
  "email": "user_new@example.com",
  "deviceId": null,
  "avatarUrl": "https://example.com/avatar_new.jpg",
  "role": {
    "id": 1,
    "roleName": "ROLE_USER"
  },
  "createdAt": "2025-03-27T16:00:00"
}
```

---

### 5.4 Cập Nhật User

**Request:**
```bash
curl -X PUT "http://localhost:8080/api/admin/users/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN_ADMIN" \
  -d '{
    "username": "nguyenviet_updated",
    "email": "nguyenviet_new@example.com",
    "password": "NewPassword@789",
    "avatarUrl": "https://example.com/avatar_updated.jpg",
    "role": {
      "id": 2,
      "roleName": "ROLE_ADMIN"
    }
  }'
```

**Expected Response (200):**
```json
{
  "id": 1,
  "username": "nguyenviet_updated",
  "email": "nguyenviet_new@example.com",
  "avatarUrl": "https://example.com/avatar_updated.jpg",
  "role": {
    "id": 2,
    "roleName": "ROLE_ADMIN"
  }
}
```

---

### 5.5 Xoá User

**Request:**
```bash
curl -X DELETE "http://localhost:8080/api/admin/users/10" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN_ADMIN"
```

**Expected Response (204):** (No Content)

---

### 5.6 Lấy Danh Sách Roles

**Request:**
```bash
curl -X GET "http://localhost:8080/api/admin/roles" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN_ADMIN"
```

**Expected Response:**
```json
[
  {
    "id": 1,
    "roleName": "ROLE_USER"
  },
  {
    "id": 2,
    "roleName": "ROLE_ADMIN"
  }
]
```

---

### 5.7 Tạo Role Mới

**Request:**
```bash
curl -X POST "http://localhost:8080/api/admin/roles" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN_ADMIN" \
  -d '{
    "roleName": "ROLE_MODERATOR"
  }'
```

**Expected Response (201):**
```json
{
  "id": 3,
  "roleName": "ROLE_MODERATOR"
}
```

---

### 5.8 Cập Nhật Role

**Request:**
```bash
curl -X PUT "http://localhost:8080/api/admin/roles/3" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN_ADMIN" \
  -d '{
    "roleName": "ROLE_CONTENT_MODERATOR"
  }'
```

**Expected Response:**
```json
{
  "id": 3,
  "roleName": "ROLE_CONTENT_MODERATOR"
}
```

---

### 5.9 Xoá Role

**Request:**
```bash
curl -X DELETE "http://localhost:8080/api/admin/roles/3" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN_ADMIN"
```

**Expected Response (204):** (No Content)

---

## 🧪 Quick Test Checklist

### ✅ Bước 1: Authentication
- [ ] ✉️ Đăng ký email+password
- [ ] 📱 Đăng ký device ID
- [ ] ✉️ Đăng nhập email+password
- [ ] 📱 Đăng nhập device ID

### ✅ Bước 2: Public APIs
- [ ] 📚 Lấy danh sách truyện
- [ ] 🔍 Tìm kiếm truyện
- [ ] 📖 Chi tiết truyện
- [ ] 📕 Danh sách chương
- [ ] 📄 Đọc nội dung chương

### ✅ Bước 3: User Features (Với Token)
- [ ] 💖 Thêm yêu thích
- [ ] 📋 Lấy danh sách yêu thích
- [ ] ❌ Xoá yêu thích
- [ ] ✔️ Kiểm tra đã yêu thích
- [ ] 💾 Lưu tiến trình đọc
- [ ] 📖 Lấy danh sách "Xem tiếp"

### ✅ Bước 4: Admin Features (Với Token Admin)
- [ ] 👥 Lấy danh sách users
- [ ] 👤 Chi tiết user
- [ ] ➕ Tạo user mới
- [ ] ✏️ Cập nhật user
- [ ] 🗑️ Xoá user
- [ ] 🏷️ Lấy danh sách roles
- [ ] ➕ Tạo role mới
- [ ] ✏️ Cập nhật role
- [ ] 🗑️ Xoá role

---

## 🛠️ Công Cụ Khuyến Nghị

### Option 1: Postman
1. Import collection từ Swagger: `http://localhost:8080/v3/api-docs`
2. Tạo variables cho token
3. Test từng endpoint

### Option 2: cURL (Dòng lệnh)
- Copy các lệnh trên vào Terminal

### Option 3: Swagger UI
- Truy cập: `http://localhost:8080/swagger-ui.html`
- Thực hiện test trực tiếp trên browser

### Option 4: Thunder Client (VSCode Extension)
- Lightweight alternative cho Postman

---

## ⚠️ Lỗi Thường Gặp & Xử Lý

| Lỗi | Nguyên Nhân | Giải Pháp |
|-----|-----------|----------|
| `401 Unauthorized` | Token không hợp lệ hoặc hết hạn | Đăng nhập lại, lấy token mới |
| `403 Forbidden` | Không có quyền (không phải admin) | Sử dụng token admin hoặc user có quyền hợp lệ |
| `404 Not Found` | Resource không tồn tại | Kiểm tra ID/slug, đảm bảo resource tồn tại |
| `400 Bad Request` | Request body sai format | Kiểm tra JSON, đảm bảo đúng format |
| `500 Internal Server Error` | Lỗi server | Kiểm tra server logs, database connection |

---

## 📝 Ghi Chú Quan Trọng

1. **Token hết hạn**: JWT token mặc định hết hạn sau 24h
2. **Database**: Đảm bảo database `story_db` đang chạy trên port 3306
3. **CORS**: Frontend cần gửi request từ các domain được permit trong SecurityConfig
4. **Pagination**: Mặc định `page=0, size=20`
5. **Sorting**: Mặc định sort theo `updated`, có thể sort theo `views`, `rating`, `newest`

---

## 🚀 Workflow Hoàn Chỉnh (End-to-End)

```
1. Register new user
   ↓
2. Get list of stories
   ↓
3. Search & get story details
   ↓
4. Get chapters list
   ↓
5. Read chapter content → Auto increment views
   ↓
6. Save reading progress
   ↓
7. Add story to favorites
   ↓
8. Get continue reading list
   ↓
9. Check favorite status
   ↓
✅ Complete Flow!
```
