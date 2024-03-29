# Book Library
The service for tracking borrowed books and book readers (borrowers).

## Book Library REST API endpoints
1. Show all books in the library
GET http://localhost:8080/book-library/api/v1/books

2. Show all readers registered in the library
GET http://localhost:8080/book-library/api/v1/readers

3. Register new reader
POST http://localhost:8080/book-library/api/v1/readers {"name":"Some Reader"}

4. Add new book
POST http://localhost:8080/book-library/api/v1/books {"author":"Some Author", "title":"Some Book Title"}

5. Borrow a book to a reader
POST http://localhost:8080/book-library/api/v1/books/{bookId}/readers/{readerId}

6. Return a book to a library
DELETE http://localhost:8080/book-library/api/v1/books/{bookId}/readers

7. Show all books borrowed by reader
GET http://localhost:8080/book-library/api/v1/readers/{readerId}/books

8. Show current reader of a book
GET http://localhost:8080/book-library/api/v1/books/{bookId}/readers

## Swagger
Show Swagger-UI page
http://localhost:8080/book-library
which is redirecting to
http://localhost:8080/book-library/swagger-ui/index.html#/

## Actuator
Check the health status of the application
http://localhost:8080/book-library/actuator/health

Get information about the application
http://localhost:8080/book-library/actuator/info

## Java Melody
Statistics of JavaMelody monitoring page
http://localhost:8080/book-library/monitoring

## Create JAR archive in target directory
Execute comand in terminal  
`mvn clean package`

## Build Docker Image on your local machine
Docker Desktop must be installed on your local machine  
go in terminal to project directory, run command  
`docker build -t djmolot/book-library .`  
if you haven't already downloaded the postres image from DockerHub,  
here's how you can do it: run command in terminal  
`docker pull postgres`

## Push created app image to DockerHub
run command in terminal  
`docker push djmolot/book-library`

## Add file .env in project root directory
Add file .env in project root directory and set in that file following variables  
`POSTGRES_USER=`  
`POSTGRES_PASSWORD=`  
`POSTGRES_DB=`  
`POSTGRES_LOCAL_PORT=`  
`POSTGRES_DOCKER_PORT=`  
  
`SPRING_LOCAL_PORT=`  
`SPRING_DOCKER_PORT=`  
`DEBUG_PORT=`  
these variables will be used to set values in file docker-compose.yml

## Run an application consisting of two linked containers
run command in terminal  
`docker compose up -d`  
this command will run application with two containers in detached mode
