package mate.academy.bookstore.exception;

public class CartItemNotUnique extends RuntimeException {
    public CartItemNotUnique(String message) {
        super(message);
    }
}
