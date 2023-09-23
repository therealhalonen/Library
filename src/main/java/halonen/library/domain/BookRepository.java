package halonen.library.domain;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Long> {

	List<Book> findAll(Sort sort);

	List<Book> findByLikesUser(User user);
}