FROM openjdk:18
WORKDIR /project
COPY target/bolognasbot.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]