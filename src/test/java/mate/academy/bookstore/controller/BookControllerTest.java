package mate.academy.bookstore.controller;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import mate.academy.bookstore.dto.book.BookDto;
import mate.academy.bookstore.dto.book.BookSearchParametersDto;
import mate.academy.bookstore.dto.book.CreateBookRequestDto;
import mate.academy.bookstore.dto.book.UpdateBookRequestDto;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.model.Category;
import mate.academy.bookstore.utill.BookProvider;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {
    protected static MockMvc mockMvc;
    private static final int EXPECTED_LENGTH = 4;
    private static final Long TEST_ID = 2L;
    private static final String CATEGORY_NAME = "Fantasy";
    private static final String TEST_BOOK_AUTHOR = "Nicolas";
    private static final String TEST_BOOK_TITLE = "New Year";
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired DataSource dataSource,
                          @Autowired WebApplicationContext applicationContext
    ) throws SQLException {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(applicationContext)
            .apply(springSecurity())
            .build();
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                connection,
                new ClassPathResource(
                    "database/categories/add-categories-in-category-table.sql")
            );
            ScriptUtils.executeSqlScript(
                connection,
                new ClassPathResource(
                    "database/books/add-books-in-table.sql")
            );
        }
    }

    @AfterAll
    static void afterAll(
        @Autowired DataSource dataSource
    ) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                connection,
                new ClassPathResource(
                    "database/categories_books/delete-data-from-categories-books-table.sql")
            );
            ScriptUtils.executeSqlScript(
                connection,
                new ClassPathResource(
                    "database/books/delete-books-from-books-table.sql")
            );
            ScriptUtils.executeSqlScript(
                connection,
                new ClassPathResource(
                    "database/categories/delete-category-from-table-category.sql")
            );
        }
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Create a new book")
    void createBook_ValidRequestDto_Success() throws Exception {
        CreateBookRequestDto requestDto = BookProvider.createRequestDto();

        BookDto expected = new BookDto()
            .setTitle(requestDto.getTitle())
            .setAuthor(requestDto.getAuthor())
            .setIsbn(requestDto.getIsbn())
            .setPrice(requestDto.getPrice())
            .setDescription(requestDto.getDescription())
            .setCoverImage(requestDto.getCoverImage())
            .setCategoryIds(requestDto.getCategoryIds());

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post("/books")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isCreated())
            .andReturn();

        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
            BookDto.class);
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("get all books")
    void getAllBook_GiveBooks_ReturnAllBooksDto() throws Exception {
        MvcResult result = mockMvc.perform(get("/books")
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andReturn();

        BookDto[] actual = objectMapper.readValue(
            result.getResponse().getContentAsString(), BookDto[].class);
        List<Long> bookIds = Arrays.stream(actual)
            .map(BookDto::getId)
            .toList();

        Assertions.assertEquals(EXPECTED_LENGTH, actual.length);
        assertThat(bookIds).containsExactlyInAnyOrder(1L, 2L, 3L, 4L);
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Get book by id")
    void getById_WithValidBookId_ShouldReturnBookDto() throws Exception {
        MvcResult result = mockMvc.perform(get("/books/{id}", TEST_ID)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        BookDto actual = objectMapper.readValue(
            result.getResponse().getContentAsString(), BookDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(TEST_ID, actual.getId());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("update book by id")
    void updateById_WithValidBookId_ShouldReturnUpdatedBookDto() throws Exception {
        UpdateBookRequestDto updateDto = BookProvider.updateRequestDto(TEST_ID);
        Book expected = BookProvider.updateBook(updateDto);
        MvcResult result = mockMvc.perform(put("/books/{id}", TEST_ID)
                .content(objectMapper.writeValueAsString(updateDto))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();
        BookDto actual = objectMapper.readValue(
            result.getResponse().getContentAsString(), BookDto.class);
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("delete book by id")
    void deleteById_WithValidBookId_Success() throws Exception {
        MvcResult result = mockMvc.perform(delete("/books/{id}", 4)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andReturn();
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("search book by parameters title or author")
    void searchBook_BookSearchParametersDto_ReturnBookDto() throws Exception {
        BookSearchParametersDto params = new BookSearchParametersDto(
            new String[]{TEST_BOOK_AUTHOR}, new String[]{TEST_BOOK_TITLE});

        MvcResult result = mockMvc.perform(get("/books/search")
                .content(objectMapper.writeValueAsString(params))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        BookDto[] actual = objectMapper.readValue(
            result.getResponse().getContentAsString(), BookDto[].class);
        Assertions.assertEquals(EXPECTED_LENGTH, actual.length);
        Assertions.assertTrue(Arrays.stream(actual).anyMatch(
            (bookDto -> bookDto.getAuthor().equals(TEST_BOOK_AUTHOR))));
        Assertions.assertTrue(Arrays.stream(actual).anyMatch(
            (bookDto -> bookDto.getTitle().equals(TEST_BOOK_TITLE))));
    }
}