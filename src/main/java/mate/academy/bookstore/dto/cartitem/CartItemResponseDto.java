package mate.academy.bookstore.dto.cartitem;

import lombok.Data;

@Data
public class CartItemResponseDto {
    private Long id;
    private Long bookId;
    private String title;
    private int quantity;
}
