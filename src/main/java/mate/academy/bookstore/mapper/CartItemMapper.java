package mate.academy.bookstore.mapper;

import mate.academy.bookstore.config.MapperConfig;
import mate.academy.bookstore.dto.cartitem.CartItemRequestDto;
import mate.academy.bookstore.dto.cartitem.CartItemResponseDto;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(config = MapperConfig.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CartItemMapper {
    CartItemResponseDto toDto(CartItem cartItem);

    CartItem toModel(CartItemRequestDto request, Book book);
}
