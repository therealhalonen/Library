package halonen.bookstore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.context.annotation.Bean;

// import halonen.bookstore.domain.Book;
// import halonen.bookstore.domain.BookRepository;
// import halonen.bookstore.domain.Category;
// import halonen.bookstore.domain.CategoryRepository;

@SpringBootApplication
public class BookstoreApplication {
	private static final Logger log = LoggerFactory.getLogger(BookstoreApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(BookstoreApplication.class, args);
	}

	/*
	 * @Bean public CommandLineRunner bookStoreTest(BookRepository repository,
	 * CategoryRepository categoryRepository) { return (args) -> {
	 * log.info("Initianing categories");
	 * 
	 * categoryRepository.save(new Category("Bad")); categoryRepository.save(new
	 * Category("Good"));
	 * 
	 * log.info("Saving books");
	 * 
	 * repository.save(new Book("GoodBook", "Me", 1985, "Test_ISBN", 6.66,
	 * categoryRepository.findByName("Good").get(0))); repository.save(new
	 * Book("BadBook", "SomeoneElse", 2003, "Huhuu", 66.60,
	 * categoryRepository.findByName("Bad").get(0)));
	 * 
	 * log.info("Fetching all books"); for (Book book : repository.findAll()) {
	 * log.info(book.toString()); } }; }
	 */
}
