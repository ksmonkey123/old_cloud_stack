FROM java:8-jre

RUN apt-get update
RUN apt-get install -y ffmpeg
RUN wget https://yt-dl.org/downloads/latest/youtube-dl -O /bin/youtube-dl

ADD ./target/ytdl-service.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/ytdl-service.jar"]

HEALTHCHECK --interval=10s --timeout=3s CMD curl -f http://localhost:8080/actuator/health || exit 1

EXPOSE 8080
