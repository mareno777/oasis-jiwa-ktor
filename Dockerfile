FROM openjdk:11
COPY /build/libs/oasis-jiwa-ktor-0.0.2-all.jar /app/oasis-jiwa-ktor.jar
COPY /build/libs/keystore.jks /app/keystore.jks
CMD ["java", "-jar", "/app/oasis-jiwa-ktor.jar"]
