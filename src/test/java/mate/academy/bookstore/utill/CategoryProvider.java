package mate.academy.bookstore.utill;

import mate.academy.bookstore.dto.category.CategoryRequestDto;
import mate.academy.bookstore.dto.category.UpdateCategoryRequestDto;

public class CategoryProvider {
    public static UpdateCategoryRequestDto createUpdateCategoryRequestDto() {
        return new UpdateCategoryRequestDto()
            .setName("drama")
            .setDescription("updated description");
    }

    public static CategoryRequestDto createCategoryRequestDto() {
        return new CategoryRequestDto()
            .setName("Thriller")
            .setDescription("dramatic context");
    }
}
