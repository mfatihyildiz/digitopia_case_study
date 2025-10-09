# === Build Stage ===
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# 1️⃣ Maven bağımlılıklarını indir
COPY pom.xml .
RUN mvn -B dependency:go-offline

# 2️⃣ Kaynak kodlarını kopyala
COPY src ./src

# 3️⃣ Jar dosyasını oluştur
RUN mvn clean package -DskipTests

# === Runtime Stage ===
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

# 4️⃣ Build edilmiş jar dosyasını kopyala
COPY --from=build /app/target/*.jar app.jar

# 5️⃣ Portu dışa aç
EXPOSE 8084

# 6️⃣ JVM ayarları + Cloud uyumluluk
ENV JAVA_OPTS="-XX:+UseContainerSupport -Xms256m -Xmx512m -Djava.security.egd=file:/dev/./urandom"
ENV SPRING_CLOUD_COMPATIBILITY_VERIFIER_ENABLED=false

# 7️⃣ Servisin kendi base-url tanımı (opsiyonel, istersen docker-compose'da da çevre değişkeni olarak geçebilirsin)
ENV SERVICES_ORGANIZATION_BASE_URL=http://organization-service:8083

# 8️⃣ Başlatma komutu
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
