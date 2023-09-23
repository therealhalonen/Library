package halonen.bookstore;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import halonen.bookstore.domain.Book;
import halonen.bookstore.domain.BookRepository;
import halonen.bookstore.domain.Category;
import halonen.bookstore.service.LoanStatus;

@DataJpaTest
public class BookRepositoryTests {

	@Autowired
	private BookRepository bookRepository;

	@Test
	public void testSaveAndFindById() {
		// Create a Book entity and save it to the repository
		Book book = new Book();
		book.setTitle("Test Book");
		book.setAuthor("Test Author");
		book.setPublicationYear(2023); // Set publication year
		book.setIsbn("1234567890"); // Set ISBN
		book.setPrice(19.99); // Set price
		Category category = new Category();
		category.setName("Test Category");
		book.setCategory(category);
		book.setStatus(LoanStatus.AVAILABLE); // Set status
		bookRepository.save(book);

		// Retrieve the saved book by ID
		Book foundBook = bookRepository.findById(book.getId()).orElse(null);

		// Assert that the retrieved book matches the saved book
		assertThat(foundBook).isNotNull();
		assertThat(foundBook.getTitle()).isEqualTo("Test Book");
		assertThat(foundBook.getAuthor()).isEqualTo("Test Author");
		assertThat(foundBook.getPublicationYear()).isEqualTo(2023);
		assertThat(foundBook.getIsbn()).isEqualTo("1234567890");
		assertThat(foundBook.getPrice()).isEqualTo(19.99);
		assertThat(foundBook.getCategory()).isEqualTo(category);
		assertThat(foundBook.getStatus()).isEqualTo(LoanStatus.AVAILABLE);
	}

	@Test
	public void testFindAll() {
		// Save a few books to the repository
		Book book1 = new Book("Book 1", "Author 1", 2021, "1234567890", 19.99, null);
		Book book2 = new Book("Book 2", "Author 2", 2022, "2345678901", 29.99, null);
		bookRepository.save(book1);
		bookRepository.save(book2);

		// Retrieve all books from the repository
		Iterable<Book> allBooks = bookRepository.findAll();

		// Assert that the list of books is not empty and contains the books
		assertThat(allBooks).isNotEmpty();
		assertThat(allBooks).contains(book1, book2);
	}

	@Test
	public void testUpdate() {
		// Create and save a book to the repository
		Book book = new Book("Book to Update", "Author", 2023, "3456789012", 39.99, null);
		bookRepository.save(book);

		// Retrieve the book from the repository
		Book retrievedBook = bookRepository.findById(book.getId()).orElse(null);

		// Modify some properties of the retrieved book
		retrievedBook.setTitle("Updated Book Title");
		retrievedBook.setAuthor("Updated Author");
		bookRepository.save(retrievedBook);

		// Retrieve the updated book from the repository
		Book updatedBook = bookRepository.findById(retrievedBook.getId()).orElse(null);

		// Assert that the retrieved book properties are updated
		assertThat(updatedBook).isNotNull();
		assertThat(updatedBook.getTitle()).isEqualTo("Updated Book Title");
		assertThat(updatedBook.getAuthor()).isEqualTo("Updated Author");
	}

	@Test
	public void testDelete() {
		// Create and save a book to the repository
		Book book = new Book("Book to Delete", "Author", 2023, "3456789012", 39.99, null);
		bookRepository.save(book);

		// Delete the book from the repository
		bookRepository.delete(book);

		// Try to retrieve the deleted book by ID
		Optional<Book> deletedBook = bookRepository.findById(book.getId());

		// Assert that the deleted book is not present
		assertThat(deletedBook.isPresent()).isFalse();
	}
}
