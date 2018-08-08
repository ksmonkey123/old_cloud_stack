FROM java:8-jre

ADD ./target/netcode-service.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/netcode-service.jar"]

HEALTHCHECK --interval=10s --timeout=3s CMD curl -f http://localhost:8080/actuator/health || exit 1

EXPOSE 8080
