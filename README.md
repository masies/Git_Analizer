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
    // Run tests
    npm run test:unit	

We use IntelliJ to compile the backend, import the folder `./backend` as a `Maven` project and compile it. The Maven dependencies will be installed automatically.
If everything went well, the project will be available at http://localhost:8080 and the API end point at http://localhost:8080/api/
  
