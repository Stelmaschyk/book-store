package mate.academy.bookstore.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import mate.academy.bookstore.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.bookstore.dto.category.CategoryRequestDto;
import mate.academy.bookstore.dto.category.CategoryResponseDto;
import mate.academy.bookstore.dto.category.UpdateCategoryRequestDto;
import mate.academy.bookstore.utill.CategoryProvider;
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
public class CategoryControllerTest {
    protected static MockMvc mockMvc;
    private static final long TEST_CATEGORY_ID = 2L;
    private static final int TEST_CATEGORY_BOOKS_QUANTITY = 2;
    private static final int TEST_CATEGORY_AMOUNT = 2;

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
                        "database/categories/delete-categories-from-table-categories.sql")
            );
        }
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Create a new category")
    void createCategory_ValidRequestDto_Success() throws Exception {
        CategoryRequestDto requestDto = CategoryProvider.createCategoryRequestDto();

        CategoryResponseDto expected = new CategoryResponseDto()
                .setName(requestDto.getName())
                .setDescription(requestDto.getDescription());

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post("/categories")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON)
            )
                .andExpect(status().isCreated())
                .andReturn();

        CategoryResponseDto actual =
                objectMapper.readValue(result.getResponse().getContentAsString(),
                CategoryResponseDto.class);
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @WithMockUser(username = "user")
    @Test
    @DisplayName("Get a category by id")
    void getCategoryById_ValidCategoryId_ShouldReturnCategoryResponseDto() throws Exception {
        MvcResult result = mockMvc.perform(get("/categories/{id}", TEST_CATEGORY_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryResponseDto.class);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(TEST_CATEGORY_ID, actual.getId());
    }

    @WithMockUser(username = "user")
    @Test
    @DisplayName("Get books list by category id")
    @Sql(scripts = "classpath:database/categories_books/add-books-to-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/categories_books"
                + "/delete-data-from-categories-books-table.sql",
                executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getBooksByCategory_ValidCategory_ShouldReturnListBookDto() throws Exception {
        MvcResult result = mockMvc.perform(get("/categories/{id}/books", TEST_CATEGORY_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDtoWithoutCategoryIds[] actual = objectMapper.readValue(
            result.getResponse().getContentAsString(), BookDtoWithoutCategoryIds[].class);

        Assertions.assertEquals(TEST_CATEGORY_BOOKS_QUANTITY, actual.length);
        Assertions.assertTrue(Arrays.stream(actual)
                .anyMatch(bookDtoWithoutCategoryIds -> bookDtoWithoutCategoryIds.getIsbn().equals(
                "978-8996989274")));
        Assertions.assertTrue(Arrays.stream(actual)
                .anyMatch(bookDtoWithoutCategoryIds -> bookDtoWithoutCategoryIds.getTitle().equals(
                "verdict")));
    }

    @WithMockUser(username = "user")
    @Test
    @DisplayName("Find all categories")
    void findAll_GiveAllCategories_ShouldReturnAllCategories() throws Exception {
        MvcResult result = mockMvc.perform(get("/categories")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode rootNode = objectMapper.readTree(result.getResponse().getContentAsString());
        JsonNode contentNode = rootNode.get("content");
        CategoryResponseDto[] actual = objectMapper.treeToValue(contentNode,
            CategoryResponseDto[].class);

        Assertions.assertEquals(TEST_CATEGORY_AMOUNT, actual.length);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Delete category by id")
    @Sql(scripts = "classpath:database/categories/add-test-category-in-category-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/categories_books/delete-test-category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteById_WithValidCategoryId_Success() throws Exception {
        MvcResult result = mockMvc.perform(delete("/categories/{id}", 4)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("update categories by id")
    void updateById_WithValidCategoryId_ShouldReturnUpdatedCategoryResponseDto() throws Exception {
        UpdateCategoryRequestDto requestDto = CategoryProvider.createUpdateCategoryRequestDto();
        MvcResult result = mockMvc.perform(put("/categories/{id}", TEST_CATEGORY_ID)
                .content(objectMapper.writeValueAsString(requestDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryResponseDto.class);
        EqualsBuilder.reflectionEquals(requestDto, actual);
    }
}
