FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app

# Copia todo el contenido del repositorio al WORKDIR.
# Esto incluye mvnw y la carpeta .mvn
COPY . .

# Asegura que mvnw tenga permisos de ejecución.
# Hacemos esto inmediatamente después de la copia de los archivos.
RUN chmod +x mvnw

# Ejecuta el comando de build
RUN ./mvnw clean package -DskipTests

# Segunda etapa para la imagen final de ejecución
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]