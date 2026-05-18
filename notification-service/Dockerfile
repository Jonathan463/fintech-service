FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app


COPY common-lib ./common-lib
WORKDIR /app/common-lib
RUN mvn clean install -DskipTests

COPY security-lib ./security-lib
WORKDIR /app/security-lib
RUN mvn clean install -DskipTests

WORKDIR /app/service
COPY notification-service/pom.xml .
COPY notification-service/src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S appgroup && adduser -S appuser -G appgroup

COPY --from=build /app/service/target/*.jar app.jar

RUN chown -R appuser:appgroup /app
USER appuser

EXPOSE 8086

HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD wget --quiet --tries=1 --spider http://localhost:8086/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]