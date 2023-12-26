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
mvn clean package

## Build Docker Image on your local machine
Docker Desktop must be installed on your local machine  
go in terminal to project directory, run command  
docker build . -t book-library:1.0.0  
if you haven't already downloaded the postres image from DockerHub,  
here's how you can do it: run command in terminal  
docker pull postgres

## Create a PostgreSQL container
run command in terminal  
docker run --name postgres16 -e POSTGRES_PASSWORD=1234 -e POSTGRES_DB=booklibrary -p 5432:5432 -d postgres

## Create the network library-network
run command in terminal  
docker network create library-network

## Connect the postgress16 container to the library-network
run command in terminal  
docker network connect library-network postgres16

## Run container library_rest and connect it to DB booklibrary inside container postgres16
run command in terminal  
docker run --name library_rest --network library-network -p 8080:8080 -e MAXIMUM_NUMBER_OF_BOOKS_FOR_A_READER_TO_BORROW_SIMULTANEOUSLY=10 -e MIMIMUM_AGE_OF_A_READER_FOR_BORROWING_RESTRICTED_BOOKS=18 -e DEFAULT_MAX_BORROW_TIME_IN_DAYS=30 -e DB_URL="jdbc:postgresql://postgres16:5432/booklibrary" -e DB_USERNAME=postgres -e DB_PASSWORD=1234 book-library:1.0.0
