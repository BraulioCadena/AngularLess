FROM eclipse-temurin:17-jdk-alpine AS build

# Define el directorio de trabajo donde se copiarán los archivos y se ejecutará Maven
WORKDIR /app

# Copia solo el Maven Wrapper (mvnw y .mvn) primero
# Esto asegura que el wrapper esté en /app
COPY mvnw .mvn/ /app/

# Asegura que el script mvnw sea ejecutable
RUN chmod +x mvnw

# Copia el resto de tu código fuente (incluyendo pom.xml)
COPY . /app

# Ahora ejecuta el comando de build usando el mvnw que ya está en /app y es ejecutable
RUN ./mvnw clean package -DskipTests

# Segunda etapa para la imagen final de ejecución
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copia el JAR generado de la etapa de build
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]