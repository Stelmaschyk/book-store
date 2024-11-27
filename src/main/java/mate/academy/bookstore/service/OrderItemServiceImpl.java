package mate.academy.bookstore.service;

import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.orderitem.OrderItemResponseDto;
import mate.academy.bookstore.exception.EntityNotFoundException;
import mate.academy.bookstore.mapper.OrderItemMapper;
import mate.academy.bookstore.repository.itemorder.OrderItemRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    @Override
    public OrderItemResponseDto findOrderItemByOrderIdAndId(Long itemId, Long orderId) {
        return orderItemMapper.toDto(
            orderItemRepository.findOrderItemByOrderIdAndId(itemId, orderId).orElseThrow(
                () -> new EntityNotFoundException("Not found item by id " + itemId
                + " in order by id " + orderId)));
    }
}
