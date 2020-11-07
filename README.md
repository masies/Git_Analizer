# Software Analytics Atelier - Group 4

### Requirements
The following tools are needed to compile the project

 - Node and Npm
 - Java 8
 - MongoDB
 
### Installation
The installation is dived in two parts, one for the front-end made with VueJS and the other for the back-end made with Spring Boot.

First clone the repository

    git clone git@lab.si.usi.ch:software-analytics-2020/group-4.git  
    // or with HTTPS
    git clone https://lab.si.usi.ch/software-analytics-2020/group-4.git
Then install the required node packages

    cd frontend
    npm i

To build the front-end we have various commands. The output build will be in `.\backend\src\main\resources\public`

    // Build the project
    npm run build 
    // Build the project and rebuild when there are changes
    npm run watch 	

We use IntelliJ to compile the backend, import the folder `./backend` as a `Maven` project and compile it. The Maven dependencies will be installed automatically.
If everything went well, the project will be available at http://localhost:8080 and the API end point at http://localhost:8080/api/


### Commands

Build the backend with maven

    // Run tests and build
    mvn clean package
    // Skip tests and build
    mvn -Dmaven.test.skip clean package 
  
Run only tests with maven

    mvn test

Run tests and upload changes to Sonar

    mvn --batch-mode verify sonar:sonar
        -Dsonar.host.url="http://rio.inf.usi.ch:9000/"
        -Dsonar.login="group4"
        -Dsonar.password="group4"
        -Dsonar.analysis.mode=publish
        -Dsonar.gitlab.commit_sha=$CI_COMMIT_SHA
        -Dsonar.gitlab.ref_name=$CI_COMMIT_REF_NAME
        -Dsonar.gitlab.project_id=$CI_PROJECT_ID


A Docker container is available, it will copy the jar file from ./backend/jar_file and execute it in Docker.
    
    // First build frontend
    // Then build backend
    cd backend
    cp target/group4-software-analytics-0.0.1-SNAPSHOT.jar jar_file
    docker-compose up --build


