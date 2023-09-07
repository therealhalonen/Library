package halonen.bookstore.web;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import halonen.bookstore.domain.Book;
import halonen.bookstore.domain.BookRepository;

@RestController
public class BookRestController {
	@Autowired
	private BookRepository repository;

	@GetMapping("/allbooks")
	public ResponseEntity<List<Book>> bookListRest() {
	    try {
	        List<Book> books = (List<Book>) repository.findAll();
	        return new ResponseEntity<>(books, HttpStatus.OK);
	    } catch (Exception e) {
	        // Log the error and return an error response
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	@GetMapping("/{id}")
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

}
