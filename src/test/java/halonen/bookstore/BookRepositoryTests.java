package halonen.bookstore;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import halonen.bookstore.domain.Book;
import halonen.bookstore.domain.BookRepository;

import static org.assertj.core.api.Assertions.assertThat;

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
        bookRepository.save(book);

        // Retrieve the saved book by ID
        Book foundBook = bookRepository.findById(book.getId()).orElse(null);

        // Assert that the retrieved book matches the saved book
        assertThat(foundBook).isNotNull();
        assertThat(foundBook.getTitle()).isEqualTo("Test Book");
        assertThat(foundBook.getAuthor()).isEqualTo("Test Author");
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

        // Modify some properties of the book
        book.setTitle("Updated Book Title");
        book.setAuthor("Updated Author");
        bookRepository.save(book);

        // Retrieve the updated book from the repository
        Book updatedBook = bookRepository.findById(book.getId()).orElse(null);

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
        Book deletedBook = bookRepository.findById(book.getId()).orElse(null);

        // Assert that the deleted book is null (not found)
        assertThat(deletedBook).isNull();
    }
}
