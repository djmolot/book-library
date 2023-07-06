package company.name.library.controllers;

public class ApiDocExamples {

    public static final String BOOKS_LIST = """
            [
                {
                    "id": 1,
                    "author": "Herbert Schildt",
                    "title": "Java. The Complete Reference. Twelfth Edition",
                    "reader": null
                    "borrowDate": null,
                    "maxBorrowTimeInDays": 14
                },
                {
                    "id": 2,
                    "author": "Walter Savitch",
                    "title": "Java. An Introduction to Problem Solving & Programming",
                    "reader": null
                    "borrowDate": null,
                    "maxBorrowTimeInDays": 14
                }
            ]
            """;

    public static final String BOOK_TO_ADD = """
        {
          "author": "Herbert Schildt",
          "title": "Java. The Complete Reference. Twelfth Edition",
          "maxBorrowTimeInDays": 14
        }
        """;

    public static final String ADDED_BOOK = """
        {
          "id": 1,
          "author": "Herbert Schildt",
          "title": "Java. The Complete Reference. Twelfth Edition",
          "reader": null,
          "borrowDate": null,
          "maxBorrowTimeInDays": 14
        }
        """;

    public static final String BOOK_ALREADY_BORROWED = """
        {
          "dateTime": "28.05.2023 16:42:41",
          "errorMessage": "Service layer Error. Book with ID 5 has already borrowed by reader Reader(id=3, name=Some Reader, books=null)."
        }
        """;

    public static final String BOOK_IS_NOT_BORROWED = """
        {
          "dateTime": "28.05.2023 16:42:41",
          "errorMessage": "Service layer Error. Book with ID 5 is not borrowed by any reader."
        }
        """;

    public static final String BOOK_DOES_NOT_EXIST = """
        {
          "dateTime": "28.05.2023 17:12:42",
          "errorMessage": "Service layer Error. Book with ID 777 does not exist in DB."
        }
        """;

    public static final String READER_FOUND = """
        {
          "id": 2,
          "name": "Voski Daniel",
          "books": null
        }
        """;

    public static final String READERS_LIST = """
        [
          {
            "id": 1,
            "name": "Zhirayr Hovik",
            "books": []
          },
          {
            "id": 2,
            "name": "Voski Daniel",
            "books": []
          }
        ]
        """;

    public static final String READER_NAME_INVALID = """
        {
          "dateTime": "28.05.2023 19:27:24",
          "errorMessage": "ArgumentValidation Error",
          "errors": [
            {
              "fieldName": "name",
              "invalidValue": "Vo",
              "constraint": "Reader name must be between 3 and 50 characters"
            }
          ]
        }
        """;

    public static final String READER_DOES_NOT_EXIST = """
        {
          "dateTime": "28.05.2023 19:46:14",
          "errorMessage": "Service layer Error. Reader with ID 555 does not exist in DB."
        }
        """;

    public static final String WELCOME_OBJECT = """
        {
          "message": "Welcome to the library!",
          "currentDate": "04.06.2023",
          "currentTime": "18:47:39.890308"
        }
        """;

}






