FROM openjdk:17
WORKDIR /app
ADD  target/assignment.jar ./assignment.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "assignment.jar"]