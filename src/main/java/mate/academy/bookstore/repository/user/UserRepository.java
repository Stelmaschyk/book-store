package mate.academy.bookstore.repository.user;

import java.util.Optional;
import mate.academy.bookstore.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    boolean existsByEmail(String email);

    @EntityGraph(attributePaths = "roles")
    Optional<User> findByEmail(String email);
}
