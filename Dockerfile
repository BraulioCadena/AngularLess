FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app

# Paso 1: Copiar solo los scripts del Maven Wrapper
# Esto asegura que mvnw y la carpeta .mvn estén disponibles y sean manejados individualmente.
COPY mvnw .mvn/ /app/

# Paso 2: ASEGURAR PERMISOS DE EJECUCIÓN INMEDIATAMENTE DESPUÉS DE LA COPIA
# Aplica permisos de ejecución al script mvnw directamente.
# Esta es la línea CRÍTICA para tu problema.
RUN chmod +x mvnw

# Paso 3: Copiar el resto del código de la aplicación (excluyendo lo que ya copiamos)
# Excluimos .mvn/ y mvnw porque ya los copiamos arriba y establecimos sus permisos.
# Nota: Si tu .dockerignore no está configurado para ignorar estas copias redundantes,
# el `COPY . .` original ya las incluía. Esta forma es más precisa.
# Una forma de copiar todo lo demás (si no tienes .dockerignore complejo):
COPY pom.xml /app/
COPY src/ /app/src/
# Si tienes otras carpetas en la raíz (ej. 'config', 'data'), añádelas aquí:
# COPY <otra_carpeta>/ /app/<otra_carpeta>/

# O, si no quieres listar todas las carpetas, y confías en tu .dockerignore,
# puedes mantener un `COPY . .` aquí, pero el `chmod +x` debe estar antes
# de la ejecución del `mvnw`.
# La clave es que el chmod +x sea *después* de la copia del mvnw.

# Ejecuta el comando de build
RUN ./mvnw clean package -DskipTests

# Segunda etapa para la imagen final de ejecución
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]