FROM openjdk:11-jdk-slim
LABEL maintainer="makgwalet@gmail.com"
VOLUME /tmp
EXPOSE 8080
RUN mkdir -p /app/
ADD target/student-manager-0.0.1-SNAPSHOT.jar /app/app.jar
#COPY target/student-manager-0.0.1-SNAPSHOT.jar /student-manager-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=dev", "/app/app.jar"]
