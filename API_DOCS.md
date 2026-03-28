# API Documentation — App Truyện

**Base URL:** `http://localhost:8080`  
**Swagger UI:** `http://localhost:8080/swagger-ui.html Swagger UI:** `http://localhost:8080/swagger-ui.html  (Nên sử dụng swagger để test API)`  
**Ghi chú:** Token = `Bearer <jwt>` trong header `Authorization`

---

## Mục lục
1. [Auth](#1-auth)
2. [Đọc truyện (Public)](#2-đọc-truyện-public)
3. [Yêu thích (JWT)](#3-yêu-thích-jwt)
4. [Lịch sử đọc (JWT)](#4-lịch-sử-đọc-jwt)
5. [Admin — Users (JWT + ROLE_ADMIN)](#5-admin--users)
6. [Admin — Roles (JWT + ROLE_ADMIN)](#6-admin--roles)

---

## 1. Auth

### POST /api/auth/register
Đăng ký tài khoản mới.

**Body (email):**
```json
{ "username": "nguyen", "email": "nguyen@example.com", "password": "123456" }
```
**Body (guest/device):**
```json
{ "deviceId": "device-abc-001" }
```
**Response:**
```json
{ "token": "eyJ...", "userId": 1, "username": "nguyen" }
```

**PowerShell test:**
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/auth/register" `
  -Method POST -ContentType "application/json" `
  -Body '{"username":"nguyen","email":"nguyen@test.com","password":"123456"}'
```

---

### POST /api/auth/login
Đăng nhập.

**Body (email):**
```json
{ "email": "nguyen@example.com", "password": "123456" }
```
**Body (device):**
```json
{ "deviceId": "device-abc-001" }
```

**PowerShell test:**
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" `
  -Method POST -ContentType "application/json" `
  -Body '{"email":"admin@test.com","password":"123456"}'
```

> 💡 Copy `token` từ response để dùng cho các API cần JWT bên dưới.

---

## 2. Đọc truyện (Public)

> Không cần token.

### GET /api/v1/stories — Danh sách truyện

| Param | Mặc định | Mô tả |
|-------|---------|-------|
| `page` | 0 | Trang (0-indexed) |
| `size` | 20 | Số truyện/trang |
| `genre` | — | Slug thể loại (vd: `hanh-dong`) |
| `status` | — | `ongoing` / `completed` / `hiatus` |
| `sort` | `updated` | `updated` / `views` / `rating` / `newest` |

```powershell
Invoke-RestMethod "http://localhost:8080/api/v1/stories?page=0&size=10&sort=views"
```

---

### GET /api/v1/stories/search?keyword= — Tìm kiếm

```powershell
Invoke-RestMethod "http://localhost:8080/api/v1/stories/search?keyword=doraemon"
```

---

### GET /api/v1/stories/{slug} — Chi tiết truyện theo slug

```powershell
Invoke-RestMethod "http://localhost:8080/api/v1/stories/ten-truyen-abc"
```

---

### GET /api/v1/stories/id/{id} — Chi tiết truyện theo ID

```powershell
Invoke-RestMethod "http://localhost:8080/api/v1/stories/id/1"
```

---

### GET /api/v1/stories/{slug}/chapters — Danh sách chương theo slug

```powershell
Invoke-RestMethod "http://localhost:8080/api/v1/stories/ten-truyen-abc/chapters"
```

---

### GET /api/v1/stories/id/{id}/chapters — Danh sách chương theo story ID

```powershell
Invoke-RestMethod "http://localhost:8080/api/v1/stories/id/1/chapters"
```

---

### GET /api/v1/stories/{slug}/chapters/{number} — Đọc chương

Tự động tăng lượt xem. Response có `prevChapterNumber` / `nextChapterNumber`.

```powershell
Invoke-RestMethod "http://localhost:8080/api/v1/stories/ten-truyen-abc/chapters/1"
```

---

### GET /api/v1/stories/chapters/{chapterId} — Đọc chương theo chapter ID

```powershell
Invoke-RestMethod "http://localhost:8080/api/v1/stories/chapters/42"
```

---

## 3. Yêu thích (JWT)

> Cần header: `Authorization: Bearer <token>`

```powershell
$token = "eyJ..."   # paste token vào đây
$headers = @{ Authorization = "Bearer $token" }
```

### GET /api/v1/favorites — Danh sách yêu thích

```powershell
Invoke-RestMethod "http://localhost:8080/api/v1/favorites" -Headers $headers
```

---

### POST /api/v1/favorites — Thêm yêu thích

```powershell
Invoke-RestMethod "http://localhost:8080/api/v1/favorites" `
  -Method POST -ContentType "application/json" `
  -Headers $headers -Body '{"storyId": 1}'
```

---

### DELETE /api/v1/favorites/{storyId} — Xoá yêu thích

```powershell
Invoke-RestMethod "http://localhost:8080/api/v1/favorites/1" `
  -Method DELETE -Headers $headers
```

---

### GET /api/v1/favorites/{storyId}/check — Kiểm tra đã yêu thích chưa

```powershell
Invoke-RestMethod "http://localhost:8080/api/v1/favorites/1/check" -Headers $headers
```

---

## 4. Lịch sử đọc (JWT)

### POST /api/v1/reading-history — Lưu tiến trình đọc

Gọi mỗi lần user đọc/cuộn. **Upsert** — gọi lại cùng `storyId` sẽ cập nhật chứ không tạo mới.

| Field | Kiểu | Mô tả |
|-------|------|-------|
| `storyId` | Integer | ID truyện đang đọc |
| `lastChapterId` | Integer | ID chương đang đọc |
| `scrollPosition` | Integer (0–100) | % đã cuộn trong chương |

```powershell
Invoke-RestMethod "http://localhost:8080/api/v1/reading-history" `
  -Method POST -ContentType "application/json" -Headers $headers `
  -Body '{"storyId":1,"lastChapterId":5,"scrollPosition":75}'
```

---

### GET /api/v1/reading-history — Xem tiếp (đang đọc dở)

Trả về danh sách truyện đang đọc, sắp xếp theo lần đọc gần nhất.

```powershell
Invoke-RestMethod "http://localhost:8080/api/v1/reading-history" -Headers $headers
```

---

### GET /api/v1/reading-history/{storyId} — Tiến trình của 1 truyện

```powershell
Invoke-RestMethod "http://localhost:8080/api/v1/reading-history/1" -Headers $headers
```

---

## 5. Admin — Users

> Cần `ROLE_ADMIN`. Dùng token của account admin.

```powershell
$adminHeaders = @{ Authorization = "Bearer <admin_token>" }
```

### GET /api/admin/users — Danh sách tất cả users

```powershell
Invoke-RestMethod "http://localhost:8080/api/admin/users" -Headers $adminHeaders
```

---

### GET /api/admin/users/{id} — Chi tiết user

```powershell
Invoke-RestMethod "http://localhost:8080/api/admin/users/1" -Headers $adminHeaders
```

---

### POST /api/admin/users — Tạo user mới

```powershell
Invoke-RestMethod "http://localhost:8080/api/admin/users" `
  -Method POST -ContentType "application/json" -Headers $adminHeaders `
  -Body '{"username":"testuser","email":"test@example.com","password":"pass123","role":{"id":2}}'
```

---

### PUT /api/admin/users/{id} — Cập nhật user

```powershell
Invoke-RestMethod "http://localhost:8080/api/admin/users/2" `
  -Method PUT -ContentType "application/json" -Headers $adminHeaders `
  -Body '{"username":"newname","email":"new@example.com","role":{"id":1}}'
```

---

### DELETE /api/admin/users/{id} — Xoá user

```powershell
Invoke-RestMethod "http://localhost:8080/api/admin/users/2" `
  -Method DELETE -Headers $adminHeaders
```

---

## 6. Admin — Roles

### GET /api/admin/roles — Danh sách roles

```powershell
Invoke-RestMethod "http://localhost:8080/api/admin/roles" -Headers $adminHeaders
```

---

### POST /api/admin/roles — Tạo role mới

```powershell
Invoke-RestMethod "http://localhost:8080/api/admin/roles" `
  -Method POST -ContentType "application/json" -Headers $adminHeaders `
  -Body '{"roleName":"ROLE_MOD"}'
```

---

## HTTP Status Codes

| Code | Ý nghĩa |
|------|---------|
| `200` | Thành công |
| `201` | Tạo mới thành công |
| `204` | Xoá thành công (no content) |
| `400` | Request sai (thiếu field hoặc validation lỗi) |
| `401` | Thiếu / hết hạn JWT |
| `403` | Không có quyền (sai role) |
| `404` | Không tìm thấy |
| `409` | Conflict (vd: đã yêu thích rồi, email đã tồn tại) |
| `500` | Lỗi server |

---

## Response format

```json
{
  "success": true,
  "message": "Success",
  "data": { ... }
}
```

> **Lưu ý:** Admin endpoints (`/api/admin/**`) trả về entity trực tiếp, không bọc trong `ApiResponse`.
