FROM openjdk:11
WORKDIR /app
COPY build/libs/oasis-jiwa-ktor-0.0.3-all.jar /app/oasis-jiwa-ktor.jar
CMD ["java", "-jar", "/app/oasis-jiwa-ktor.jar"]
