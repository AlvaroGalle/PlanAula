# ─── Stage 1: build con Gradle ───────────────────────────────
FROM gradle:7.6-jdk17 AS build

WORKDIR /app

# 1) Copia archivos de configuración de Gradle (caché)
COPY build.gradle settings.gradle gradlew ./
COPY gradle gradle
RUN ./gradlew --version

# 2) Copia el código y compila sin tests
COPY src ./src
RUN ./gradlew build -x test --no-daemon

# ─── Stage 2: runtime ligero ────────────────────────────────
FROM eclipse-temurin:17-jdk-alpine

# (Opcional) Crear usuario no-root
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

WORKDIR /app

# Copia el fat-jar desde el build de Gradle
COPY --from=build /app/build/libs/*.jar app.jar

# Expone el puerto (por defecto 8080)
EXPOSE 8080

# Parámetros JVM opcionales
ENV JAVA_OPTS="-Xms256m -Xmx512m"

# Arranque de la aplicación
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]
