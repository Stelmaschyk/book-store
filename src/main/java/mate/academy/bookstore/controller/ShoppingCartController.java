package mate.academy.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.cartitem.CartItemDto;
import mate.academy.bookstore.dto.cartitem.CartItemRequestDto;
import mate.academy.bookstore.dto.cartitem.CartItemUpdateDto;
import mate.academy.bookstore.dto.shoppingcart.ShoppingCartDto;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.service.ShoppingCartService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping cart", description = "Endpoints for managing shopping carts")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @PostMapping
    @Operation(summary = "Add item cart", description = "Add item cart to the shopping cart")
    public CartItemDto addItemCart(@RequestBody @Valid CartItemRequestDto request,
                                   Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.addCartItem(user, request);
    }

    @GetMapping
    @Operation(summary = "Get user cart",
        description = "Receive user's shopping cart by email")
    public ShoppingCartDto getCart(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        ShoppingCartDto cart = shoppingCartService.getCart(user.getEmail());
        return cart;
    }

    @PutMapping("/items/{id}")
    @Operation(summary = "Update item cart", description = "Update item cart in the shopping cart")
    public CartItemDto updateCartItem(@RequestBody @Valid CartItemUpdateDto request,
                                      @PathVariable @Positive Long id,
                                      Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.updateCartItemById(user, id, request);
    }

    @DeleteMapping("/items/{id}")
    @Operation(summary = "Delete item cart", description = "Delete item cart in the shopping cart")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCartItem(@PathVariable @Positive Long id) {
        shoppingCartService.deleteById(id);
    }
}

