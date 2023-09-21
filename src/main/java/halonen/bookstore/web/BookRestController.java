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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import halonen.bookstore.domain.Book;
import halonen.bookstore.domain.BookRepository;
import halonen.bookstore.domain.Category;
import halonen.bookstore.domain.CategoryRepository;

@RestController
@RequestMapping("/api")
public class BookRestController {
	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	// Get All Books
	@GetMapping("/books")
	public ResponseEntity<List<Book>> bookListRest() {
	    try {
	        List<Book> books = (List<Book>) bookRepository.findAll();
	        return new ResponseEntity<>(books, HttpStatus.OK);
	    } catch (Exception e) {
	        // Log the error and return an error response
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	// Get One Book
	@GetMapping("/books/{id}")
	public ResponseEntity<Book> getBookById(@PathVariable("id") Long bookId) {
	    try {
	        Optional<Book> bookOptional = bookRepository.findById(bookId);
	        if (bookOptional.isPresent()) {
	            return new ResponseEntity<>(bookOptional.get(), HttpStatus.OK);
	        } else {
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        }
	    } catch (Exception e) {
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	// Add New Book
	@PostMapping("/books")
    public ResponseEntity<Book> addBook(@RequestBody Book newBook) {
        try {
            // Save the new book to the repository
            Book savedBook = bookRepository.save(newBook);
            return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	// Delete Book
    @DeleteMapping("/books/{id}")
    public ResponseEntity<Void> deleteBookById(@PathVariable("id") Long bookId) {
        try {
            // Check if the book exists
            if (bookRepository.existsById(bookId)) {
                // If it exists, delete it
            	bookRepository.deleteById(bookId);
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
    @PutMapping("/books/{id}")
    public ResponseEntity<Book> updateBook(
            @PathVariable("id") Long bookId,
            @RequestBody Book updatedBook) {
        try {
            Optional<Book> existingBookOptional = bookRepository.findById(bookId);
            if (existingBookOptional.isPresent()) {
                Book existingBook = existingBookOptional.get();

                // Update the properties of the existing book with the new values
                existingBook.setTitle(updatedBook.getTitle());
                existingBook.setAuthor(updatedBook.getAuthor());
                existingBook.setPublicationYear(updatedBook.getPublicationYear());
                existingBook.setIsbn(updatedBook.getIsbn());
                existingBook.setPrice(updatedBook.getPrice());
                existingBook.setCategory(updatedBook.getCategory());


                // Save the updated book to the repository
                Book savedBook = bookRepository.save(existingBook);
                return new ResponseEntity<>(savedBook, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Print the exception for debugging
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // CRUD operations for Categories
    @GetMapping("/categories")
    public ResponseEntity<List<Category>> categoryListRest() {
        try {
            List<Category> categories = (List<Category>) categoryRepository.findAll();
            return new ResponseEntity<>(categories, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable("id") Long categoryId) {
        try {
            Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
            if (categoryOptional.isPresent()) {
                return new ResponseEntity<>(categoryOptional.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/categories")
    public ResponseEntity<Category> addCategory(@RequestBody Category newCategory) {
        try {
            Category savedCategory = categoryRepository.save(newCategory);
            return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable("id") Long categoryId) {
        try {
            if (categoryRepository.existsById(categoryId)) {
                categoryRepository.deleteById(categoryId);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
