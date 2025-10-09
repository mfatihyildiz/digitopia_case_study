# === Build Stage ===
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# 1️⃣ Bağımlılıkların önbelleğe alınması
COPY pom.xml .
RUN mvn -B dependency:go-offline

# 2️⃣ Kaynak kodları kopyala
COPY src ./src

# 3️⃣ Jar paketini oluştur
RUN mvn clean package -DskipTests

# === Runtime Stage ===
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

# 4️⃣ Build edilmiş jar’ı kopyala
COPY --from=build /app/target/*.jar app.jar

# 5️⃣ API Gateway portu (default: 8080)
EXPOSE 8080

# 6️⃣ Performans ve stabilite ayarları
ENV JAVA_OPTS="-XX:+UseContainerSupport -Xms256m -Xmx512m -Djava.security.egd=file:/dev/./urandom"

# 7️⃣ Discovery / Gateway uyumluluk kontrol hatalarını devre dışı bırak
ENV SPRING_CLOUD_COMPATIBILITY_VERIFIER_ENABLED=false

# 8️⃣ Uygulamayı başlat
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
