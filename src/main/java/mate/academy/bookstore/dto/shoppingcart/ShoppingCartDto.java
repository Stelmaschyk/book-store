package mate.academy.bookstore.dto.shoppingcart;

import java.util.Set;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import mate.academy.bookstore.model.CartItem;

@Data
public class ShoppingCartDto {
    private long id;
    @NotBlank
    private long userId;
    private Set<CartItem> cartItems;
}