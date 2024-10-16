package mate.academy.bookstore.service;

import java.util.List;
import mate.academy.bookstore.dto.book.BookDto;
import mate.academy.bookstore.dto.category.CategoryRequestDto;
import mate.academy.bookstore.dto.category.CategoryResponceDto;
import mate.academy.bookstore.dto.category.UpdateCategoryRequestDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface CategoryService {
    CategoryResponceDto save(CategoryRequestDto requestDto);

    List<CategoryResponceDto> findAll(Pageable pageable);

    CategoryResponceDto getById(Long id);

    CategoryResponceDto updateCategoryById(Long id, UpdateCategoryRequestDto updateDto);

    void deleteById(Long id);

    List<BookDto> getBooksByCategoriesId(Long id);
}
