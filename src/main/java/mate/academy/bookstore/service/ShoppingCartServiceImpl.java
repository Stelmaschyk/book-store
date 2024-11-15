package mate.academy.bookstore.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.cartitem.CartItemDto;
import mate.academy.bookstore.dto.cartitem.CartItemRequestDto;
import mate.academy.bookstore.dto.cartitem.CartItemUpdateDto;
import mate.academy.bookstore.dto.shoppingcart.ShoppingCartDto;
import mate.academy.bookstore.exception.CartItemNotUnique;
import mate.academy.bookstore.exception.EntityNotFoundException;
import mate.academy.bookstore.mapper.CartItemMapper;
import mate.academy.bookstore.mapper.ShoppingCartMapper;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.model.CartItem;
import mate.academy.bookstore.model.ShoppingCart;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.repository.book.BookRepository;
import mate.academy.bookstore.repository.shoppingcart.ShoppingCartRepository;
import mate.academy.bookstore.repository.сartItem.CartItemRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final BookRepository bookRepository;
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final ShoppingCartMapper shoppingCartMapper;

    @Override
    public void createShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCartDto getCart(String email) {
        return shoppingCartMapper.toDto(shoppingCartRepository.findByUserEmail(email));
    }

    @Override
    public CartItemDto addCartItem(User user, CartItemRequestDto request) {
        Optional<Book> optionalBook = bookRepository.findById(request.getBookId());
        Book book = optionalBook.orElseThrow(
            () -> new EntityNotFoundException("There is not such book"));
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserEmail(user.getEmail());
        if (cartItemRepository.existsByBook(book)) {
            List<CartItem> cartItemsByBook = cartItemRepository.findByBook(book);
            for (CartItem cartItem : cartItemsByBook) {
                if (Objects.equals(cartItem.getShoppingCart().getId(), shoppingCart.getId())) {
                    throw new CartItemNotUnique("Cart item with book " + book.getId()
                        + " already exists");
                }
            }
        }
        CartItem cartItem = cartItemMapper.toModel(request);
        cartItem.setBook(book);
        cartItem.setShoppingCart(shoppingCart);
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public CartItemDto updateCartItemById(User user, Long id, CartItemUpdateDto request) {
        CartItem cartItem = cartItemRepository.findById(id).orElseThrow(
            () -> new EntityNotFoundException("There is not such car item"));
        cartItem.setQuantity(request.getQuantity());
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public void deleteById(Long id) {
        cartItemRepository.deleteById(id);
    }

}