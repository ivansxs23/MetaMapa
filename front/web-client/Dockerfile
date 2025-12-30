# Etapa de build
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copiamos todo
COPY . .

# Compilamos SIN tests ni compilación de tests
RUN mvn clean package -DskipTests -Dmaven.test.skip=true -B

# Etapa runtime
FROM eclipse-temurin:17-jre

WORKDIR /app

ARG JAR_PATH

COPY --from=build /app/${JAR_PATH} app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]