FROM openjdk:11

ARG JAR_FILE=build/libs/

COPY ${JAR_FILE}/*.jar /app/

ENTRYPOINT ["java","-jar","/app/app.jar"]
