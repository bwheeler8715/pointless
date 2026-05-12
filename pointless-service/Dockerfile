FROM eclipse-temurin:21-jre-alpine

WORKDIR /app
COPY --exclude=*-javadoc.jar --exclude=*-sources.jar target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]