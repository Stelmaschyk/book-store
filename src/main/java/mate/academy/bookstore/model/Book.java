package mate.academy.bookstore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import mate.academy.bookstore.validation.Isbn;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Setter
@SQLDelete(sql = "UPDATE books SET is_deleted = true WHERE id=?")
@SQLRestriction("is_deleted=false")
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @NotNull
    private String title;
    @Column(nullable = false)
    @NotNull
    private String author;
    @Column(nullable = false, unique = true)
    @NotNull
    @Isbn
    private String isbn;
    @Column(nullable = false)
    @NotNull
    @Min(0)
    private BigDecimal price;
    @NotNull
    private String description;
    private String coverImage;
    @Column(nullable = false)
    private boolean isDeleted = false;
}
