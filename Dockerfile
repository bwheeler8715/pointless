FROM eclipse-temurin:21-jre-alpine
RUN apk add --no-cache nginx nodejs npm bash

WORKDIR /app
COPY --exclude=*-javadoc.jar --exclude=*-sources.jar pointless-service/target/*.jar backend.jar
COPY pointless-ui/dist ./frontend
COPY nginx.conf /etc/nginx/http.d/default.conf

# Modify config.json to use a relative path for the API
RUN sed -i 's|"API_URL": ".*"|"API_URL": "/api"|g' ./frontend/config.json

COPY entrypoint.sh .
RUN chmod +x entrypoint.sh

EXPOSE 8080

ENTRYPOINT ["./entrypoint.sh"]