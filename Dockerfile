FROM openjdk:11-jdk
ARG JAR_FILE=./build/libs/AMD_Project-0.0.1-SNAPSHOT.jar
RUN apt update -y && apt install -y vim && apt install -y curl && apt install -y net-tools
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]