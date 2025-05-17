# Etapa 1: Build con Gradle
FROM gradle:8.7-jdk19 AS build
WORKDIR /app
COPY build.gradle .
COPY settings.gradle .
COPY gradlew .
COPY gradle gradle
COPY src src

# Construye el JAR (ajusta el nombre de la tarea si tu proyecto lo requiere)
RUN ./gradlew clean build -x test

# Etapa 2: Imagen final con solo el JDK y el JAR
FROM openjdk:19-jdk
WORKDIR /app
VOLUME /tmp

# Copia el JAR generado desde la etapa de build
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]