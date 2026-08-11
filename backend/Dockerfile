# ---------- Etapa 1: build com Maven ----------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
# Baixa as dependencias primeiro para aproveitar o cache de camadas do Docker
RUN mvn -B dependency:go-offline
COPY src ./src
RUN mvn -B clean package -DskipTests

# ---------- Etapa 2: runtime com Tomcat 11 ----------
FROM tomcat:11.0-jdk21-temurin
# Remove a webapp de exemplo do Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=build /app/target/agrobom-backend.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]
