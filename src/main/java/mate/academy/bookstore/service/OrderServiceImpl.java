package mate.academy.bookstore.service;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.order.OrderRequestDto;
import mate.academy.bookstore.dto.order.OrderResponseDto;
import mate.academy.bookstore.dto.order.OrderStatusRequestDto;
import mate.academy.bookstore.dto.order.UpdateOrderResponseDto;
import mate.academy.bookstore.dto.orderitem.OrderItemResponseDto;
import mate.academy.bookstore.exception.EntityNotFoundException;
import mate.academy.bookstore.exception.OrderProcessingException;
import mate.academy.bookstore.mapper.OrderItemMapper;
import mate.academy.bookstore.mapper.OrderMapper;
import mate.academy.bookstore.model.CartItem;
import mate.academy.bookstore.model.Order;
import mate.academy.bookstore.model.OrderItem;
import mate.academy.bookstore.model.ShoppingCart;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.repository.itemorder.OrderItemRepository;
import mate.academy.bookstore.repository.order.OrderRepository;
import mate.academy.bookstore.repository.shoppingcart.ShoppingCartRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderItemMapper orderItemMapper;
    private final OrderMapper orderMapper;

    @Transactional
    @Override
    public OrderResponseDto createOrder(Long userId, OrderRequestDto orderRequestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException("Shopping cart not found by userId: " + userId));
        Set<CartItem> cartItems = shoppingCart.getCartItems();
        if (cartItems.isEmpty()) {
            throw new OrderProcessingException("Cart items not found in shoppingCart by userId: "
                + userId);
        }
        Order order = setOrder(shoppingCart, orderRequestDto.getShippingAddress());
        order.setOrderItems(createSetOfOrderItems(order, cartItems));
        shoppingCart.getCartItems().clear();
        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderResponseDto> getOrders(User user) {
        return orderRepository.findByUser(user).stream()
            .map(orderMapper::toDto)
            .toList();
    }

    @Transactional
    @Override
    public UpdateOrderResponseDto updateOrderStatus(Long orderId,
                                                    OrderStatusRequestDto requestDto) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new EntityNotFoundException("Can't find order by id: " + orderId));
        order.setOrderStatus(requestDto.getOrderStatus());
        orderRepository.save(order);
        return orderMapper.toUpdateDto(order);
    }

    @Override
    public List<OrderItemResponseDto> getOrderItemsByOrderId(Long orderId) {
        return getOrderById(orderId).getOrderItems().stream()
            .map(orderItemMapper::toDto)
            .toList();
    }

    @Override
    public OrderItemResponseDto getOrderItemByIdAndOrderId(Long orderItemId, Long orderId) {
        return findOrderItemByOrderIdAndId(orderItemId, orderId);
    }

    private Set<OrderItem> createSetOfOrderItems(Order order, Set<CartItem> cartItems) {
        Set<OrderItem> orderItems = cartItems.stream()
                .map(cartItem -> createOrderItem(order, cartItem))
                .collect(Collectors.toSet());
        orderItemRepository.saveAll(orderItems);
        return orderItems;
    }

    private OrderItem createOrderItem(Order order, CartItem cartItem) {
        OrderItem orderItem = orderItemMapper.toModelFromCartItem(cartItem);
        orderItem.setOrder(order);
        return orderItem;
    }

    private Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() ->
            new EntityNotFoundException("can't find order by id: " + orderId));
    }

    private BigDecimal calculateTotalCartItemsCost(Set<CartItem> cartItems) {
        return cartItems.stream()
            .map(c -> c.getBook().getPrice().multiply(BigDecimal.valueOf(c.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Order setOrder(ShoppingCart shoppingCart, String shippingAddress) {
        Order order = new Order();
        order.setUser(shoppingCart.getUser());
        order.setShippingAddress(shippingAddress);
        order.setTotal(calculateTotalCartItemsCost(shoppingCart.getCartItems()));
        return orderRepository.save(order);
    }

    public OrderItemResponseDto findOrderItemByOrderIdAndId(Long itemId, Long orderId) {
        return orderItemMapper.toDto(
            orderItemRepository.findOrderItemByOrderIdAndId(itemId, orderId).orElseThrow(
                () -> new EntityNotFoundException("Not found item by id " + itemId
                    + " in order by id " + orderId)));
    }
}
