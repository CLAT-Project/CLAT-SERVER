FROM openjdk:17-jdk-alpine
VOLUME /tmp
COPY build/libs/*.jar clat-server.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/clat-server.jar"]