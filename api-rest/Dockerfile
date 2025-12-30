# Etapa de build
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests -Dmaven.test.skip=true -B

# Etapa runtime
FROM eclipse-temurin:17-jre

WORKDIR /app

ARG JAR_PATH

# Usamos wildcard para que copie el JAR aunque el nombre exacto varíe un poco
COPY --from=build /app/${JAR_PATH}*/**.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]