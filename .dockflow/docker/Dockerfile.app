FROM eclipse-temurin:23-jdk AS builder

WORKDIR /app

COPY . .
RUN chmod +x gradlew

RUN ./gradlew dependencies --no-daemon
RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:23-jdk

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

CMD ["java", "-jar", "app.jar"]
