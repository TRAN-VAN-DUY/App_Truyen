# Story API — Hướng dẫn test

**Base URL:** `http://localhost:8080`  
**Swagger UI:** `http://localhost:8080/swagger-ui.html`

---

## Khởi động

```bash
cd story-api
mvn spring-boot:run
```

> Sau khi thấy `Started StoryApiApplication` là đã sẵn sàng.

---

## 1. Lấy danh sách truyện *(Public)*

```
GET /api/v1/stories?page=0&size=10
```

**curl:**
```bash
curl "http://localhost:8080/api/v1/stories?page=0&size=10"
```

**Swagger:** Để trống `genre`, `status`. Bấm **Execute**.

> 📝 Copy `slug` từ kết quả để dùng cho các API bên dưới.

---

## 2. Tìm kiếm truyện *(Public)*

```
GET /api/v1/stories/search?keyword=doraemon
```

```bash
curl "http://localhost:8080/api/v1/stories/search?keyword=doraemon"
```

---

## 3. Chi tiết truyện *(Public)*

```
GET /api/v1/stories/{slug}
```

```bash
curl "http://localhost:8080/api/v1/stories/ten-truyen-o-day"
```

> Thay `ten-truyen-o-day` bằng `slug` lấy từ bước 1.

---

## 4. Danh sách chương *(Public)*

```
GET /api/v1/stories/{slug}/chapters
```

```bash
curl "http://localhost:8080/api/v1/stories/ten-truyen-o-day/chapters"
```

> Xem có những `chapterNumber` nào trước khi đọc nội dung.

---

## 5. Đọc nội dung chương *(Public)*

```
GET /api/v1/stories/{slug}/chapters/{chapterNumber}
```

```bash
curl "http://localhost:8080/api/v1/stories/ten-truyen-o-day/chapters/1"
```

Response trả về: nội dung chương + `prevChapterNumber` / `nextChapterNumber` để navigate.

---

## 6. Lấy JWT để test API secured

Vì auth do team khác làm, dùng **jwt.io** để tạo token test:

1. Vào [https://jwt.io](https://jwt.io)
2. Chọn Algorithm: **HS256**
3. **Payload:**
```json
{
  "sub": "1",
  "exp": 9999999999
}
```
4. **Verify Signature → secret:** điền đúng chuỗi trong `application.yml` → `app.jwt.secret`
5. Copy token ở ô **Encoded** bên trái.

---

## 7. Danh sách yêu thích *(Cần JWT)*

```
GET /api/v1/favorites
Authorization: Bearer <token>
```

```bash
curl -H "Authorization: Bearer <token>" \
     "http://localhost:8080/api/v1/favorites"
```

**Swagger:** Bấm **Authorize** (🔒) → điền token → Execute.

---

## 8. Thêm yêu thích *(Cần JWT)*

```
POST /api/v1/favorites
Authorization: Bearer <token>
Content-Type: application/json

{ "storyId": 1 }
```

```bash
curl -X POST \
     -H "Authorization: Bearer <token>" \
     -H "Content-Type: application/json" \
     -d '{"storyId": 1}' \
     "http://localhost:8080/api/v1/favorites"
```

---

## 9. Kiểm tra đã yêu thích chưa *(Cần JWT)*

```
GET /api/v1/favorites/{storyId}/check
```

```bash
curl -H "Authorization: Bearer <token>" \
     "http://localhost:8080/api/v1/favorites/1/check"
```

---

## 10. Xoá yêu thích *(Cần JWT)*

```
DELETE /api/v1/favorites/{storyId}
```

```bash
curl -X DELETE \
     -H "Authorization: Bearer <token>" \
     "http://localhost:8080/api/v1/favorites/1"
```

---

## 11. Lưu tiến trình đọc *(Cần JWT)*

Gọi sau mỗi lần user đọc xong / cuộn qua một đoạn.

```
POST /api/v1/reading-history
Authorization: Bearer <token>
Content-Type: application/json

{
  "storyId": 1,
  "lastChapterId": 5,
  "scrollPosition": 75
}
```

```bash
curl -X POST \
     -H "Authorization: Bearer <token>" \
     -H "Content-Type: application/json" \
     -d '{"storyId":1,"lastChapterId":5,"scrollPosition":75}' \
     "http://localhost:8080/api/v1/reading-history"
```

> `scrollPosition`: 0–100 (% đã đọc trong chương hiện tại).  
> Gọi lại với cùng `storyId` → **tự động cập nhật** (upsert).

---

## 12. Xem tiếp — danh sách đang đọc dở *(Cần JWT)*

```
GET /api/v1/reading-history
Authorization: Bearer <token>
```

```bash
curl -H "Authorization: Bearer <token>" \
     "http://localhost:8080/api/v1/reading-history"
```

Response trả về: story + chapter đang đọc + scrollPosition, **sắp xếp theo đọc gần nhất**.

---

## 13. Tiến trình của 1 truyện cụ thể *(Cần JWT)*

```
GET /api/v1/reading-history/{storyId}
```

```bash
curl -H "Authorization: Bearer <token>" \
     "http://localhost:8080/api/v1/reading-history/1"
```

---

## Response format chung

```json
{
  "success": true,
  "message": "Success",
  "data": { ... }
}
```

| `success` | Ý nghĩa |
|-----------|---------|
| `true` | Thành công |
| `false` | Lỗi — xem `message` để biết lý do |

### HTTP Status codes

| Code | Ý nghĩa |
|------|---------|
| `200` | OK |
| `201` | Tạo mới thành công |
| `400` | Sai request body |
| `401` | Thiếu / sai JWT |
| `404` | Không tìm thấy |
| `409` | Duplicate (đã yêu thích rồi) |
| `500` | Lỗi server (thường do DB) |
