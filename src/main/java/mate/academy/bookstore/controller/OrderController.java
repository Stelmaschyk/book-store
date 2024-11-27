package mate.academy.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.order.OrderRequestDto;
import mate.academy.bookstore.dto.order.OrderResponseDto;
import mate.academy.bookstore.dto.order.OrderStatusRequestDto;
import mate.academy.bookstore.dto.order.UpdateOrderResponseDto;
import mate.academy.bookstore.dto.orderitem.OrderItemResponseDto;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order", description = "Endpoints for orders managing")
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "Submit order", description = "submit current order")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponseDto submitOrder(@RequestBody @Valid OrderRequestDto requestDto,
                                        @AuthenticationPrincipal User user) {
        return orderService.createOrder(user.getId(), requestDto);
    }

    @Operation(summary = "Get orders", description = "get user orders")
    @GetMapping
    public List<OrderResponseDto> getOrders(@AuthenticationPrincipal User user) {
        return orderService.getOrders(user);
    }

    @Operation(summary = "Update order status", description = "update order status by orderId")
    @PatchMapping("/{orderId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public UpdateOrderResponseDto updateOrderStatus(@PathVariable Long orderId,
            @RequestBody @Valid OrderStatusRequestDto requestDto) {
        return orderService.updateOrderStatus(orderId, requestDto);
    }

    @Operation(summary = "Get order items", description = "get all order items from order by "
            + "orderId")
    @GetMapping("/{orderId}/items")
    public List<OrderItemResponseDto> getOrderItemsByOrderId(@PathVariable Long orderId) {
        return orderService.getOrderItemsByOrderId(orderId);
    }

    @Operation(summary = "Get particular OrderItem", description = "Get from particular Order "
            + " particular orderItem by id")
    @GetMapping("/{orderId}/items/{orderItemId}")
    public OrderItemResponseDto getOrderItemByIdAndOrderId(@PathVariable Long orderId,
                                                           @PathVariable Long orderItemId) {
        return orderService.getOrderItemByIdAndOrderId(orderId, orderItemId);
    }
}
