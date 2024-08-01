package mate.academy.bookstore.repository;

import java.util.Optional;
import mate.academy.bookstore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query(value = "SELECT * FROM book WHERE id = ?1", nativeQuery = true)
    Optional<Book> getBookById(Long id);
}
