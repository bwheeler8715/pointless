FROM nginx:1.30.0-alpine

# Modify the default nginx.conf to remove user
RUN sed -i '/^user /d' /etc/nginx/nginx.conf

COPY dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Modify file permissions so nginx can run as non-root user
RUN chgrp -R 0 /var/cache/nginx /etc/nginx /var/run /usr/share/nginx/html && \
    chmod -R g=u /var/cache/nginx /etc/nginx /var/run /usr/share/nginx/html

# Set non-root user to run as
USER 1001

EXPOSE 8080

CMD ["nginx", "-g", "daemon off;"]