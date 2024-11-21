package mate.academy.bookstore.service;

import jakarta.transaction.Transactional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.cartitem.CartItemRequestDto;
import mate.academy.bookstore.dto.cartitem.CartItemUpdateDto;
import mate.academy.bookstore.dto.shoppingcart.ShoppingCartResponseDto;
import mate.academy.bookstore.exception.EntityNotFoundException;
import mate.academy.bookstore.mapper.CartItemMapper;
import mate.academy.bookstore.mapper.ShoppingCartMapper;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.model.CartItem;
import mate.academy.bookstore.model.ShoppingCart;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.repository.book.BookRepository;
import mate.academy.bookstore.repository.cartitem.CartItemRepository;
import mate.academy.bookstore.repository.shoppingcart.ShoppingCartRepository;
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

    @Transactional
    @Override
    public ShoppingCartResponseDto addCartItem(User user, CartItemRequestDto request) {
        Book book = getBook(request);
        ShoppingCart shoppingCart = getShoppingCart(user);
        Set<Long> bookIds = shoppingCart.getCartItems().stream()
                .map(cartItem -> cartItem.getBook().getId())
                .collect(Collectors.toSet());
        CartItem cartItem = getCartItem(request, bookIds, shoppingCart, book);
        shoppingCart.getCartItems().add(cartItem);
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toDto(shoppingCartRepository.save(shoppingCart));
    }

    @Transactional
    @Override
    public ShoppingCartResponseDto updateBooksQuantity(User user, Long cartItemId,
                                                   CartItemUpdateDto request) {
        ShoppingCart shoppingCart = getShoppingCart(user);
        CartItem cartItem = findCartItem(shoppingCart, cartItemId);
        cartItem.setQuantity(request.getQuantity());
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toDto(shoppingCartRepository.save(cartItem.getShoppingCart()));
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        cartItemRepository.deleteById(id);
    }

    private Book getBook(CartItemRequestDto cartItemRequestDto) {
        return bookRepository.findById(cartItemRequestDto.getBookId())
            .orElseThrow(() -> new EntityNotFoundException("There is not such book "
                    + cartItemRequestDto.getBookId()));
    }

    @Override
    public ShoppingCartResponseDto getShoppingCart(String email) {
        return shoppingCartMapper.toDto(shoppingCartRepository.findByUserEmail(email));
    }

    private ShoppingCart getShoppingCart(User user) {
        return shoppingCartRepository.findByUserEmail(user.getEmail());
    }

    private CartItem getCartItem(
            CartItemRequestDto cartItemRequestDto,
            Set<Long> bookIds,
            ShoppingCart shoppingCart,
            Book book) {
        boolean containsBookId = bookIds.contains(cartItemRequestDto.getBookId());
        CartItem cartItem = cartItemMapper.toModel(cartItemRequestDto);
        if (containsBookId) {
            CartItem existingCartItem = findCartItem(
                    shoppingCart, book.getId());
            existingCartItem.setQuantity(existingCartItem.getQuantity()
                    + cartItemRequestDto.getQuantity());
            cartItem = existingCartItem;
        } else {
            cartItem.setQuantity(cartItemRequestDto.getQuantity());
            cartItem.setShoppingCart(shoppingCart);
            cartItem.setBook(book);
        }
        return cartItem;
    }

    private CartItem findCartItem(ShoppingCart shoppingCart, Long cartItemId) {
        return shoppingCart.getCartItems()
            .stream()
            .filter(cartItem -> cartItem.getBook().getId().equals(cartItemId))
            .findFirst()
            .orElseThrow(() -> new EntityNotFoundException("CartItem with id " + cartItemId
                + " not found"));
    }
}
