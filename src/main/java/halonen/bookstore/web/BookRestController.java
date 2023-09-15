package halonen.bookstore.web;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import halonen.bookstore.domain.Book;
import halonen.bookstore.domain.BookRepository;

@RestController
@RequestMapping("/api")
public class BookRestController {
	@Autowired
	private BookRepository repository;

	@GetMapping("/books")
	public ResponseEntity<List<Book>> bookListRest() {
	    try {
	        List<Book> books = (List<Book>) repository.findAll();
	        return new ResponseEntity<>(books, HttpStatus.OK);
	    } catch (Exception e) {
	        // Log the error and return an error response
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	@GetMapping("/books/{id}")
	public ResponseEntity<Book> getBookById(@PathVariable("id") Long bookId) {
	    try {
	        Optional<Book> bookOptional = repository.findById(bookId);
	        if (bookOptional.isPresent()) {
	            return new ResponseEntity<>(bookOptional.get(), HttpStatus.OK);
	        } else {
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        }
	    } catch (Exception e) {
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	@PostMapping("/books")
    public ResponseEntity<Book> addBook(@RequestBody Book newBook) {
        try {
            // Save the new book to the repository
            Book savedBook = repository.save(newBook);
            return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
	
    @DeleteMapping("/books/{id}")
    public ResponseEntity<Void> deleteBookById(@PathVariable("id") Long bookId) {
        try {
            // Check if the book exists
            if (repository.existsById(bookId)) {
                // If it exists, delete it
                repository.deleteById(bookId);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                // If the book is not found, return a 404 status
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            // If an error occurs during deletion, return an error status
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
