# -------- Build Stage --------
FROM gradle:8.7-jdk17 AS builder
WORKDIR /workspace

# Copie seulement les fichiers de build Gradle pour profiter du cache
COPY build.gradle.kts settings.gradle.kts gradle.properties ./
COPY gradle ./gradle

# Télécharge les dépendances
RUN gradle dependencies --no-daemon || true

# Copie le reste du projet
COPY src ./src

# Build le jar
RUN gradle bootJar --no-daemon

# -------- Run Stage --------
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=builder /workspace/build/libs/*.jar app.jar

# Préparer le script MySQL wait
COPY wait-for-mysql.sh wait-for-mysql.sh
RUN apk add --no-cache netcat-openbsd dos2unix \
    && dos2unix wait-for-mysql.sh \
    && chmod +x wait-for-mysql.sh

EXPOSE 8084
ENTRYPOINT ["./wait-for-mysql.sh"]
