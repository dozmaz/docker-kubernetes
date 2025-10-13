# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY pom.xml ./
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime (changed to eclipse-temurin)
FROM eclipse-temurin:17-jre-alpine AS runtime
LABEL maintainer="guidocutipayujra@gmail.com"
LABEL org.opencontainers.image.source="https://github.com/tu-repo"
LABEL org.opencontainers.image.description="Spring Boot app con build multi-stage y buenas prácticas Docker"

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

RUN addgroup -g 1001 -S spring && \
    adduser -S spring -u 1001
RUN chown -R spring:spring /app
USER spring

ENV APP_PORT=5000
EXPOSE ${APP_PORT}

ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:+UseG1GC -XX:+DisableExplicitGC"

HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:${APP_PORT}/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
