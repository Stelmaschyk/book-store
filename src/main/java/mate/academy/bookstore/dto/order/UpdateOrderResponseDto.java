package mate.academy.bookstore.dto.order;

import lombok.Data;
import mate.academy.bookstore.model.Order;

@Data
public class UpdateOrderResponseDto {
    private Order.OrderStatus orderStatus;
}
