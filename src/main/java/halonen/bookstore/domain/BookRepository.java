package halonen.bookstore.domain;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Long> {

    List<Book> findAll(Sort sort);

    List<Book> findByLikesUser(User user);
}