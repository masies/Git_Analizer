FROM xqdocker/ubuntu-openjdk-1.8


RUN sudo apt-get update
RUN update-ca-certificates -f
RUN sudo apt-get -y install git
RUN sudo apt-get -y install curl
RUN sudo apt-get install -y libarchive13
RUN curl http://131.123.42.38/lmcrs/v1.0.0/srcml_1.0.0-1_ubuntu14.04.deb --output srcml_1.0.0-1_ubuntu14.04.deb
RUN sudo dpkg -i srcml_1.0.0-1_ubuntu14.04.deb

ARG JAR_FILE=./jar_file/group4-software-analytics-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT sudo java -Dspring.data.mongodb.uri=mongodb://db:27017/SoftwareAnalytics -jar /app.jar
