#!/bin/bash
# Start the Java backend in the background
java -jar /app/backend.jar &

# Start NGINX in the foreground to keep the container running
nginx -g 'daemon off;'