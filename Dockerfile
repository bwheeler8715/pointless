FROM eclipse-temurin:21-jre-alpine
RUN apk add --no-cache nginx nodejs npm bash

# Modify the default nginx.conf to remove user and send logs to stdout
RUN sed -i '/^user /d' /etc/nginx/nginx.conf && \
    ln -sf /dev/stdout /var/log/nginx/access.log && \
    ln -sf /dev/stderr /var/log/nginx/error.log

# Copy in all the necessary files
WORKDIR /app
COPY --exclude=*-javadoc.jar --exclude=*-sources.jar pointless-service/target/*.jar backend.jar
COPY pointless-ui/dist ./frontend
COPY nginx.conf /etc/nginx/http.d/default.conf
COPY entrypoint.sh .

# Modify config.json to use a relative path for the API
RUN sed -i 's|"API_URL": ".*"|"API_URL": "/api"|g' ./frontend/config.json
# Make entrypoint.sh executable
RUN chmod +x entrypoint.sh

# Modify file permissions so nginx can run as non-root user
RUN mkdir -p /var/lib/nginx/tmp /var/log/nginx && \
    chgrp -R 0 /var/lib/nginx /var/log/nginx /etc/nginx /run/nginx /app && \
    chmod -R g=u /var/lib/nginx /var/log/nginx /etc/nginx /run/nginx /app

# Set non-root user to run as
USER 1001

EXPOSE 8080

ENTRYPOINT ["./entrypoint.sh"]