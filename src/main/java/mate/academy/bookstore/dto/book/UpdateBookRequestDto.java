package mate.academy.bookstore.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Data;
import mate.academy.bookstore.validation.Isbn;

@Data
public class UpdateBookRequestDto {
    @NotBlank
    @Size(max = 40)
    private String title;
    @NotBlank
    private String author;
    @Isbn
    private String isbn;
    @NotBlank
    @Positive
    private BigDecimal price;
    @Size(max = 100)
    private String description;
    private String coverImage;
}
