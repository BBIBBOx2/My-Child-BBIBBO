FROM openjdk:11
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app-test.jar
ENTRYPOINT ["java","-jar","/app-test.jar", "0.0.0.0:8080"]
