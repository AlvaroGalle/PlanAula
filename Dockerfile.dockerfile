# Etapa 1: Build con Gradle
FROM gradle:8.7-jdk19 AS build
WORKDIR /app
COPY mi-proyecto/build.gradle .
COPY mi-proyecto/settings.gradle .
COPY mi-proyecto/gradlew .
COPY mi-proyecto/gradle mi-proyecto/gradle
COPY mi-proyecto/src mi-proyecto/src

RUN ./gradlew clean build -x test

# Etapa 2: Imagen final con Java
FROM openjdk:19-jdk
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
