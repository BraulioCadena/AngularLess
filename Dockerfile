FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app

# Copia solo el script mvnw directamente al directorio de trabajo.
# ELIMINA el comentario al final de la línea.
COPY mvnw .

# Copia toda la carpeta .mvn (incluyendo su contenido y subcarpetas).
# ELIMINA el comentario al final de la línea.
COPY .mvn .mvn/

# Asegura que el script mvnw sea ejecutable
RUN chmod +x mvnw

# Copia el resto de tu código fuente (pom.xml, src, etc.)
# Si tienes un .dockerignore bien configurado, puedes usar:
COPY . .
# O si quieres ser más explícito y no tienes un .dockerignore:
# COPY pom.xml .
# COPY src/ src/
# etc.
# La opción `COPY . .` es la más común y suele funcionar si no tienes archivos indeseados en la raíz.

# Ejecuta el comando de build
RUN ./mvnw clean package -DskipTests

# Segunda etapa para la imagen final de ejecución
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]