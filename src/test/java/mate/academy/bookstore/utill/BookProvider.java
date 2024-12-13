package mate.academy.bookstore.utill;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;
import mate.academy.bookstore.dto.book.CreateBookRequestDto;
import mate.academy.bookstore.dto.book.UpdateBookRequestDto;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.model.Category;

public class BookProvider {
    private static final String TEST_TITLE = "Title";
    private static final String TEST_AUTHOR = "Author";
    private static final String TEST_ISBN = "978-8996989277";
    private static final String TEST_DESCRIPTION = "Description";
    private static final String TEST_IMAGE = "image.jpg";
    private static final BigDecimal TEST_PRICE = BigDecimal.valueOf(55);
    private static final Long TEST_CATEGORIES_ID = 1L;
    private static final String TEST_UPDATE_TITLE = "Title";
    private static final String TEST_UPDATE_AUTHOR = "Author";
    private static final String TEST_UPDATE_ISBN = "998-9996689279";
    private static final BigDecimal TEST_UPDATE_PRICE = BigDecimal.valueOf(105);

    public static CreateBookRequestDto createRequestDto() {
        return new CreateBookRequestDto()
            .setTitle(TEST_TITLE)
            .setAuthor(TEST_AUTHOR)
            .setIsbn(TEST_ISBN)
            .setPrice(TEST_PRICE)
            .setDescription(TEST_DESCRIPTION)
            .setCoverImage(TEST_IMAGE)
            .setCategoryIds(Set.of(TEST_CATEGORIES_ID));
    }

    public static Book createBook(CreateBookRequestDto requestDto, Category category) {
        return new Book()
        .setTitle(requestDto.getTitle())
        .setAuthor(requestDto.getAuthor())
        .setIsbn(requestDto.getIsbn())
        .setPrice(requestDto.getPrice())
        .setDescription(requestDto.getDescription())
        .setCoverImage(requestDto.getCoverImage())
        .setCategories(Set.of(category));
    }

    public static UpdateBookRequestDto updateRequestDto(Long id) {
        return new UpdateBookRequestDto()
            .setTitle(TEST_UPDATE_TITLE)
            .setAuthor(TEST_UPDATE_AUTHOR)
            .setIsbn(TEST_UPDATE_ISBN)
            .setPrice(TEST_UPDATE_PRICE)
            .setDescription(TEST_DESCRIPTION)
            .setCoverImage(TEST_IMAGE)
            .setCategoryIds(Set.of(TEST_CATEGORIES_ID));
    }

    public static Book updateBook(UpdateBookRequestDto updateBookRequestDtoDto) {
        return new Book()
            .setTitle(updateBookRequestDtoDto.getTitle())
            .setAuthor(updateBookRequestDtoDto.getAuthor())
            .setPrice(updateBookRequestDtoDto.getPrice())
            .setIsbn(updateBookRequestDtoDto.getIsbn())
            .setCategories(updateBookRequestDtoDto.getCategoryIds().stream()
                .map(id -> new Category().setId(id))
                .collect(Collectors.toSet()));
    }
}
