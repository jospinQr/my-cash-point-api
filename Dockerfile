# -------- Build Stage --------
FROM gradle:8.7-jdk17 AS builder
WORKDIR /workspace

# Copier fichiers de configuration Gradle
COPY build.gradle.kts settings.gradle.kts ./
COPY gradle ./gradle

# Télécharge les dépendances (cache Docker)
RUN gradle dependencies --no-daemon || true

# Copier le code source
COPY src ./src

# Build le jar Spring Boot
RUN gradle bootJar --no-daemon

# -------- Run Stage --------
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

COPY --from=builder /workspace/build/libs/*.jar app.jar

COPY wait-for-mysql.sh wait-for-mysql.sh
RUN apk add --no-cache netcat-openbsd dos2unix \
    && dos2unix wait-for-mysql.sh \
    && chmod +x wait-for-mysql.sh

EXPOSE 8084
ENTRYPOINT ["./wait-for-mysql.sh"]
