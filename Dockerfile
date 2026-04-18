# maven build
FROM maven:3.9.6-openjdk-21 AS build
COPY . .
RUN mvn clean package -DskipTests

# java run
FROM eclipse-temurin:21-jre
COPY --from=build /target/*.jar app.jar

# specifics for hugging face deployment
ENV PORT=7860
EXPOSE 7860

# tells which port to use (assigned by hugging face)
ENTRYPOINT ["java", "-Dserver.port=${PORT}", "-jar", "app.jar"]