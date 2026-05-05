FROM eclipse-temurin:21-jre-alpine
RUN apk add --no-cache nginx nodejs npm bash

WORKDIR /app
COPY --exclude=*-javadoc.jar --exclude=*-sources.jar pointless-service/target/*.jar backend.jar
COPY pointless-ui/dist ./frontend
COPY nginx.conf /etc/nginx/http.d/default.conf

COPY entrypoint.sh .
RUN chmod +x entrypoint.sh

EXPOSE 80

ENTRYPOINT ["./entrypoint.sh"]