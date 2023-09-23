package halonen.bookstore.domain;

import org.springframework.data.repository.CrudRepository;

public interface LoanRepository extends CrudRepository<Loan, Long> {

	Loan findByBook(Book book);
}
