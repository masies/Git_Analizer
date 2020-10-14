FROM openjdk:13-jdk-alpine


RUN ["apk", "add","git"]
RUN ["apk", "add","curl"]
ARG JAR_FILE=./target/group4-software-analytics-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Dspring.data.mongodb.uri=mongodb://db:27017/SoftwareAnalytics", "-jar","/app.jar"]