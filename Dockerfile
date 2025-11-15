# -------- Build Stage --------
FROM gradle:8.7-jdk17 AS builder
WORKDIR /workspace

# Copie tout le projet
COPY . .

# Construit le JAR Spring Boot
RUN gradle bootJar --no-daemon

# -------- Run Stage --------
FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp

# Installer outils nécessaires
RUN apk add --no-cache netcat-openbsd dos2unix
WORKDIR /app

# Copier le JAR depuis l'étape de build
COPY --from=builder /workspace/build/libs/*.jar app.jar

# Copier et préparer le script wait-for-mysql.sh
COPY wait-for-mysql.sh wait-for-mysql.sh
RUN dos2unix wait-for-mysql.sh
RUN chmod +x wait-for-mysql.sh

EXPOSE 8084

# Entrée : attendre MySQL puis lancer l'app
ENTRYPOINT ["./wait-for-mysql.sh"]
