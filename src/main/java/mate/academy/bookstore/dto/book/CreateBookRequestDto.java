package mate.academy.bookstore.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;
import lombok.experimental.Accessors;
import mate.academy.bookstore.validation.Isbn;

@Data
@Accessors(chain = true)
public class CreateBookRequestDto {
    @NotBlank
    @Size(max = 40)
    private String title;
    @NotBlank
    private String author;
    @Isbn
    private String isbn;
    @NotNull
    @Positive
    private BigDecimal price;
    @Size(max = 150)
    private String description;
    private String coverImage;
    @NotEmpty
    private Set<Long> categoryIds;
}
