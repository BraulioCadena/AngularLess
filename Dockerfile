# 📦 Etapa de construcción
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build

WORKDIR /app
COPY . .

RUN mvn clean package -DskipTests

# 🚀 Etapa final (runtime)
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app
COPY --from=build /app/target/LeslyProject-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
