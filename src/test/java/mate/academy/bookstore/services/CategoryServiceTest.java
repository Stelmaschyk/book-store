package mate.academy.bookstore.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import mate.academy.bookstore.dto.book.BookDto;
import mate.academy.bookstore.dto.category.CategoryRequestDto;
import mate.academy.bookstore.dto.category.CategoryResponseDto;
import mate.academy.bookstore.dto.category.UpdateCategoryRequestDto;
import mate.academy.bookstore.exception.EntityNotFoundException;
import mate.academy.bookstore.mapper.BookMapper;
import mate.academy.bookstore.mapper.CategoryMapper;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.model.Category;
import mate.academy.bookstore.repository.book.BookRepository;
import mate.academy.bookstore.repository.category.CategoryRepository;
import mate.academy.bookstore.service.CategoryServiceImpl;
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
public class CategoryServiceTest {
    private static final long TEST_CATEGORY_ID = 1L;
    private static final long TEST_INCORRECT_CATEGORY_ID = 111L;
    private static final String TEST_CATEGORY_NAME = "Category_1";
    private static final String TEST_CATEGORY_DESCRIPTION = "Category_1";
    private static final String UPDATE_TEST_CATEGORY_NAME = "Update_category_1";
    private static Category category;
    private static CategoryRequestDto categoryRequestDto;
    private static CategoryResponseDto categoryResponseDto;
    private static CategoryResponseDto updatedCategoryResponseDto;
    private static UpdateCategoryRequestDto updateCategoryRequestDto;
    private static List<Category> categories;

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @InjectMocks
    private CategoryServiceImpl categoryService;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;

    @BeforeAll
    static void beforeAll() {
        category = new Category()
            .setId(TEST_CATEGORY_ID)
            .setName(TEST_CATEGORY_NAME)
            .setDescription(TEST_CATEGORY_DESCRIPTION);

        categoryRequestDto = new CategoryRequestDto()
            .setName(UPDATE_TEST_CATEGORY_NAME)
            .setDescription(TEST_CATEGORY_DESCRIPTION);

        categoryResponseDto = new CategoryResponseDto()
            .setId(TEST_CATEGORY_ID)
            .setName(TEST_CATEGORY_NAME)
            .setDescription(TEST_CATEGORY_DESCRIPTION);

        updatedCategoryResponseDto = new CategoryResponseDto()
            .setId(TEST_CATEGORY_ID)
            .setName(UPDATE_TEST_CATEGORY_NAME)
            .setDescription(TEST_CATEGORY_DESCRIPTION);

        updateCategoryRequestDto = new UpdateCategoryRequestDto()
            .setName(UPDATE_TEST_CATEGORY_NAME);

        categories = Arrays.asList(
            new Category()
                .setId(TEST_CATEGORY_ID)
                .setName(TEST_CATEGORY_DESCRIPTION),
            new Category()
                .setId(2L)
                .setName("Drama"));
    }

    @DisplayName("""
        Create a category with a valid categoryRequestDto
        Should return the created CategoryResponseDto
            """)
    @Test
    void createCategory_WithValidRequestDto_ShouldReturnCategoryResponseDto() {
        when(categoryMapper.toModel(categoryRequestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toCategoryDto(category)).thenReturn(categoryResponseDto);

        CategoryResponseDto actual = categoryService.save(categoryRequestDto);

        assertNotNull(actual);
        assertEquals(TEST_CATEGORY_ID, actual.getId());

        verify(categoryMapper, times(1)).toModel(categoryRequestDto);
        verify(categoryRepository, times(1)).save(category);
        verify(categoryMapper, times(1)).toCategoryDto(category);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @DisplayName("""
        Find all categories with a valid request
        Should return Page list of CategoryResponseDto
            """)
    @Test
    void findAllCategories_WithValidRequestDto_ShouldReturnCategoriesResponseDto() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Category> categoryPage = new PageImpl<Category>(categories, pageable,
                categories.size());

        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);

        List<CategoryResponseDto> expectedBookDtos = categories.stream()
                .map(categoryMapper::toCategoryDto)
                .toList();

        Page<CategoryResponseDto> actual = categoryService.findAll(pageable);

        assertNotNull(actual);
        assertEquals(expectedBookDtos.size(), actual.getContent().size());
    }

    @DisplayName("""
        Get category by ID with valid ID
        Should return the CategoryResponseDto
            """)
    @Test
    void getCategoryById_WithValidId_ShouldReturnCategoriesResponseDto() {
        when(categoryRepository.findById(TEST_CATEGORY_ID)).thenReturn(Optional.of(category));
        when(categoryMapper.toCategoryDto(category)).thenReturn(categoryResponseDto);

        CategoryResponseDto actual = categoryService.getById(TEST_CATEGORY_ID);

        assertNotNull(actual);
        assertEquals(TEST_CATEGORY_ID, actual.getId());
    }

    @DisplayName("""
        Get category by ID with non-existing ID
        Should throw EntityNotFoundException
            """)
    @Test
    void getCategoryById_WithNonExistingId_ShouldThrowException() {
        when(categoryRepository.findById(TEST_INCORRECT_CATEGORY_ID)).thenReturn(Optional.empty());

        EntityNotFoundException entityNotFoundException =
                assertThrows(EntityNotFoundException.class,
                    () -> categoryService.getById(TEST_INCORRECT_CATEGORY_ID));

        String expectedMessage = "Can't find category by id " + TEST_INCORRECT_CATEGORY_ID;
        String actualMessage = entityNotFoundException.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @DisplayName("""
        Update category with a valid ID and UpdateCategoryRequestDto
        Should return the updated CategoryResponseDto
            """)
    @Test
    void updateCategory_WithValidUpdateRequestDto_ShouldUpdateCategoryResponseDto() {
        when(categoryRepository.findById(TEST_CATEGORY_ID)).thenReturn(Optional.of(category));
        doAnswer(invocation -> {
            Category updateCategory = invocation.getArgument(1, Category.class);
            updateCategory.setName(updateCategoryRequestDto.getName());
            updateCategory.setDescription(updateCategoryRequestDto.getDescription());
            return null;
        }).when(categoryMapper).updateCategoryFromDto(updateCategoryRequestDto, category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toCategoryDto(category)).thenReturn(updatedCategoryResponseDto);

        CategoryResponseDto expected = categoryService
                .updateCategoryById(TEST_CATEGORY_ID, updateCategoryRequestDto);

        assertEquals(expected.getId(), updatedCategoryResponseDto.getId());
        assertEquals(expected.getName(), updatedCategoryResponseDto.getName());

        verify(categoryMapper, times(1))
                .updateCategoryFromDto(updateCategoryRequestDto, category);
        verify(categoryRepository, times(1)).save(category);
        verify(categoryMapper, times(1))
                .updateCategoryFromDto(updateCategoryRequestDto, category);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @DisplayName("""
        Received books list with valid category id
            """)
    @Test
    void getBooksByCategoryId_WithValidId_ShouldReturnBookResponseDto() {
        Set<Category> categories = new HashSet<>();
        categories.add(category);

        List<Book> books = List.of(
            new Book().setId(1L).setTitle("Book 1").setCategories(categories),
            new Book().setId(2L).setTitle("Book 2").setCategories(categories)
        );

        List<BookDto> expectedBookDto = List.of(
            new BookDto().setId(1L).setTitle("Book 1"),
            new BookDto().setId(2L).setTitle("Book 2")
        );

        when(categoryRepository.existsById(TEST_CATEGORY_ID)).thenReturn(true);
        when(bookRepository.findAllByCategoriesId(TEST_CATEGORY_ID)).thenReturn(books);

        when(bookMapper.toBookDto(books.get(0))).thenReturn(expectedBookDto.get(0));
        when(bookMapper.toBookDto(books.get(1))).thenReturn(expectedBookDto.get(1));

        List<BookDto> actual = categoryService.getBooksByCategoriesId(TEST_CATEGORY_ID);
        assertNotNull(actual);
        assertEquals(expectedBookDto.size(), actual.size());

        verify(categoryRepository, times(1)).existsById(TEST_CATEGORY_ID);
        verify(bookRepository, times(1)).findAllByCategoriesId(TEST_CATEGORY_ID);
        verify(bookMapper, times(1)).toBookDto(books.get(0));
        verify(bookMapper, times(1)).toBookDto(books.get(1));
        verifyNoMoreInteractions(categoryRepository, bookRepository, bookMapper);
    }
}


