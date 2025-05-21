# Etapa 1: Build con Gradle
FROM gradle:8.7.0-jdk17 AS build
WORKDIR /app

# Copia los archivos necesarios desde la subcarpeta PlanAula
COPY PlanAula/build.gradle .
COPY PlanAula/settings.gradle .
COPY PlanAula/gradlew .
COPY PlanAula/gradle ./gradle
COPY PlanAula/src ./src

RUN ./gradlew clean build -x test

# Etapa 2: Imagen final con Java
FROM openjdk:19-jdk
WORKDIR /app

# Copia el JAR generado desde la etapa de build
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
