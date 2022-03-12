FROM maven:3.5-jdk-11 as BUILD

WORKDIR /app
COPY src src
COPY pom.xml pom.xml
RUN mvn clean package

FROM openjdk:11

COPY --from=BUILD /app/target/*.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]