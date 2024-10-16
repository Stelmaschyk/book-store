package mate.academy.bookstore.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateCategoryRequestDto {
    @NotBlank
    @Size(max = 50)
    private String name;
    @NotBlank
    private String description;
}
