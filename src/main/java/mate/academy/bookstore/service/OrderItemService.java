package mate.academy.bookstore.service;

import mate.academy.bookstore.dto.orderitem.OrderItemResponseDto;

public interface OrderItemService {
    OrderItemResponseDto findOrderItemByOrderIdAndId(Long itemId, Long orderId);
}
