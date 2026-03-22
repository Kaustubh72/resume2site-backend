# Build stage
FROM maven:3.9.9-amazoncorretto-21 AS build
WORKDIR /workspace

COPY pom.xml ./
COPY src ./src

RUN mvn -B clean package -DskipTests -DskipITs

# Run stage
FROM amazoncorretto:21
WORKDIR /app

COPY --from=build /workspace/target/*.jar app.jar

EXPOSE 8081

ENV SERVER_PORT=8081
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
