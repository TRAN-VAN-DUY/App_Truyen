# Truyen Admin API

Backend API cho hệ thống quản lý người dùng 

## Công nghệ sử dụng

* Java 21
* Spring Boot
* Spring Data JPA
* Maven
* Swagger (OpenAPI)

---

# Hướng dẫn chạy project

## 1. Clone project

```bash
git clone <repository-url>
cd truyen-admin-api
```

---

## 2. Mở project bằng IntelliJ IDEA

* Mở IntelliJ
* Chọn **Open**
* Chọn thư mục project

Sau khi mở, IntelliJ sẽ tự tải dependencies Maven.

---

## 3. Chạy ứng dụng

Tìm file:

```
src/main/java/com/truyenmb/TruyenMbApplication.java
```

Run file này bằng cách:

* Click chuột phải
* Chọn **Run 'TruyenMbApplication'**

Hoặc chạy bằng Maven:

```bash
mvn spring-boot:run
```

---

## 4. Truy cập Swagger UI

Sau khi ứng dụng chạy thành công, mở trình duyệt và truy cập:

```
http://localhost:8080/swagger-ui/index.html
```

Swagger UI cho phép test các API trực tiếp.

---

## 5. Test API Role

Ví dụ endpoint tạo role:

```
POST /api/admin/roles
```

Có thể test trực tiếp tại:

```
http://localhost:8080/swagger-ui/index.html#/role-controller/create_2
```

Tại Swagger UI:

1. Chọn **Role Controller**
2. Chọn API **create**
3. Nhấn **Try it out**
4. Nhập dữ liệu JSON
5. Nhấn **Execute**

---

# Cấu trúc project

```
com.truyenmb
 ├── controller
 │   ├── UserController
 │   └── RoleController
 │
 ├── entity
 │   ├── User
 │   └── Role
 │
 ├── repository
 │   ├── UserRepository
 │   └── RoleRepository
 │
 └── TruyenMbApplication
```

---

