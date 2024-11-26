package mate.academy.bookstore.service;

import java.util.List;
import mate.academy.bookstore.dto.order.OrderRequestDto;
import mate.academy.bookstore.dto.order.OrderResponseDto;
import mate.academy.bookstore.dto.order.OrderStatusRequestDto;
import mate.academy.bookstore.dto.orderitem.OrderItemResponseDto;
import mate.academy.bookstore.model.User;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponseDto createOrder(Long userId, OrderRequestDto orderRequestDto, Pageable pageable);

    List<OrderResponseDto> getOrders(User user);

    void updateOrderStatus(Long orderId, OrderStatusRequestDto requestDto);

    List<OrderItemResponseDto> getOrderItemsByOrderId(Long orderId);

    OrderItemResponseDto getOrderItemByIdAndOrderId(Long orderId, Long orderItemId);
}
