package model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "Book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)// (Long, PK)
    private String title;
    @Column(nullable = false)// (String, not null)
    private String author;
    @Column(nullable = false, unique = true)// (String, not null)
    private String isbn;
    @Column(nullable = false, unique = true)// (String, not null, unique)
    private BigDecimal price;
    private String description;
    private String coverImage;
}
