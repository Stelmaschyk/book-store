package mate.academy.bookstore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Setter
@SQLDelete(sql = "UPDATE shopping_carts SET is_deleted = true WHERE id=?")
@SQLRestriction("is_deleted=false")
@Table(name = "shopping_carts")
@RequiredArgsConstructor
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "user_id", nullable = false)
    @OneToOne
    private User user;
    @Column(nullable = false)
    private boolean isDeleted = false;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "shoppingCart")
    private Set<CartItem> cartItems;

    public void addCartItem(CartItem cartItem) {
        cartItems.add(cartItem);
        cartItem.setShoppingCart(this);
    }
}
