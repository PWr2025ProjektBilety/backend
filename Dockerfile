FROM maven:3.9.11-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn -B -DskipTests clean package


FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar
COPY load-secrets.sh /app/load-secrets.sh

RUN chmod +x /app/load-secrets.sh

EXPOSE 8080

ENTRYPOINT ["/app/load-secrets.sh"]