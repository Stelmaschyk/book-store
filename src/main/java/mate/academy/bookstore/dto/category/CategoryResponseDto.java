package mate.academy.bookstore.dto.category;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@RequiredArgsConstructor
public class CategoryResponseDto {
    private Long id;
    private String name;
    private String description;
}
