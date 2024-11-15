package mate.academy.bookstore.repository.сartItem;

import java.util.List;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    boolean existsByBook(Book book);

    List<CartItem> findByBook(Book book);
}