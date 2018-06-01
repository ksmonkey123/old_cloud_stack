FROM java:8-jre

ADD ./target/auth-service.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/auth-service.jar"]

HEALTHCHECK --interval=10s --timeout=3s CMD curl -f http://localhost:9191/actuator/health || exit 1

EXPOSE 9191
