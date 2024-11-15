package mate.academy.bookstore.service;

import mate.academy.bookstore.dto.cartitem.CartItemDto;
import mate.academy.bookstore.dto.cartitem.CartItemRequestDto;
import mate.academy.bookstore.dto.cartitem.CartItemUpdateDto;
import mate.academy.bookstore.dto.shoppingcart.ShoppingCartDto;
import mate.academy.bookstore.model.User;

public interface ShoppingCartService {
    ShoppingCartDto getCart(String email);

    CartItemDto addCartItem(User user, CartItemRequestDto request);

    CartItemDto updateCartItemById(User user, Long id, CartItemUpdateDto request);

    void deleteById(Long id);

    void createShoppingCart(User user);
}