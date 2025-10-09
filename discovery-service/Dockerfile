# === Build stage ===
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Bağımlılıkların önbelleğe alınması için önce pom.xml kopyalanır
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Kaynak kodlar kopyalanır
COPY src ./src

# Uygulama paketlenir (testler atlanır)
RUN mvn clean package -DskipTests

# === Runtime stage ===
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

# Jar dosyasını build aşamasından al
COPY --from=build /app/target/*.jar app.jar

# Port 8761'i expose et
EXPOSE 8761

# Healthcheck desteği için environment ayarları
ENV JAVA_OPTS="-Xms256m -Xmx512m -Djava.security.egd=file:/dev/./urandom"

# Spring Cloud sürümleri ile uyum için compatibility verifier'ı devre dışı bırak
ENV SPRING_CLOUD_COMPATIBILITY_VERIFIER_ENABLED=false

# Container entrypoint
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
