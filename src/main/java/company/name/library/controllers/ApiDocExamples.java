package company.name.library.controllers;

public class ApiDocExamples {

    public static final String BOOKS_LIST = """
        [
            {
            "id": 1,
            "author": "Herbert Schildt",
            "title": "Java. The Complete Reference. Twelfth Edition",
            "reader": {
              "id": 1,
              "name": "Zhirayr Hovik",
              "birthDate": "2001-10-27"
            },
            "borrowDate": "2023-07-19",
            "maxBorrowTimeInDays": 21,
            "restricted": false
            },
            {
                "id": 2,
                "author": "Walter Savitch",
                "title": "Java. An Introduction to Problem Solving & Programming",
                "reader": null,
                "borrowDate": null,
                "maxBorrowTimeInDays": 14,
                "restricted": false
            }
        ]
    """;

    public static final String BOOKS_OF_READER = """
        [
          {
            "id": 1,
            "author": "Herbert Schildt",
            "title": "Java. The Complete Reference. Twelfth Edition",
            "borrowDate": "2023-07-21",
            "maxBorrowTimeInDays": 14,
            "restricted": false
          },
          {
            "id": 3,
            "author": "Narasimha Karumanchi",
            "title": "Data Structures And Algorithms Made Easy In JAVA",
            "borrowDate": "2023-07-22",
            "maxBorrowTimeInDays": 21,
            "restricted": false
          }
        ]
    """;

    public static final String BOOK_TO_ADD = """
        {
          "author": "Herbert Schildt",
          "title": "Java. The Complete Reference. Twelfth Edition",
          "maxBorrowTimeInDays": 14,
          "restricted": false
        }
    """;

    public static final String ADDED_BOOK = """
        {
          "id": 1,
          "author": "Herbert Schildt",
          "title": "Java. The Complete Reference. Twelfth Edition",
          "maxBorrowTimeInDays": 14,
          "restricted": false
        }
    """;

    public static final String BOOK_TITLE_INVALID = """
        {
          "dateTime": "13.07.2023 18:55:09",
          "errorMessage": "ArgumentValidation Error",
          "errors": [
            {
              "fieldName": "title",
              "invalidValue": "Java",
              "constraint": "Title must be between 10 and 255 characters"
            }
          ]
        }
    """;

    public static final String BOOK_ALREADY_BORROWED = """
        {
          "dateTime": "22.07.2023 17:03:57",
          "errorMessage": "Service layer Error. Book with ID 2 has already borrowed by reader id:1 name:Zhirayr Hovik."
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
            "id": 5,
            "name": "Voski Daniel",
            "birthDate": "2003-05-18"
        }
    """;

    public static final String READERS_LIST = """
        [
          {
            "id": 1,
            "name": "Zhirayr Hovik",
            "birthDate": "2003-05-18",
            "books": []
          },
          {
            "id": 2,
            "name": "Voski Daniel",
            "birthDate": "2003-05-18",
            "books": [
              {
                "id": 1,
                "author": "Herbert Schildt",
                "title": "Java. The Complete Reference. Twelfth Edition",
                "borrowDate": "2023-07-21",
                "maxBorrowTimeInDays": 14,
                "restricted": false
              }
            ]
          },
          {
            "id": 3,
            "name": "Ruben Nazaret",
            "birthDate": "2000-02-20",
            "books": []
          }
        ]
    """;

    public static final String READER_TO_ADD = """
        {
            "name": "Voski Daniel",
            "birthDate": "2003-05-18"
        }
    """;

    public static final String ADDED_READER = """
        {
            "id": 5,
            "name": "Voski Daniel",
            "birthDate": "2003-05-18"
        }
    """;

    public static final String READER_NAME_INVALID = """
        {
          "dateTime": "13.07.2023 19:05:43",
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
          "currentDate": "22.07.2023",
          "currentTime": "20:00:56.2154815",
          "maxNumberOfBooksToBorrow": 10,
          "minAgeOfReaderForRestricted": 18
        }
    """;

}






