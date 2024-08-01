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
        return bookRepository.findAll().stream()
            .map(bookMapper::toBookDto)
            .toList();
    }

    @Override
    public BookDto findById(Long id) {
        Book book = bookRepository.getBookById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find book by id " + id));
        return bookMapper.toBookDto(book);
    }

    @Override
    public BookDto updateBookById(Long id, UpdateBookRequestDto updateDto) {
        Book existingBook = bookRepository.getBookById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find book by id " + id));
        existingBook.setTitle(updateDto.getTitle());
        existingBook.setAuthor(updateDto.getAuthor());
        existingBook.setIsbn(updateDto.getIsbn());
        existingBook.setPrice(updateDto.getPrice());
        existingBook.setDescription(updateDto.getDescription());
        existingBook.setCoverImage(updateDto.getCoverImage());
        return bookMapper.updateBookDto(bookRepository.save(existingBook));
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }
}


