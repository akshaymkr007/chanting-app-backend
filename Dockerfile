# Stage 1: Build the Spring Boot application using Maven and Temurin JDK 21
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /build

# Copy Maven wrapper and POM first for dependency layer caching
COPY pom.xml ./
# Download dependencies offline to cache layers
RUN apk add --no-cache maven && mvn dependency:go-offline -B

# Copy application source and build production executable JAR
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Minimal hardened production JRE runtime
FROM eclipse-temurin:21-jre-alpine AS runner

WORKDIR /app

# Install curl for container health check
RUN apk add --no-cache curl

# Create non-root system user and group for security
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy built JAR from builder stage
COPY --from=builder --chown=spring:spring /build/target/japa-backend-*.jar app.jar

# JVM ergonomics optimized for container memory constraints
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"
ENV SPRING_PROFILES_ACTIVE="prod"
ENV PORT=8080

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s --start-period=30s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
