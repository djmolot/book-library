package company.name.service;

import company.name.dao.BookDao;
import company.name.models.Book;
import company.name.models.dto.BookRequestDto;
import company.name.models.dto.BookResponseDto;
import company.name.service.mapper.BookMapper;

import java.util.List;
import java.util.stream.Collectors;

public class BookServiceImpl implements BookService {
    private final BookDao bookDao;
    private final BookMapper bookMapper;

    public BookServiceImpl(BookDao bookDao, BookMapper bookMapper) {
        this.bookDao = bookDao;
        this.bookMapper = bookMapper;
    }

    @Override
    public void createNewBook(BookRequestDto requestDto) {
        Book book = bookMapper.mapToModel(requestDto);
        bookDao.add(book);
    }

    @Override
    public List<BookResponseDto> getAll() {
        return bookDao.getAll().stream()
                .map(bookMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
