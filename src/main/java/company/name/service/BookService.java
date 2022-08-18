package company.name.service;

import company.name.models.Book;
import company.name.models.dto.BookRequestDto;
import company.name.models.dto.BookResponseDto;

import java.util.List;

public interface BookService {
    void createNewBook(BookRequestDto requestDto);

    List<BookResponseDto> getAll();
}
