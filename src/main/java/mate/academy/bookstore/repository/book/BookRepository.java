package mate.academy.bookstore.repository.book;

import java.util.List;
import java.util.Optional;
import mate.academy.bookstore.model.Book;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    List<Book> findAllByCategoriesId(Long categoryId);

    @EntityGraph(attributePaths = "categories")
    Optional<Book> findById(Long id);
}
