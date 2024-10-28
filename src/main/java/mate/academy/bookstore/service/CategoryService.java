package mate.academy.bookstore.service;

import java.util.List;
import mate.academy.bookstore.dto.book.BookDto;
import mate.academy.bookstore.dto.category.CategoryRequestDto;
import mate.academy.bookstore.dto.category.CategoryResponseDto;
import mate.academy.bookstore.dto.category.UpdateCategoryRequestDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface CategoryService {
    CategoryResponseDto save(CategoryRequestDto requestDto);

    List<CategoryResponseDto> findAll(Pageable pageable);

    CategoryResponseDto getById(Long id);

    CategoryResponseDto updateCategoryById(Long id, UpdateCategoryRequestDto updateDto);

    void deleteById(Long id);

    List<BookDto> getBooksByCategoriesId(Long id);
}
