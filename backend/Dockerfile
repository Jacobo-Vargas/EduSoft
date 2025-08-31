# 1. Build stage: Java 21 JDK
FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

# Copiar código fuente y dependencias
COPY . .

# Dar permiso de ejecución al wrapper de Maven
RUN chmod +x mvnw

# Construir el backend 
RUN ./mvnw clean package -DskipTests

# 2. Runtime stage: Java 21 JRE ligera
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copiar jar compilado
COPY --from=build /app/target/*.jar app.jar

# Variables de JVM ajustadas a EC2 de 2GB
ENV JAVA_OPTS="-Xms256m -Xmx512m"

# Exponer el puerto HTTPS del backend
EXPOSE 8443

# Ejecutar la app con las opciones de memoria
CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar --spring.profiles.active=prod"]
