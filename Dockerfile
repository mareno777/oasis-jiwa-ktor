FROM openjdk:11
WORKDIR /app
COPY /build/libs/*.jar /app/oasis-jiwa-ktor.jar
CMD ["java", "-jar", "/app/oasis-jiwa-ktor.jar"]
