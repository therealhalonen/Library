package halonen.bookstore.domain;

import org.springframework.data.repository.CrudRepository;

public interface LoanRepository extends CrudRepository<Loan, Long> {

	Loan findByBookAndUser(Book book, User user);

	Loan findByBook(Book book);
}
