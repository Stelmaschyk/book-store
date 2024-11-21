package mate.academy.bookstore.service;

import mate.academy.bookstore.dto.cartitem.CartItemRequestDto;
import mate.academy.bookstore.dto.cartitem.CartItemUpdateDto;
import mate.academy.bookstore.dto.shoppingcart.ShoppingCartResponseDto;
import mate.academy.bookstore.model.User;

public interface ShoppingCartService {

    void createShoppingCart(User user);

    ShoppingCartResponseDto getShoppingCart(String email);

    ShoppingCartResponseDto addCartItem(User user, CartItemRequestDto request);

    ShoppingCartResponseDto updateBooksQuantity(User user, Long cartItemId,
                                                CartItemUpdateDto quantity);

    void deleteById(Long id);
}
