package mate.academy.bookstore.service;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import mate.academy.bookstore.dto.book.BookDto;
import mate.academy.bookstore.dto.book.CreateBookRequestDto;
import mate.academy.bookstore.dto.book.UpdateBookRequestDto;
import mate.academy.bookstore.exception.EntityNotFoundException;
import mate.academy.bookstore.mapper.BookMapper;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.repository.book.BookRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    private static final Long TEST_BOOK_ID = 1L;
    private static final Long INCORRECT_BOOK_ID = 111L;
    private static Book book;
    private static Book updatedBook;
    private static BookDto expectedDto;
    private static BookDto updatedExpectedDto;
    private static CreateBookRequestDto requestDto;
    private static UpdateBookRequestDto updatedRequestDto;
    private static List<Book> books;

    @Mock
    private BookMapper bookMapper;
    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private BookServiceImpl bookService;

    @BeforeAll
    static void beforeAll() {
        book = new Book().setId(TEST_BOOK_ID)
            .setTitle("Test_Book_1")
            .setAuthor("Test_Author_1")
            .setPrice(BigDecimal.valueOf(20.00))
            .setIsbn("111-1111111111");
        requestDto = new CreateBookRequestDto()
            .setTitle("Test_Book_1")
            .setAuthor("Test_Author_1")
            .setPrice(BigDecimal.valueOf(20.00))
            .setIsbn("111-1111111111");
        expectedDto = new BookDto().setId(TEST_BOOK_ID)
            .setTitle("Test_Book_1")
            .setAuthor("Test_Author_1")
            .setPrice(BigDecimal.valueOf(20.00))
            .setIsbn("111-1111111111");
        books = List.of(book, new Book().setId(TEST_BOOK_ID)
            .setTitle("Test_Book_2")
            .setAuthor("Test_Author_2")
            .setPrice(BigDecimal.valueOf(30.00))
            .setIsbn("222-2222222222"));
        updatedBook = new Book().setId(TEST_BOOK_ID)
            .setTitle("Update_Test_Book_1.1")
            .setAuthor("Update_Test_Author_1.1")
            .setPrice(BigDecimal.valueOf(30.00))
            .setIsbn("111-1111111111");
        updatedRequestDto = new UpdateBookRequestDto()
            .setTitle("Update_Test_Book_1.1")
            .setAuthor("Update_Test_Author_1.1")
            .setPrice(BigDecimal.valueOf(30.00))
            .setIsbn("111-1111111111");
        updatedExpectedDto = new BookDto().setId(TEST_BOOK_ID)
            .setTitle("Update_Test_Book_1.1")
            .setAuthor("Update_Test_Author_1.1")
            .setPrice(BigDecimal.valueOf(30.00))
            .setIsbn("111-1111111111");
    }

    @Test
    @DisplayName("""
        Save a book with a valid create book request
        Should return the created BookDto
            """)
    public void saveBook_WithValidCreateBookRequestDto_ShouldReturnRequestDto() {
        when(bookMapper.toModel(requestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toBookDto(book)).thenReturn(expectedDto);

        BookDto actual = bookService.save(requestDto);

        assertThat(actual)
                .isNotNull()
                .isEqualTo(expectedDto);

        verify(bookMapper, times(1)).toModel(requestDto);
        verify(bookRepository, times(1)).save(book);
        verify(bookMapper, times(1)).toBookDto(book);
        verifyNoMoreInteractions(bookMapper, bookRepository);
    }

    @Test
    @DisplayName("""
        Find all books with a valid request
        Should return a list of BookDto
            """)
    public void findAllBooks_WithValidPageable_ShouldReturnListOfBookDto() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());

        List<BookDto> expectedDto = books.stream()
                .map(book -> bookMapper.toBookDto(book))
                .toList();

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toBookDto(books)).thenReturn(expectedDto);
        List<BookDto> actual = bookService.findAll(pageable);

        assertThat(actual)
                .isNotEmpty()
                .isEqualTo(expectedDto);

        verify(bookRepository, times(1)).findAll(pageable);
        verify(bookMapper, times(1)).toBookDto(books);
    }

    @Test
    @DisplayName("""
        Find a book by ID with valid ID
        Should return the corresponding BookDto
            """)
    public void findBookById_WithValidId_ShouldReturnBookDto() {
        when(bookRepository.findById(TEST_BOOK_ID)).thenReturn(Optional.of(book));
        when(bookMapper.toBookDto(book)).thenReturn(expectedDto);

        BookDto actual = bookService.findById(TEST_BOOK_ID);

        assertThat(actual)
                .isNotNull()
                .isEqualTo(expectedDto);
    }

    @Test
    @DisplayName("""
        Find a book by ID with non-existing book ID
        Should throw EntityNotFoundException
            """)
    public void findBookById_WithNonExistingBookId_ShouldThrowException() {
        when(bookRepository.findById(INCORRECT_BOOK_ID)).thenReturn(Optional.empty());

        EntityNotFoundException entityNotFoundException = assertThrows(
                EntityNotFoundException.class, () -> bookService.findById(INCORRECT_BOOK_ID));

        String expectedMessage = "Can't find book by id " + INCORRECT_BOOK_ID;
        String actual = entityNotFoundException.getMessage();

        assertThat(actual)
                .isNotNull()
                .isEqualTo(expectedMessage);

        verify(bookRepository, times(1)).findById(INCORRECT_BOOK_ID);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("""
        Update a book by ID with valid data
        Should return the updated BookDto
            """)
    public void updateBookById_WithValidId_ShouldReturnBookDto() {
        when(bookRepository.findById(TEST_BOOK_ID)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(updatedBook);
        when(bookMapper.toBookDto(updatedBook)).thenReturn(updatedExpectedDto);

        BookDto actual = bookService.updateBookById(TEST_BOOK_ID, updatedRequestDto);

        assertThat(actual)
                .isNotNull()
                .isEqualTo(updatedExpectedDto);

        verify(bookRepository, times(1)).findById(TEST_BOOK_ID);
        verify(bookRepository, times(1)).save(book);
        verify(bookMapper, times(1)).toBookDto(updatedBook);
        verifyNoMoreInteractions(bookRepository);
    }
}
