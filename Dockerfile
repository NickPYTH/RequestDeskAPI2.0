# Этап 1: Сборка с официальным образом Maven
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app
COPY pom.xml .
COPY src ./src

# Кэшируем зависимости и собираем проект
RUN mvn dependency:go-offline -B
RUN mvn clean package -DskipTests

# Этап 2: Финальный образ с JRE
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]