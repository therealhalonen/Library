package halonen.library.domain;

import org.springframework.data.repository.CrudRepository;

public interface BorrowRepository extends CrudRepository<Borrow, Long> {

	Borrow findByBook(Book book);
}
