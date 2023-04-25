## Name
The K8s-course project

## Description
The aim of project is to handle post created by user. It consists of two microservices: 
- user-service - manage users which are store in Postgres DB
- post-service - manage posts which also are store in Postgres DB
## Run
To start both microservices use docker-compose.yml file. Go to root directory of project and execute the following command in terminal:

    docker-compose -f docker-compose.yml up

