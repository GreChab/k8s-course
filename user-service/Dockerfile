FROM eclipse-temurin:18-alpine
WORKDIR /app
COPY ./build/libs/*SNAPSHOT.jar app.jar
CMD ["java", "-jar", "/app/app.jar"]