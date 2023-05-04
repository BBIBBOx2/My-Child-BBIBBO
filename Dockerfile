FROM openjdk:11

ARG JAR_FILE=build/libs/

COPY ${JAR_FILE}* /app/app.jar

ENTRYPOINT ["java","-jar","/app/app.jar"]
