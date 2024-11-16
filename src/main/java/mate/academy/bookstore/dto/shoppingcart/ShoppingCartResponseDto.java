package mate.academy.bookstore.dto.shoppingcart;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Set;
import lombok.Data;
import mate.academy.bookstore.dto.cartitem.CartItemResponseDto;

@Data
public class ShoppingCartResponseDto {
    private Long id;
    private Long userId;
    @JsonProperty("cartItems")
    private Set<CartItemResponseDto> cartItemDtos;
}
