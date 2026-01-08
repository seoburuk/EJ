# Multi-stage build for Java Spring application
FROM maven:3.9-eclipse-temurin-8 AS builder

WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM tomcat:8.5-jre8

# Remove default Tomcat applications
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy WAR file from builder stage
COPY --from=builder /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

# Expose port
EXPOSE 8080

# Run Tomcat
CMD ["catalina.sh", "run"]
