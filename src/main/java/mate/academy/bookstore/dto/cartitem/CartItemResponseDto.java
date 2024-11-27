package mate.academy.bookstore.dto.cartitem;

import lombok.Data;

@Data
public class CartItemResponseDto {
    private Long bookId;
    private int quantity;
}
