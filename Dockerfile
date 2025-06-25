FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app

# Copia solo el script mvnw directamente al directorio de trabajo
COPY mvnw . /app/  # Copia mvnw a /app/

# Copia toda la carpeta .mvn (incluyendo su contenido y subcarpetas)
# Esto asegura que .mvn/wrapper/maven-wrapper.properties esté en /app/.mvn/wrapper/
COPY .mvn /app/.mvn/

# Asegura que el script mvnw sea ejecutable
RUN chmod +x mvnw

# Copia el resto de tu código fuente (pom.xml, src, etc.)
# Si ya tienes una estrategia de .dockerignore, puedes mantener `COPY . .` aquí.
# Si no, sé explícito:
COPY pom.xml /app/
COPY src/ /app/src/
# Y cualquier otra carpeta raíz de tu proyecto, como 'config/', 'static/', 'templates/' etc.

# Ahora ejecuta el comando de build
RUN ./mvnw clean package -DskipTests

# Segunda etapa para la imagen final de ejecución
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]