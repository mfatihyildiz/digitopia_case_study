# === Build Stage ===
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# 1️⃣ Maven bağımlılıklarını önceden indir
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

# 5️⃣ Servisin dış dünyaya açılacağı port
EXPOSE 8083

# 6️⃣ JVM optimizasyonları
ENV JAVA_OPTS="-XX:+UseContainerSupport -Xms256m -Xmx512m -Djava.security.egd=file:/dev/./urandom"

# 7️⃣ Spring Cloud sürüm uyumluluk kontrolünü devre dışı bırak (Eureka için gerekli)
ENV SPRING_CLOUD_COMPATIBILITY_VERIFIER_ENABLED=false

# 8️⃣ Uygulamayı başlat
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
