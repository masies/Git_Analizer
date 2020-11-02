FROM ubuntu:16.04

RUN apt-get update
RUN apt-get -y install sudo
RUN apt-get -y install git
RUN apt-get -y install curl
RUN apt-get -y install libcurl3
RUN apt-get install -y libarchive13
RUN apt-get -y install openjdk-8-jdk
RUN curl http://131.123.42.38/lmcrs/v1.0.0/srcml_1.0.0-1_ubuntu16.04.deb --output srcml_1.0.0-1_ubuntu16.04.deb
RUN dpkg -i srcml_1.0.0-1_ubuntu16.04.deb

ARG JAR_FILE=./jar_file/group4-software-analytics-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT java -Dspring.data.mongodb.uri=mongodb://db:27017/SoftwareAnalytics -jar /app.jar