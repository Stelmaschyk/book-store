package mate.academy.bookstore.repository.role;

import mate.academy.bookstore.model.Role;
import mate.academy.bookstore.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(RoleName roleName);
}
