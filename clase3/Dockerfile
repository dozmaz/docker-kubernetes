# Stage 1: Build
FROM gradle:8.7.0-jdk17-alpine AS build
LABEL stage=build
WORKDIR /app
COPY --chown=gradle:gradle . .
RUN gradle clean build -x test --no-daemon

# Stage 2: Runtime
FROM openjdk:17-alpine AS runtime
LABEL maintainer="guidocutipayujra@gmail.com"
LABEL org.opencontainers.image.source="https://github.com/tu-repo"
LABEL org.opencontainers.image.description="Spring Boot app con build multi-stage y buenas prácticas Docker"

# Crear usuario no root
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

WORKDIR /home/appuser

VOLUME /data/springboot

# Copiar solo el JAR generado
COPY --from=build /app/build/libs/*.jar app.jar

# Puerto configurable
ENV APP_PORT=8080
EXPOSE ${APP_PORT}

# Variables de entorno recomendadas
ENV JAVA_OPTS=""

# Healthcheck (opcional pero recomendado)
HEALTHCHECK --interval=30s --timeout=5s --start-period=30s --retries=3 \
  CMD wget --spider -q http://localhost:${APP_PORT}/actuator/health || exit 1

# Comando de arranque
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar --server.port=$APP_PORT"]