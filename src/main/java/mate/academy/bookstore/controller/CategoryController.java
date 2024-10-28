package mate.academy.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.book.BookDto;
import mate.academy.bookstore.dto.category.CategoryRequestDto;
import mate.academy.bookstore.dto.category.CategoryResponseDto;
import mate.academy.bookstore.dto.category.UpdateCategoryRequestDto;
import mate.academy.bookstore.service.BookService;
import mate.academy.bookstore.service.CategoryService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Category", description = "Endpoints for managing categories")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final BookService bookService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Create and save new category")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(
            responseCode = "201",
            description = "Category created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CategoryResponseDto.class)
            )
    )
    public CategoryResponseDto createCategory(@RequestBody @Valid CategoryRequestDto requestDto) {
        return categoryService.save(requestDto);
    }

    @Operation(summary = "Get all available categories with pagination")
    @GetMapping
    public List<CategoryResponseDto> getAllCategories(Pageable pageable) {
        return categoryService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by id")
    public CategoryResponseDto getCategoryById(@PathVariable Long id) {
        return categoryService.getById(id);
    }

    @GetMapping("/{id}/books")
    @Operation(summary = "Get books by category id")
    public List<BookDto> getBooksByCategoryId(@PathVariable Long id) {
        return categoryService.getBooksByCategoriesId(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update category by category id")
    public CategoryResponseDto updateCategoryById(@PathVariable Long id,
               @RequestBody @Valid UpdateCategoryRequestDto updateDto) {
        return categoryService.updateCategoryById(id, updateDto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Delete category by id")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategoryById(@PathVariable Long id) {
        categoryService.deleteById(id);
    }
}
