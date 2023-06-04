# Book Library
The service for tracking borrowed books and book readers (borrowers).

## Book Library REST API endpoints
Show Swagger-UI page http://localhost:8080/book-library
1. Show all books in the library GET http://localhost:8080/api/v1/library/books
2. Show all readers registered in the library GET http://localhost:8080/api/v1/library/readers
3. Register new reader POST http://localhost:8080/api/v1/library/readers {"name":"Some Reader"}
4. Add new book POST http://localhost:8080/api/v1/library/books {"author":"Some Author", "title":"Some Book Title"}
5. Borrow a book to a reader POST http://localhost:8080/api/v1/library/books/{bookId}/readers/{readerId}
6. Return a book to a library DELETE http://localhost:8080/api/v1/library/books/{bookId}/readers
7. Show all books borrowed by reader GET http://localhost:8080/api/v1/library/readers/{readerId}/books
8. Show current reader of a book GET http://localhost:8080/api/v1/library/books/{bookId}/readers