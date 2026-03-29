# Build stage
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy file jar đã build từ stage trước
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Chạy ứng dụng, dùng biến môi trường để config (không sửa file yml)
ENTRYPOINT ["java", "-jar", "app.jar"]