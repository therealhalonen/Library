package halonen.bookstore.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import halonen.bookstore.domain.Book;
import halonen.bookstore.domain.BookRepository;
import halonen.bookstore.domain.Category;
import halonen.bookstore.domain.CategoryRepository;

@Controller
public class BookstoreController {
	@Autowired
	private BookRepository repository;
	@Autowired
	private CategoryRepository categoryRepository;

	// Create Book
	@RequestMapping(value = "/add")
	public String addBook(Model model) {
		model.addAttribute("book", new Book());
		model.addAttribute("category", new Category());
		model.addAttribute("categories", categoryRepository.findAll());
		return "addbook";
	}
	
	// Create Category
	@RequestMapping(value = "/addcategory")
	public String addCategory(Model model) {
	    model.addAttribute("category", new Category());
	    model.addAttribute("categories", categoryRepository.findAll());
	    return "addcategory";
	}
	
	// Read Books
	@RequestMapping(value = { "/", "/booklist" })
	public String bookList(Model model) {
		model.addAttribute("books", repository.findAll());
		model.addAttribute("categories", categoryRepository.findAll());
		return "booklist";
	}

	// Update Book
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String editBook(@PathVariable("id") Long bookId, Model model) {
		model.addAttribute("book", repository.findById(bookId));
		model.addAttribute("categories", categoryRepository.findAll());
		return "editbook";
	}

	// Delete Book
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public String deleteBook(@PathVariable("id") Long bookId, Model model) {
		repository.deleteById(bookId);
		return "redirect:../booklist";
	}
	
	// Delete Category
	@RequestMapping(value = "/deletecategory/{id}", method = RequestMethod.GET)
	public String deleteCategory(@PathVariable("id") Long categoryId) {
	    categoryRepository.deleteById(categoryId);
	    return "redirect:/addcategory";
	}
	
	// Save Book
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String saveBook(Book book) {
		repository.save(book);
		return "redirect:booklist";
	}
	
	// Save Category
	// Edited to handle page reload and stuffz thanks to Spring Boot, AJAX etc etc documentation.
	@RequestMapping(value = "/savecategory", method = RequestMethod.POST)
	public ResponseEntity<String> saveCategory(@RequestBody Category category) {
	    try {
	        categoryRepository.save(category);
	        return ResponseEntity.ok("{\"success\": true}");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"success\": false}");
	    }
	}
}