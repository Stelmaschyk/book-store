package mate.academy.bookstore.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.BookDto;
import mate.academy.bookstore.dto.CreateBookRequestDto;
import mate.academy.bookstore.dto.UpdateBookRequestDto;
import mate.academy.bookstore.exception.EntityNotFoundException;
import mate.academy.bookstore.mapper.BookMapper;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.repository.BookRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        return bookMapper.toBookDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll() {
        List<Book> bookDtos = bookRepository.findAll();
        return bookMapper.toBookDto(bookDtos);
    }

    @Override
    public BookDto findById(Long id) {
        Book book = bookRepository.findByIsDeletedIsFalseAndId(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find book by id " + id));
        return bookMapper.toBookDto(book);
    }

    public BookDto updateBookById(Long id, UpdateBookRequestDto updateDto) {
        Book existingBook = bookRepository.findByIsDeletedIsFalseAndId(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find book by id " + id));
        Book updateBook = bookMapper.updateBook(updateDto);
        return bookMapper.toBookDto(bookRepository.save(updateBook));
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }
}


