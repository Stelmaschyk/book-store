package mate.academy.bookstore.mapper;

import java.util.List;
import mate.academy.bookstore.config.MapperConfig;
import mate.academy.bookstore.dto.book.BookDto;
import mate.academy.bookstore.dto.book.CreateBookRequestDto;
import mate.academy.bookstore.dto.book.UpdateBookRequestDto;
import mate.academy.bookstore.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(config = MapperConfig.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookMapper {
    BookDto toBookDto(Book book);

    List<BookDto> toBookDto(List<Book> book);

    Book toModel(CreateBookRequestDto requestDto);

    void updateBookFromDto(UpdateBookRequestDto book, @MappingTarget Book entity);
}
