package company.name.service.mapper;

import company.name.models.Book;
import company.name.models.dto.BookRequestDto;
import company.name.models.dto.BookResponseDto;

public class BookMapper {
    public BookResponseDto mapToDto(Book book) {
        BookResponseDto responseDto = new BookResponseDto();
        responseDto.setId(book.getId());
        responseDto.setAuthor(book.getAuthor());
        responseDto.setName(book.getName());
        return responseDto;
    }

    public Book mapToModel(BookRequestDto requestDto) {
        Book book = new Book();
        book.setAuthor(requestDto.getAuthor());
        book.setName(requestDto.getName());
        return book;
    }
}
