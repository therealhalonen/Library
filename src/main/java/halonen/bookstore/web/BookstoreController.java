package halonen.bookstore.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import halonen.bookstore.domain.Book;
import halonen.bookstore.domain.BookRepository;
import halonen.bookstore.domain.Category;
import halonen.bookstore.domain.CategoryRepository;
import halonen.bookstore.domain.UserRepository;

@Controller
public class BookstoreController {
	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private UserRepository userRepository;

	// Login stuffz
	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/approval")
	public String approvalPage() {
		// Logic to prepare data or perform actions related to the approval page
		return "approval"; // Return the Thymeleaf template or view name for the approval page
	}

	// Create Book
	@GetMapping(value = "/add")
	public String addBook(Model model) {
		model.addAttribute("book", new Book());
		model.addAttribute("category", new Category());
		model.addAttribute("categories", categoryRepository.findAll());
		return "addbook";
	}

	// Create Category
	@GetMapping(value = "/addcategory")
	public String addCategory(Model model) {
		model.addAttribute("category", new Category());
		model.addAttribute("categories", categoryRepository.findAll());
		return "addcategory";
	}

	// Read Books
	/*
	 * @GetMapping(value = { "/", "/booklist" }) public String bookList(Model model)
	 * { model.addAttribute("books", repository.findAll());
	 * model.addAttribute("categories", categoryRepository.findAll()); Model user =
	 * model.addAttribute("user", userRepository.findAll());
	 * System.out.println(user.toString()); return "booklist"; }
	 */

	@GetMapping(value = { "/", "/booklist" })
	public String bookList(Model model, @RequestParam(value = "sortField", defaultValue = "title") String sortField,
			@RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection) {
		// Get books based on sorting criteria and direction
		Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
		Sort sort = Sort.by(direction, sortField);

		List<Book> books = bookRepository.findAll(sort);

		model.addAttribute("books", books);
		model.addAttribute("categories", categoryRepository.findAll());
		model.addAttribute("user", userRepository.findAll());

		return "booklist";
	}

	@GetMapping("/booklist/sort")
	public String sortBooks(@RequestParam("field") String field, @RequestParam("direction") String direction) {
		// Redirect to the /booklist page with sorting parameters
		return "redirect:/booklist?sortField=" + field + "&sortDirection=" + direction;
	}

	// Update Book
	@GetMapping(value = "/edit/{id}")
	public String editBook(@PathVariable("id") Long bookId, Model model) {
		model.addAttribute("book", bookRepository.findById(bookId));
		model.addAttribute("categories", categoryRepository.findAll());
		return "editbook";
	}

	// Delete Book
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@GetMapping(value = "/delete/{id}")
	public String deleteBook(@PathVariable("id") Long bookId, Model model) {
		bookRepository.deleteById(bookId);
		return "redirect:../booklist";
	}

	// Delete Category
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@GetMapping(value = "/deletecategory/{id}")
	public String deleteCategory(@PathVariable("id") Long categoryId) {
		categoryRepository.deleteById(categoryId);
		return "redirect:/addcategory";
	}

	// Save Book
	@PostMapping(value = "/save")
	public String saveBook(Book book) {
		bookRepository.save(book);
		return "redirect:booklist";
	}
	
	// Save Book Simple
	@PostMapping(value = "/savecategorysimple")
	public String saveCategorySimple(Category category) {
		categoryRepository.save(category);
		return "redirect:addcategory";
	}
	// Save Category - Complicated Finally working!!!! 3.9.2023
	@PostMapping(value = "/savecategory", produces = "application/json")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> saveCategory(@RequestBody Category category) {
		try {
			// Save the category to the database
			categoryRepository.save(category);

			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("categoryid", category.getCategoryid());
			response.put("name", category.getName());

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			// Handle any exceptions that occur during the save operation
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("success", false);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

}